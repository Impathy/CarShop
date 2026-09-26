package ru.impathy.integration.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request to create an in-stock order")
public class CreateInStockOrderRequest {
    @NotNull
    @Schema(description = "Identifier of the car", example = "20000000-0000-0000-0000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID carId;

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }
}
