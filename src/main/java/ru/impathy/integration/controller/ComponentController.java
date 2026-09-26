package ru.impathy.integration.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import ru.impathy.application.ComponentApplicationService;
import ru.impathy.integration.dto.ComponentDto;
import ru.impathy.integration.dto.CreateComponentRequest;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/components")
@Profile("storage")
public class ComponentController {
    private final ComponentApplicationService service;

    public ComponentController(ComponentApplicationService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ComponentDto> getComponents() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ComponentDto getComponent(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    public ComponentDto createComponent(@Valid @RequestBody CreateComponentRequest request) {
        return service.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    public void deleteComponent(@PathVariable UUID id) {
        service.softDelete(id);
    }
}
