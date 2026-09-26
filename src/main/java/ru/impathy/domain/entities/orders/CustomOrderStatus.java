package ru.impathy.domain.entities.orders;

public enum CustomOrderStatus {
    CREATED,
    APPROVED_BY_WAREHOUSE,
    WAITING_FOR_PAYMENT,
    PAID,
    WAITING_FOR_DELIVERY,
    READY_FOR_DELIVERY,
    COMPLETED,
    CANCELED
}
