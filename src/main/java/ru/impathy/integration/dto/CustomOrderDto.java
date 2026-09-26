package ru.impathy.integration.dto;

import ru.impathy.domain.entities.orders.CustomOrderStatus;

import java.time.Instant;
import java.util.UUID;

public class CustomOrderDto {
    private UUID id;
    private UUID clientId;
    private UUID managerId;
    private UUID configuredCarId;
    private CustomOrderStatus status;
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

    public UUID getConfiguredCarId() {
        return configuredCarId;
    }

    public void setConfiguredCarId(UUID configuredCarId) {
        this.configuredCarId = configuredCarId;
    }

    public CustomOrderStatus getStatus() {
        return status;
    }

    public void setStatus(CustomOrderStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
