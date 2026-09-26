package ru.impathy.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.impathy.domain.entities.orders.InStockOrderStatus;

@Entity
@Table(name = "in_stock_car_orders")
public class InStockCarOrderJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserJpaEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private UserJpaEntity manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarJpaEntity car;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InStockOrderStatus status;

    public UserJpaEntity getClient() {
        return client;
    }

    public void setClient(UserJpaEntity client) {
        this.client = client;
    }

    public UserJpaEntity getManager() {
        return manager;
    }

    public void setManager(UserJpaEntity manager) {
        this.manager = manager;
    }

    public CarJpaEntity getCar() {
        return car;
    }

    public void setCar(CarJpaEntity car) {
        this.car = car;
    }

    public InStockOrderStatus getStatus() {
        return status;
    }

    public void setStatus(InStockOrderStatus status) {
        this.status = status;
    }
}
