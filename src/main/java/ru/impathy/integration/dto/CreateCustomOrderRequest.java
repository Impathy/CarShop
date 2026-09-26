package ru.impathy.integration.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Request to create a custom order")
public class CreateCustomOrderRequest {
    @NotNull
    @Schema(description = "Identifier of the configured car", example = "20000000-0000-0000-0000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID configuredCarId;

    public UUID getConfiguredCarId() {
        return configuredCarId;
    }

    public void setConfiguredCarId(UUID configuredCarId) {
        this.configuredCarId = configuredCarId;
    }
}
