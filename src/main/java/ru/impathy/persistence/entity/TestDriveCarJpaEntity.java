package ru.impathy.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_drive_cars")
public class TestDriveCarJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarJpaEntity car;

    public CarJpaEntity getCar() {
        return car;
    }

    public void setCar(CarJpaEntity car) {
        this.car = car;
    }
}
