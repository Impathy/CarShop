package ru.impathy.integration.dto;

import ru.impathy.domain.entities.orders.InStockOrderStatus;

import java.time.Instant;
import java.util.UUID;

public class InStockOrderDto {
    private UUID id;
    private UUID clientId;
    private UUID managerId;
    private UUID carId;
    private InStockOrderStatus status;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public InStockOrderStatus getStatus() {
        return status;
    }

    public void setStatus(InStockOrderStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
