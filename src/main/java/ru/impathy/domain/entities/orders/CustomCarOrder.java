package ru.impathy.domain.entities.orders;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.users.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class CustomCarOrder {
    private UUID id;
    private User client;
    private User manager;
    private Car configuredCar;
    private CustomOrderStatus status;
    private LocalDateTime createdAt;

    public CustomCarOrder(UUID id, User client, User manager, Car configuredCar, CustomOrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.manager = manager;
        this.configuredCar = configuredCar;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public User getManager() {
        return manager;
    }

    public Car getConfiguredCar() {
        return configuredCar;
    }

    public CustomOrderStatus getStatus() {
        return status;
    }

    public void setStatus(CustomOrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
