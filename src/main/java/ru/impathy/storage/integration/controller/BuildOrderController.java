package ru.impathy.storage.integration.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.annotations.Operation;
import ru.impathy.storage.application.BuildOrderApplicationService;
import ru.impathy.storage.integration.dto.BuildOrderDto;
import ru.impathy.storage.integration.dto.CreateBuildOrderRequest;
import ru.impathy.storage.integration.dto.UpdateBuildOrderRequest;
import ru.impathy.security.CurrentUserService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/build-orders")
@Profile("storage")
public class BuildOrderController {
    private final BuildOrderApplicationService service;
    private final CurrentUserService currentUserService;

    public BuildOrderController(BuildOrderApplicationService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    @Operation(summary = "Create a build order")
    public BuildOrderDto create(@Valid @RequestBody CreateBuildOrderRequest request) {
        return service.create(
                request.getSourceOrderId(),
                request.getSourceOrderType(),
                request.getCarId(),
                request.getManagerId(),
                currentUserService.getCurrentUserId(),
                request.getTraceId(),
                request.getRequiredComponentIds()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    @Operation(summary = "List build orders")
    public List<BuildOrderDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    @Operation(summary = "Get a build order by id")
    public BuildOrderDto get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    @Operation(summary = "Update a build order")
    public BuildOrderDto update(@PathVariable UUID id, @Valid @RequestBody UpdateBuildOrderRequest request) {
        return service.update(id, request.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    @Operation(summary = "Delete a build order")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
