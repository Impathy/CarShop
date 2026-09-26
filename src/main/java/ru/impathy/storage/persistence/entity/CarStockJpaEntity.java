package ru.impathy.storage.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.impathy.persistence.entity.BaseJpaEntity;

import java.util.UUID;

@Entity
@Table(name = "car_stock")
public class CarStockJpaEntity extends BaseJpaEntity {
    @Column(name = "car_id", nullable = false, unique = true)
    private UUID carId;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public boolean isAvailable() {
        return totalQuantity > reservedQuantity;
    }
}
