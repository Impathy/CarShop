package ru.impathy.domain.entities.testDrive;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.users.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDriveRequest {
    private UUID id;
    private User client;
    private Car car;
    private LocalDateTime startAt;

    public TestDriveRequest(UUID id, User client, Car car, LocalDateTime startAt) {
        this.id = id;
        this.client = client;
        this.car = car;
        this.startAt = startAt;
    }

    public UUID getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public Car getCar() {
        return car;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }
}
