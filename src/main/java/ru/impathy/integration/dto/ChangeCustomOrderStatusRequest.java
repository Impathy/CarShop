package ru.impathy.integration.dto;

import jakarta.validation.constraints.NotNull;
import ru.impathy.domain.entities.orders.CustomOrderStatus;

public class ChangeCustomOrderStatusRequest {
    @NotNull
    private CustomOrderStatus status;

    public CustomOrderStatus getStatus() {
        return status;
    }

    public void setStatus(CustomOrderStatus status) {
        this.status = status;
    }
}
