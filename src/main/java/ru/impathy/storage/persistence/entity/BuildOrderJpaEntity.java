package ru.impathy.storage.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import ru.impathy.persistence.entity.BaseJpaEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "build_orders")
public class BuildOrderJpaEntity extends BaseJpaEntity {
    @Column(name = "source_order_id", nullable = false)
    private UUID sourceOrderId;

    @Column(name = "source_order_type", nullable = false)
    private String sourceOrderType;

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "warehouse_employee_id", nullable = false)
    private UUID warehouseEmployeeId;

    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @Column(name = "status", nullable = false)
    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "build_order_component_ids", joinColumns = @JoinColumn(name = "build_order_id"))
    @Column(name = "component_id", nullable = false)
    private Set<UUID> requiredComponentIds = new HashSet<>();

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
}
