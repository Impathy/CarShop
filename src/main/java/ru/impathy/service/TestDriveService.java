package ru.impathy.service;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.entities.testDrive.TestDriveRequest;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.domain.users.AccessLevel;
import ru.impathy.domain.users.User;
import ru.impathy.repository.CarRepository;
import ru.impathy.repository.TestDriveCarRepository;
import ru.impathy.repository.TestDriveRequestRepository;
import ru.impathy.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestDriveService {
    private TestDriveRequestRepository requestRepository;
    private TestDriveCarRepository testDriveCarRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;

    public TestDriveService(TestDriveRequestRepository requestRepository,
                            TestDriveCarRepository testDriveCarRepository,
                            UserRepository userRepository,
                            CarRepository carRepository) {
        this.requestRepository = requestRepository;
        this.testDriveCarRepository = testDriveCarRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public void addCarToTestDrive(UUID managerId, UUID carId) {
        requireId(managerId, "managerId");
        requireId(carId, "carId");

        User manager = getManager(managerId);

        carRepository.findByIdForManager(manager, carId).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
        if (testDriveCarRepository.containsForManager(manager, carId)) {
            throw new DomainValidationException("Car already in test drive list: " + carId);
        }
        testDriveCarRepository.addByManager(manager, carId);
    }

    public void removeCarFromTestDrive(UUID managerId, UUID carId) {
        requireId(managerId, "managerId");
        requireId(carId, "carId");

        User manager = getManager(managerId);

        if (!testDriveCarRepository.containsForManager(manager, carId)) {
            throw new EntityNotFoundExeption("Car is not in test drive list: " + carId);
        }
        testDriveCarRepository.removeByManager(manager, carId);
    }

    public List<Car> listCarsForTestDrive(UUID actorId) {
        requireId(actorId, "actorId");
        User actor = getActor(actorId);

        List<UUID> carIds;
        if (actor.getAccessLevel() == AccessLevel.CLIENT) {
            carIds = testDriveCarRepository.findAllForClient(actor);
        } else if (actor.getAccessLevel() == AccessLevel.MANAGER) {
            carIds = testDriveCarRepository.findAllForManager(actor);
        } else if (actor.getAccessLevel() == AccessLevel.ADMIN) {
            carIds = testDriveCarRepository.findAllForAdmin(actor);
        } else {
            throw new DomainValidationException("User cannot view test drive cars: " + actorId);
        }

        return carIds.stream()
                .map(carId -> {
                    if (actor.getAccessLevel() == AccessLevel.CLIENT) {
                        return carRepository.findByIdForClient(actor, carId).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
                    }
                    if (actor.getAccessLevel() == AccessLevel.MANAGER) {
                        return carRepository.findByIdForManager(actor, carId).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
                    }
                    return carRepository.findByIdForAdmin(actor, carId).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
                })
                .collect(Collectors.toList());
    }

    public TestDriveRequest createRequest(UUID clientId, UUID carId, LocalDateTime startAt) {
        requireId(clientId, "clientId");
        requireId(carId, "carId");
        if (startAt == null) {
            throw new DomainValidationException("Test drive startAt is required");
        }
        if (!startAt.isAfter(LocalDateTime.now())) {
            throw new DomainValidationException("Test drive startAt must be in the future");
        }

        User client = getClient(clientId);

        Car car = carRepository.findByIdForClient(client, carId).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
        if (!testDriveCarRepository.containsForClient(client, carId)) {
            throw new DomainValidationException("Car is not available for test-drive: " + carId);
        }

        TestDriveRequest request = new TestDriveRequest(UUID.randomUUID(), client, car, startAt);
        return requestRepository.saveByClient(client, request);
    }

    public List<TestDriveRequest> listRequests(UUID managerId) {
        requireId(managerId, "managerId");
        User manager = getManager(managerId);
        return requestRepository.findAllForManager(manager);
    }

    private void requireId(UUID id, String fieldName) {
        if (id == null) {
            throw new DomainValidationException(fieldName + " is required");
        }
    }

    private User getActor(UUID actorId) {
        return userRepository.findById(actorId).orElseThrow(() -> new EntityNotFoundExeption("User not found: " + actorId));
    }

    private User getClient(UUID clientId) {
        User client = getActor(clientId);
        if (client.getAccessLevel() != AccessLevel.CLIENT) {
            throw new DomainValidationException("User is not client: " + clientId);
        }
        return client;
    }

    private User getManager(UUID managerId) {
        User manager = getActor(managerId);
        if (manager.getAccessLevel() != AccessLevel.MANAGER) {
            throw new DomainValidationException("User is not manager: " + managerId);
        }
        return manager;
    }
}
