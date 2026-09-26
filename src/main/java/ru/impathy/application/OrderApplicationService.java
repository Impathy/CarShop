package ru.impathy.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.domain.entities.orders.CustomOrderStatus;
import ru.impathy.domain.entities.orders.InStockOrderStatus;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.domain.users.AccessLevel;
import ru.impathy.messaging.OrderApprovalEvent;
import ru.impathy.messaging.OrderType;
import ru.impathy.integration.dto.CustomOrderDto;
import ru.impathy.integration.dto.InStockOrderDto;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.entity.CustomCarOrderJpaEntity;
import ru.impathy.persistence.entity.InStockCarOrderJpaEntity;
import ru.impathy.persistence.entity.UserJpaEntity;
import ru.impathy.persistence.repository.CarJpaRepository;
import ru.impathy.persistence.repository.CustomCarOrderJpaRepository;
import ru.impathy.persistence.repository.InStockCarOrderJpaRepository;
import ru.impathy.persistence.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Profile("order")
public class OrderApplicationService {
    private final InStockCarOrderJpaRepository inStockRepository;
    private final CustomCarOrderJpaRepository customRepository;
    private final UserJpaRepository userRepository;
    private final CarJpaRepository carRepository;
    private final OrderOutboxService orderOutboxService;
    private int nextManagerIndex;

    public OrderApplicationService(InStockCarOrderJpaRepository inStockRepository,
                                   CustomCarOrderJpaRepository customRepository,
                                   UserJpaRepository userRepository,
                                   CarJpaRepository carRepository,
                                   OrderOutboxService orderOutboxService) {
        this.inStockRepository = inStockRepository;
        this.customRepository = customRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.orderOutboxService = orderOutboxService;
        this.nextManagerIndex = 0;
    }

    @Transactional
    public InStockOrderDto createInStockOrder(UUID currentUserId, UUID carId) {
        UserJpaEntity client = getClient(currentUserId);
        CarJpaEntity car = carRepository.findByIdAndRemovedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
        UserJpaEntity manager = getNextManager();

        InStockCarOrderJpaEntity order = new InStockCarOrderJpaEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setCar(car);
        order.setStatus(InStockOrderStatus.CREATED);

        return toDto(inStockRepository.save(order));
    }

    @Transactional
    public CustomOrderDto createCustomOrder(UUID currentUserId, UUID configuredCarId) {
        UserJpaEntity client = getClient(currentUserId);
        CarJpaEntity configuredCar = carRepository.findByIdAndRemovedFalse(configuredCarId)
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + configuredCarId));
        UserJpaEntity manager = getNextManager();

        CustomCarOrderJpaEntity order = new CustomCarOrderJpaEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setConfiguredCar(configuredCar);
        order.setStatus(CustomOrderStatus.CREATED);

        return toDto(customRepository.save(order));
    }

    @Transactional
    public InStockOrderDto changeInStockOrderStatus(UUID managerId, UUID orderId, InStockOrderStatus status) {
        if (status == null) {
            throw new DomainValidationException("Order status is required");
        }
        getManager(managerId);
        InStockCarOrderJpaEntity order = inStockRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId));

        validateInStockStatusTransition(order.getStatus(), status);
        order.setStatus(status);
        InStockOrderDto dto = toDto(inStockRepository.save(order));
        if (status == InStockOrderStatus.PAID) {
            orderOutboxService.enqueueApproval(createInStockApprovalEvent(order));
        }
        return dto;
    }

    @Transactional
    public CustomOrderDto changeCustomOrderStatusByManager(UUID managerId, UUID orderId, CustomOrderStatus status) {
        if (status == null) {
            throw new DomainValidationException("Order status is required");
        }
        if (status == CustomOrderStatus.APPROVED_BY_WAREHOUSE) {
            throw new DomainValidationException("Use warehouse approval operation for APPROVED_BY_WAREHOUSE status");
        }
        getManager(managerId);
        CustomCarOrderJpaEntity order = customRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));

        validateCustomStatusTransition(order.getStatus(), status);
        order.setStatus(status);
        CustomOrderDto dto = toDto(customRepository.save(order));
        if (status == CustomOrderStatus.PAID) {
            orderOutboxService.enqueueApproval(createCustomApprovalEvent(order));
        }
        return dto;
    }

    @Transactional
    public CustomOrderDto approveCustomOrderByWarehouse(UUID orderId) {
        CustomCarOrderJpaEntity order = customRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));
        validateCustomStatusTransition(order.getStatus(), CustomOrderStatus.APPROVED_BY_WAREHOUSE);
        order.setStatus(CustomOrderStatus.APPROVED_BY_WAREHOUSE);
        return toDto(customRepository.save(order));
    }

    @Transactional
    public InStockOrderDto cancelInStockOrderAsOwner(UUID ownerId, UUID orderId) {
        InStockCarOrderJpaEntity order = inStockRepository.findByIdAndClient_IdAndRemovedFalse(orderId, ownerId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId));
        validateInStockStatusTransition(order.getStatus(), InStockOrderStatus.CANCELED);
        order.setStatus(InStockOrderStatus.CANCELED);
        return toDto(inStockRepository.save(order));
    }

    @Transactional
    public CustomOrderDto cancelCustomOrderAsOwner(UUID ownerId, UUID orderId) {
        CustomCarOrderJpaEntity order = customRepository.findByIdAndClient_IdAndRemovedFalse(orderId, ownerId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));
        validateCustomStatusTransition(order.getStatus(), CustomOrderStatus.CANCELED);
        order.setStatus(CustomOrderStatus.CANCELED);
        return toDto(customRepository.save(order));
    }

    @Transactional
    public InStockOrderDto cancelInStockOrderByAdmin(UUID orderId) {
        InStockCarOrderJpaEntity order = inStockRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId));
        validateInStockStatusTransition(order.getStatus(), InStockOrderStatus.CANCELED);
        order.setStatus(InStockOrderStatus.CANCELED);
        return toDto(inStockRepository.save(order));
    }

    @Transactional
    public CustomOrderDto cancelCustomOrderByAdmin(UUID orderId) {
        CustomCarOrderJpaEntity order = customRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));
        validateCustomStatusTransition(order.getStatus(), CustomOrderStatus.CANCELED);
        order.setStatus(CustomOrderStatus.CANCELED);
        return toDto(customRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<InStockOrderDto> listInStockOrdersForUser(UUID userId) {
        return inStockRepository.findAllByClient_IdAndRemovedFalse(userId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CustomOrderDto> listCustomOrdersForUser(UUID userId) {
        return customRepository.findAllByClient_IdAndRemovedFalse(userId).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<InStockOrderDto> listInStockOrdersForManager(UUID managerId) {
        getManager(managerId);
        return inStockRepository.findAllByRemovedFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CustomOrderDto> listCustomOrdersForManager(UUID managerId) {
        getManager(managerId);
        return customRepository.findAllByRemovedFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<InStockOrderDto> listAllInStockOrders() {
        return inStockRepository.findAllByRemovedFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<CustomOrderDto> listAllCustomOrders() {
        return customRepository.findAllByRemovedFalse().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public InStockOrderDto getInStockOrderForUser(UUID userId, UUID orderId) {
        InStockCarOrderJpaEntity order = inStockRepository.findByIdAndClient_IdAndRemovedFalse(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId));
        return toDto(order);
    }

    @Transactional(readOnly = true)
    public CustomOrderDto getCustomOrderForUser(UUID userId, UUID orderId) {
        CustomCarOrderJpaEntity order = customRepository.findByIdAndClient_IdAndRemovedFalse(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));
        return toDto(order);
    }

    @Transactional(readOnly = true)
    public InStockOrderDto getInStockOrderById(UUID orderId) {
        return toDto(inStockRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId)));
    }

    @Transactional(readOnly = true)
    public CustomOrderDto getCustomOrderById(UUID orderId) {
        return toDto(customRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId)));
    }

    @Transactional
    public void applyInStockApproval(UUID orderId, boolean approved) {
        InStockCarOrderJpaEntity order = inStockRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("In-stock order not found: " + orderId));
        order.setStatus(approved ? InStockOrderStatus.READY_FOR_DELIVERY : InStockOrderStatus.CANCELED);
        inStockRepository.save(order);
    }

    @Transactional
    public void applyCustomApproval(UUID orderId, boolean approved) {
        CustomCarOrderJpaEntity order = customRepository.findByIdAndRemovedFalse(orderId)
                .orElseThrow(() -> new EntityNotFoundExeption("Custom order not found: " + orderId));
        order.setStatus(approved ? CustomOrderStatus.READY_FOR_DELIVERY : CustomOrderStatus.CANCELED);
        customRepository.save(order);
    }

    private UserJpaEntity getClient(UUID clientId) {
        requireId(clientId, "clientId");
        return userRepository.findById(clientId)
                .filter(user -> !user.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("User not found: " + clientId));
    }

    private UserJpaEntity getManager(UUID managerId) {
        requireId(managerId, "managerId");
        return userRepository.findById(managerId)
                .filter(user -> !user.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("User not found: " + managerId));
    }

    private UserJpaEntity getNextManager() {
        List<UserJpaEntity> managers = userRepository.findAllByAccessLevelAndRemovedFalse(AccessLevel.MANAGER);
        if (managers.isEmpty()) {
            throw new DomainValidationException("No managers available");
        }
        if (nextManagerIndex >= managers.size()) {
            nextManagerIndex = 0;
        }
        UserJpaEntity manager = managers.get(nextManagerIndex);
        nextManagerIndex = nextManagerIndex + 1;
        return manager;
    }

    private void requireId(UUID id, String fieldName) {
        if (id == null) {
            throw new DomainValidationException(fieldName + " is required");
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

    private InStockOrderDto toDto(InStockCarOrderJpaEntity order) {
        InStockOrderDto dto = new InStockOrderDto();
        dto.setId(order.getId());
        dto.setClientId(order.getClient().getId());
        dto.setManagerId(order.getManager().getId());
        dto.setCarId(order.getCar().getId());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    private CustomOrderDto toDto(CustomCarOrderJpaEntity order) {
        CustomOrderDto dto = new CustomOrderDto();
        dto.setId(order.getId());
        dto.setClientId(order.getClient().getId());
        dto.setManagerId(order.getManager().getId());
        dto.setConfiguredCarId(order.getConfiguredCar().getId());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    private OrderApprovalEvent createInStockApprovalEvent(InStockCarOrderJpaEntity order) {
        OrderApprovalEvent event = new OrderApprovalEvent();
        event.setOrderId(order.getId());
        event.setOrderType(OrderType.IN_STOCK);
        event.setClientId(order.getClient().getId());
        event.setManagerId(order.getManager().getId());
        event.setCarId(order.getCar().getId());
        event.setTraceId(order.getId().toString());
        event.setEventId(java.util.UUID.randomUUID());
        return event;
    }

    private OrderApprovalEvent createCustomApprovalEvent(CustomCarOrderJpaEntity order) {
        OrderApprovalEvent event = new OrderApprovalEvent();
        event.setOrderId(order.getId());
        event.setOrderType(OrderType.CUSTOM);
        event.setClientId(order.getClient().getId());
        event.setManagerId(order.getManager().getId());
        event.setConfiguredCarId(order.getConfiguredCar().getId());
        event.setTraceId(order.getId().toString());
        event.setEventId(java.util.UUID.randomUUID());
        return event;
    }
}
