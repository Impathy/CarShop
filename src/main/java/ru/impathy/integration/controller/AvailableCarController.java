package ru.impathy.integration.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.impathy.application.AvailableCarApplicationService;
import ru.impathy.integration.dto.AvailableCarDto;

import java.util.List;
import java.util.UUID;

@RestController
@Profile("order")
@RequestMapping("/api/v1/cars")
public class AvailableCarController {
    private final AvailableCarApplicationService service;

    public AvailableCarController(AvailableCarApplicationService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @Operation(summary = "Get available cars")
    public List<AvailableCarDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @Operation(summary = "Get an available car by id")
    public AvailableCarDto get(@PathVariable UUID id) {
        return service.get(id);
    }
}
