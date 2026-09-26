package ru.impathy.integration.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.annotations.Operation;
import ru.impathy.application.OrderApplicationService;
import ru.impathy.integration.dto.ChangeCustomOrderStatusRequest;
import ru.impathy.integration.dto.ChangeInStockOrderStatusRequest;
import ru.impathy.integration.dto.CreateCustomOrderRequest;
import ru.impathy.integration.dto.CreateInStockOrderRequest;
import ru.impathy.integration.dto.CustomOrderDto;
import ru.impathy.integration.dto.InStockOrderDto;
import ru.impathy.security.CurrentUserService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/orders")
@Profile("order")
public class OrderController {
    private final OrderApplicationService service;
    private final CurrentUserService currentUserService;

    public OrderController(OrderApplicationService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/in-stock")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create an in-stock order")
    public InStockOrderDto createInStock(@Valid @RequestBody CreateInStockOrderRequest request) {
        return service.createInStockOrder(currentUserService.getCurrentUserId(), request.getCarId());
    }

    @PostMapping("/custom")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create a custom order")
    public CustomOrderDto createCustom(@Valid @RequestBody CreateCustomOrderRequest request) {
        return service.createCustomOrder(currentUserService.getCurrentUserId(), request.getConfiguredCarId());
    }

    @PatchMapping("/in-stock/{orderId}/status")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public InStockOrderDto changeInStockStatus(@PathVariable UUID orderId,
                                               @Valid @RequestBody ChangeInStockOrderStatusRequest request) {
        return service.changeInStockOrderStatus(currentUserService.getCurrentUserId(), orderId, request.getStatus());
    }

    @PatchMapping("/custom/{orderId}/status")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public CustomOrderDto changeCustomStatus(@PathVariable UUID orderId,
                                             @Valid @RequestBody ChangeCustomOrderStatusRequest request) {
        return service.changeCustomOrderStatusByManager(currentUserService.getCurrentUserId(), orderId, request.getStatus());
    }

    @PatchMapping("/custom/{orderId}/warehouse-approve")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    public CustomOrderDto warehouseApproveCustom(@PathVariable UUID orderId) {
        return service.approveCustomOrderByWarehouse(orderId);
    }

    @PatchMapping("/in-stock/{orderId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isInStockOwner(#orderId, authentication)")
    public InStockOrderDto cancelInStock(@PathVariable UUID orderId) {
        if (hasAdmin()) {
            return service.cancelInStockOrderByAdmin(orderId);
        }
        return service.cancelInStockOrderAsOwner(currentUserService.getCurrentUserId(), orderId);
    }

    @PatchMapping("/custom/{orderId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isCustomOwner(#orderId, authentication)")
    public CustomOrderDto cancelCustom(@PathVariable UUID orderId) {
        if (hasAdmin()) {
            return service.cancelCustomOrderByAdmin(orderId);
        }
        return service.cancelCustomOrderAsOwner(currentUserService.getCurrentUserId(), orderId);
    }

    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public List<InStockOrderDto> listInStock() {
        if (hasManagerOrAdmin()) {
            return hasAdmin() ? service.listAllInStockOrders() : service.listInStockOrdersForManager(currentUserService.getCurrentUserId());
        }
        return service.listInStockOrdersForUser(currentUserService.getCurrentUserId());
    }

    @GetMapping("/custom")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public List<CustomOrderDto> listCustom() {
        if (hasManagerOrAdmin()) {
            return hasAdmin() ? service.listAllCustomOrders() : service.listCustomOrdersForManager(currentUserService.getCurrentUserId());
        }
        return service.listCustomOrdersForUser(currentUserService.getCurrentUserId());
    }

    @GetMapping("/in-stock/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @orderSecurity.isInStockOwner(#orderId, authentication)")
    public InStockOrderDto getInStockById(@PathVariable UUID orderId) {
        if (hasManagerOrAdmin()) {
            return service.getInStockOrderById(orderId);
        }
        return service.getInStockOrderForUser(currentUserService.getCurrentUserId(), orderId);
    }

    @GetMapping("/custom/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @orderSecurity.isCustomOwner(#orderId, authentication)")
    public CustomOrderDto getCustomById(@PathVariable UUID orderId) {
        if (hasManagerOrAdmin()) {
            return service.getCustomOrderById(orderId);
        }
        return service.getCustomOrderForUser(currentUserService.getCurrentUserId(), orderId);
    }

    private boolean hasManagerOrAdmin() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER") || a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean hasAdmin() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
