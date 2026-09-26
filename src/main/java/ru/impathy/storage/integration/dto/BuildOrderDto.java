package ru.impathy.storage.integration.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class BuildOrderDto {
    private UUID id;
    private UUID sourceOrderId;
    private String sourceOrderType;
    private UUID carId;
    private UUID managerId;
    private UUID warehouseEmployeeId;
    private String traceId;
    private String status;
    private Set<UUID> requiredComponentIds;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean removed;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(UUID sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public UUID getWarehouseEmployeeId() {
        return warehouseEmployeeId;
    }

    public void setWarehouseEmployeeId(UUID warehouseEmployeeId) {
        this.warehouseEmployeeId = warehouseEmployeeId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<UUID> getRequiredComponentIds() {
        return requiredComponentIds;
    }

    public void setRequiredComponentIds(Set<UUID> requiredComponentIds) {
        this.requiredComponentIds = requiredComponentIds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
