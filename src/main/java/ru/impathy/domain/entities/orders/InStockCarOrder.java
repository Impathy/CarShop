package ru.impathy.domain.entities.orders;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.users.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class InStockCarOrder {
    private UUID id;
    private User client;
    private User manager;
    private Car car;
    private InStockOrderStatus status;
    private LocalDateTime createdAt;

    public InStockCarOrder(UUID id, User client, User manager, Car car, InStockOrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.manager = manager;
        this.car = car;
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

    public Car getCar() {
        return car;
    }

    public InStockOrderStatus getStatus() {
        return status;
    }

    public void setStatus(InStockOrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
