package ru.impathy.integration.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import ru.impathy.application.CarQueryApplicationService;
import ru.impathy.domain.entities.carParts.ComponentType;
import ru.impathy.integration.dto.CarDto;
import ru.impathy.integration.dto.CreateCarRequest;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/cars")
@Profile("storage")
public class CarController {
    private final CarQueryApplicationService carService;

    public CarController(CarQueryApplicationService carService) {
        this.carService = carService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CarDto> getCars(@RequestParam(required = false) String brand,
                                @RequestParam(required = false) List<ComponentType> componentTypes) {
        return carService.findAll(brand, componentTypes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CarDto getCar(@PathVariable UUID id) {
        return carService.findById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    public CarDto createCar(@Valid @RequestBody CreateCarRequest request) {
        return carService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN','ADMIN')")
    public void deleteCar(@PathVariable UUID id) {
        carService.softDelete(id);
    }
}
