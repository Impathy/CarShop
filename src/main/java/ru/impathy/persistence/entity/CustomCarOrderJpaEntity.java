package ru.impathy.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.impathy.domain.entities.orders.CustomOrderStatus;

@Entity
@Table(name = "custom_car_orders")
public class CustomCarOrderJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserJpaEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private UserJpaEntity manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "configured_car_id", nullable = false)
    private CarJpaEntity configuredCar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomOrderStatus status;

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

    public CarJpaEntity getConfiguredCar() {
        return configuredCar;
    }

    public void setConfiguredCar(CarJpaEntity configuredCar) {
        this.configuredCar = configuredCar;
    }

    public CustomOrderStatus getStatus() {
        return status;
    }

    public void setStatus(CustomOrderStatus status) {
        this.status = status;
    }
}
