package ru.impathy.storage.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import ru.impathy.messaging.OrderApprovalEvent;
import ru.impathy.messaging.OrderApprovalResultEvent;
import ru.impathy.messaging.ProcessedMessageService;
import ru.impathy.storage.application.BuildOrderApplicationService;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.repository.CarJpaRepository;
import ru.impathy.storage.persistence.entity.CarStockJpaEntity;
import ru.impathy.storage.persistence.entity.PartStockJpaEntity;
import ru.impathy.storage.persistence.repository.CarStockJpaRepository;
import ru.impathy.storage.persistence.repository.PartStockJpaRepository;

@Component
@Profile("storage & !test")
public class StorageOrderApprovalListener {
    private final BuildOrderApplicationService buildOrderService;
    private final CarJpaRepository carRepository;
    private final StorageOrderResultPublisher resultPublisher;
    private final ProcessedMessageService processedMessageService;
    private final CarStockJpaRepository carStockRepository;
    private final PartStockJpaRepository partStockRepository;
    private final java.util.UUID defaultWarehouseEmployeeId;

    public StorageOrderApprovalListener(BuildOrderApplicationService buildOrderService,
                                        CarJpaRepository carRepository,
                                        StorageOrderResultPublisher resultPublisher,
                                        ProcessedMessageService processedMessageService,
                                        CarStockJpaRepository carStockRepository,
                                        PartStockJpaRepository partStockRepository,
                                        @Value("${app.storage.default-warehouse-user-id:00000000-0000-0000-0000-000000000003}") java.util.UUID defaultWarehouseEmployeeId) {
        this.buildOrderService = buildOrderService;
        this.carRepository = carRepository;
        this.resultPublisher = resultPublisher;
        this.processedMessageService = processedMessageService;
        this.carStockRepository = carStockRepository;
        this.partStockRepository = partStockRepository;
        this.defaultWarehouseEmployeeId = defaultWarehouseEmployeeId;
    }

    @RabbitListener(queues = "impathy.order.approval.queue")
    @Transactional
    public void handle(OrderApprovalEvent event) {
        String messageId = event.getEventId() == null ? null : event.getEventId().toString();
        if (messageId != null && processedMessageService.alreadyProcessed(messageId)) {
            return;
        }
        boolean approved = approve(event);
        OrderApprovalResultEvent result = new OrderApprovalResultEvent();
        result.setEventId(event.getEventId());
        result.setOrderId(event.getOrderId());
        result.setOrderType(event.getOrderType());
        result.setTraceId(event.getTraceId());
        result.setApproved(approved);
        result.setReason(approved ? null : "Car or components are not available");
        resultPublisher.publish(result);
        if (messageId != null) {
            processedMessageService.markProcessed(messageId, "ORDER_APPROVAL");
        }
    }

    private boolean approve(OrderApprovalEvent event) {
        CarJpaEntity car = null;
        if (event.getCarId() != null) {
            car = carRepository.findByIdAndRemovedFalse(event.getCarId()).orElse(null);
        } else if (event.getConfiguredCarId() != null) {
            car = carRepository.findByIdAndRemovedFalse(event.getConfiguredCarId()).orElse(null);
        }
        if (car == null) {
            buildOrderService.create(event.getOrderId(), event.getOrderType().name(), null, event.getManagerId(), defaultWarehouseEmployeeId, event.getTraceId(), java.util.Set.of());
            buildOrderService.markFailed(event.getOrderId());
            return false;
        }
        java.util.Set<java.util.UUID> componentIds = car.getBaseParts().stream()
                .map(ru.impathy.persistence.entity.CarPartJpaEntity::getId)
                .collect(java.util.stream.Collectors.toSet());
        buildOrderService.create(event.getOrderId(), event.getOrderType().name(), car.getId(), event.getManagerId(), defaultWarehouseEmployeeId, event.getTraceId(), componentIds);
        if (!reserveStock(car.getId(), componentIds)) {
            buildOrderService.markFailed(event.getOrderId());
            return false;
        }
        buildOrderService.markAssembled(event.getOrderId());
        return true;
    }

    private boolean reserveStock(java.util.UUID carId, java.util.Set<java.util.UUID> componentIds) {
        CarStockJpaEntity carStock = carStockRepository.findByCarIdAndRemovedFalse(carId).orElse(null);
        if (carStock == null || !carStock.isAvailable()) {
            return false;
        }
        for (java.util.UUID componentId : componentIds) {
            PartStockJpaEntity partStock = partStockRepository.findByCarPartIdAndRemovedFalse(componentId).orElse(null);
            if (partStock == null || !partStock.isAvailable()) {
                return false;
            }
        }
        carStock.setReservedQuantity(carStock.getReservedQuantity() + 1);
        carStockRepository.save(carStock);
        for (java.util.UUID componentId : componentIds) {
            PartStockJpaEntity partStock = partStockRepository.findByCarPartIdAndRemovedFalse(componentId).orElse(null);
            if (partStock != null) {
                partStock.setReservedQuantity(partStock.getReservedQuantity() + 1);
                partStockRepository.save(partStock);
            }
        }
        return true;
    }
}
