package ru.impathy.integration.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateTestDriveCarRequest {
    @NotNull
    private UUID carId;

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }
}
