package ru.impathy.service;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.entities.orders.CustomCarOrder;
import ru.impathy.domain.entities.orders.CustomOrderStatus;
import ru.impathy.domain.entities.orders.InStockCarOrder;
import ru.impathy.domain.entities.orders.InStockOrderStatus;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.domain.users.AccessLevel;
import ru.impathy.domain.users.User;
import ru.impathy.repository.CarRepository;
import ru.impathy.repository.CustomCarOrderRepository;
import ru.impathy.repository.InStockCarOrderRepository;
import ru.impathy.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private InStockCarOrderRepository inStockCarOrderRepository;
    private CustomCarOrderRepository customCarOrderRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private int nextManagerIndex;

    public OrderService(InStockCarOrderRepository inStockCarOrderRepository,
                        CustomCarOrderRepository customCarOrderRepository,
                        UserRepository userRepository,
                        CarRepository carRepository) {
        this.inStockCarOrderRepository = inStockCarOrderRepository;
        this.customCarOrderRepository = customCarOrderRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.nextManagerIndex = 0;
    }

    public InStockCarOrder createInStockOrder(UUID clientId, UUID carId) {
        requireId(clientId, "clientId");
        requireId(carId, "carId");

        User client = getClient(clientId);
        Car car = carRepository.findByIdForClient(client, carId).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
        User manager = getNextManager();

        InStockCarOrder order = new InStockCarOrder(
                UUID.randomUUID(),
                client,
                manager,
                car,
                InStockOrderStatus.CREATED,
                LocalDateTime.now());
        return inStockCarOrderRepository.saveByClient(client, order);
    }

    public CustomCarOrder createCustomOrder(UUID clientId, Car configuredCar) {
        requireId(clientId, "clientId");
        validateConfiguredCar(configuredCar);

        User client = getClient(clientId);
        User manager = getNextManager();

        CustomCarOrder order = new CustomCarOrder(
                UUID.randomUUID(),
                client,
                manager,
                configuredCar,
                CustomOrderStatus.CREATED,
                LocalDateTime.now());
        return customCarOrderRepository.saveByClient(client, order);
    }

    public InStockCarOrder changeInStockOrderStatus(UUID managerId, UUID orderId, InStockOrderStatus status) {
        requireId(managerId, "managerId");
        requireId(orderId, "orderId");
        if (status == null) {
            throw new DomainValidationException("Order status is required");
        }

        User manager = getManager(managerId);
        InStockCarOrder order = inStockCarOrderRepository.findByIdForManager(manager, orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId));

        validateInStockStatusTransition(order.getStatus(), status);

        order.setStatus(status);
        return inStockCarOrderRepository.saveByManager(manager, order);
    }

    public CustomCarOrder changeCustomOrderStatus(UUID managerId, UUID orderId, CustomOrderStatus status) {
        requireId(managerId, "managerId");
        requireId(orderId, "orderId");
        if (status == null) {
            throw new DomainValidationException("Order status is required");
        }

        User manager = getManager(managerId);
        CustomCarOrder order = customCarOrderRepository.findByIdForManager(manager, orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));

        validateCustomStatusTransition(order.getStatus(), status);

        order.setStatus(status);
        return customCarOrderRepository.saveByManager(manager, order);
    }

    public List<InStockCarOrder> listInStockOrders(UUID managerId) {
        requireId(managerId, "managerId");
        User manager = getManager(managerId);
        return inStockCarOrderRepository.findAllForManager(manager);
    }

    public List<CustomCarOrder> listCustomOrders(UUID managerId) {
        requireId(managerId, "managerId");
        User manager = getManager(managerId);
        return customCarOrderRepository.findAllForManager(manager);
    }

    private void requireId(UUID id, String fieldName) {
        if (id == null) {
            throw new DomainValidationException(fieldName + " is required");
        }
    }

    private void validateConfiguredCar(Car configuredCar) {
        if (configuredCar == null) {
            throw new DomainValidationException("Configured car is required");
        }
        if (configuredCar.getBrand() == null || configuredCar.getBrand().isBlank()) {
            throw new DomainValidationException("Configured car brand is required");
        }
        if (configuredCar.getModelName() == null || configuredCar.getModelName().isBlank()) {
            throw new DomainValidationException("Configured car modelName is required");
        }
        if (configuredCar.getPrice() < 0) {
            throw new DomainValidationException("Configured car price cannot be negative");
        }
    }

    private void validateInStockStatusTransition(InStockOrderStatus from, InStockOrderStatus to) {
        if (from == InStockOrderStatus.COMPLETED || from == InStockOrderStatus.CANCELED) {
            throw new DomainValidationException("Cannot change terminal order status");
        }

        if (to == InStockOrderStatus.CANCELED) {
            return;
        }

        if (from == InStockOrderStatus.CREATED && to != InStockOrderStatus.APPROVED_BY_MANAGER) {
            throw new DomainValidationException("Invalid in-stock status transition");
        }
        if (from == InStockOrderStatus.APPROVED_BY_MANAGER && to != InStockOrderStatus.WAITING_FOR_PAYMENT) {
            throw new DomainValidationException("Invalid in-stock status transition");
        }
        if (from == InStockOrderStatus.WAITING_FOR_PAYMENT && to != InStockOrderStatus.PAID) {
            throw new DomainValidationException("Invalid in-stock status transition");
        }
        if (from == InStockOrderStatus.PAID && to != InStockOrderStatus.READY_FOR_DELIVERY) {
            throw new DomainValidationException("Invalid in-stock status transition");
        }
        if (from == InStockOrderStatus.READY_FOR_DELIVERY && to != InStockOrderStatus.COMPLETED) {
            throw new DomainValidationException("Invalid in-stock status transition");
        }
    }

    private void validateCustomStatusTransition(CustomOrderStatus from, CustomOrderStatus to) {
        if (from == CustomOrderStatus.COMPLETED || from == CustomOrderStatus.CANCELED) {
            throw new DomainValidationException("Cannot change terminal order status");
        }

        if (to == CustomOrderStatus.CANCELED) {
            return;
        }

        if (from == CustomOrderStatus.CREATED && to != CustomOrderStatus.APPROVED_BY_WAREHOUSE) {
            throw new DomainValidationException("Invalid custom status transition");
        }
        if (from == CustomOrderStatus.APPROVED_BY_WAREHOUSE && to != CustomOrderStatus.WAITING_FOR_PAYMENT) {
            throw new DomainValidationException("Invalid custom status transition");
        }
        if (from == CustomOrderStatus.WAITING_FOR_PAYMENT && to != CustomOrderStatus.PAID) {
            throw new DomainValidationException("Invalid custom status transition");
        }
        if (from == CustomOrderStatus.PAID && to != CustomOrderStatus.WAITING_FOR_DELIVERY) {
            throw new DomainValidationException("Invalid custom status transition");
        }
        if (from == CustomOrderStatus.WAITING_FOR_DELIVERY && to != CustomOrderStatus.READY_FOR_DELIVERY) {
            throw new DomainValidationException("Invalid custom status transition");
        }
        if (from == CustomOrderStatus.READY_FOR_DELIVERY && to != CustomOrderStatus.COMPLETED) {
            throw new DomainValidationException("Invalid custom status transition");
        }
    }

    private User getClient(UUID clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundExeption("User not found: " + clientId));
        if (client.getAccessLevel() != AccessLevel.CLIENT) {
            throw new DomainValidationException("User is not client: " + clientId);
        }
        return client;
    }

    private User getManager(UUID managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundExeption("User not found: " + managerId));
        if (manager.getAccessLevel() != AccessLevel.MANAGER) {
            throw new DomainValidationException("User is not manager: " + managerId);
        }
        return manager;
    }

    private User getNextManager() {
        List<User> managers = userRepository.findByAccessLevel(AccessLevel.MANAGER);
        if (managers.isEmpty()) {
            throw new DomainValidationException("No managers available");
        }
        if (nextManagerIndex >= managers.size()) {
            nextManagerIndex = 0;
        }
        User manager = managers.get(nextManagerIndex);
        nextManagerIndex = nextManagerIndex + 1;
        return manager;
    }
}
