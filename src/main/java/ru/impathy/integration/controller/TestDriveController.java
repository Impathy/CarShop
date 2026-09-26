package ru.impathy.integration.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;
import ru.impathy.application.TestDriveApplicationService;
import ru.impathy.integration.dto.CreateTestDriveCarRequest;
import ru.impathy.integration.dto.CreateTestDriveRequest;
import ru.impathy.integration.dto.TestDriveCarDto;
import ru.impathy.integration.dto.TestDriveRequestDto;
import ru.impathy.security.CurrentUserService;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/test-drive")
@Profile("order")
public class TestDriveController {
    private final TestDriveApplicationService service;
    private final CurrentUserService currentUserService;

    public TestDriveController(TestDriveApplicationService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/cars")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public TestDriveCarDto addCar(@Valid @RequestBody CreateTestDriveCarRequest request) {
        return service.addCarToTestDrive(currentUserService.getCurrentUserId(), request.getCarId());
    }

    @DeleteMapping("/cars")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public void removeCar(@Parameter(example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID carId) {
        service.removeCarFromTestDrive(currentUserService.getCurrentUserId(), carId);
    }

    @GetMapping("/cars")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public List<TestDriveCarDto> listCars() {
        return service.listCarsForTestDrive(currentUserService.getCurrentUserId());
    }

    @PostMapping("/requests")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public TestDriveRequestDto createRequest(@Valid @RequestBody CreateTestDriveRequest request) {
        return service.createRequest(currentUserService.getCurrentUserId(), request.getCarId(), request.getStartAt());
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<TestDriveRequestDto> listRequests() {
        return service.listRequests(currentUserService.getCurrentUserId());
    }
}
