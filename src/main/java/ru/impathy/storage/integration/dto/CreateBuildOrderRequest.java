package ru.impathy.storage.integration.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import java.util.UUID;

@Schema(description = "Request to create a build order")
public class CreateBuildOrderRequest {
    @NotNull
    @Schema(description = "Identifier of the source order", example = "40000000-0000-0000-0000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID sourceOrderId;

    @NotNull
    @Schema(description = "Type of the source order", example = "IN_STOCK", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sourceOrderType;

    @Schema(description = "Identifier of the car", example = "20000000-0000-0000-0000-000000000001")
    private UUID carId;

    @Schema(description = "Identifier of the manager", example = "00000000-0000-0000-0000-000000000002")
    private UUID managerId;

    @NotNull
    @Schema(description = "Trace identifier", example = "trace-0001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String traceId;

    @ArraySchema(schema = @Schema(example = "10000000-0000-0000-0000-000000000001"))
    @Schema(description = "Identifiers of required components")
    private Set<UUID> requiredComponentIds;

    public UUID getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(UUID sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Set<UUID> getRequiredComponentIds() {
        return requiredComponentIds;
    }

    public void setRequiredComponentIds(Set<UUID> requiredComponentIds) {
        this.requiredComponentIds = requiredComponentIds;
    }
}
