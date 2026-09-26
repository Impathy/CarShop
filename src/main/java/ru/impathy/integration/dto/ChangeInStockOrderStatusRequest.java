package ru.impathy.integration.dto;

import jakarta.validation.constraints.NotNull;
import ru.impathy.domain.entities.orders.InStockOrderStatus;

public class ChangeInStockOrderStatusRequest {
    @NotNull
    private InStockOrderStatus status;

    public InStockOrderStatus getStatus() {
        return status;
    }

    public void setStatus(InStockOrderStatus status) {
        this.status = status;
    }
}
