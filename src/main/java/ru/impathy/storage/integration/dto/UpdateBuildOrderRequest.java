package ru.impathy.storage.integration.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to update build order status")
public class UpdateBuildOrderRequest {
    @NotBlank
    @Schema(description = "New build order status", example = "IN_PROGRESS", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
