package ru.impathy.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "test_drive_requests")
public class TestDriveRequestJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserJpaEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarJpaEntity car;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    public UserJpaEntity getClient() {
        return client;
    }

    public void setClient(UserJpaEntity client) {
        this.client = client;
    }

    public CarJpaEntity getCar() {
        return car;
    }

    public void setCar(CarJpaEntity car) {
        this.car = car;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }
}
