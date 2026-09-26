package ru.impathy.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.integration.dto.TestDriveCarDto;
import ru.impathy.integration.dto.TestDriveRequestDto;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.entity.TestDriveCarJpaEntity;
import ru.impathy.persistence.entity.TestDriveRequestJpaEntity;
import ru.impathy.persistence.entity.UserJpaEntity;
import ru.impathy.persistence.repository.CarJpaRepository;
import ru.impathy.persistence.repository.TestDriveCarJpaRepository;
import ru.impathy.persistence.repository.TestDriveRequestJpaRepository;
import ru.impathy.persistence.repository.UserJpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Profile("order")
public class TestDriveApplicationService {
    private final TestDriveRequestJpaRepository requestRepository;
    private final TestDriveCarJpaRepository testDriveCarRepository;
    private final UserJpaRepository userRepository;
    private final CarJpaRepository carRepository;

    public TestDriveApplicationService(TestDriveRequestJpaRepository requestRepository,
                                       TestDriveCarJpaRepository testDriveCarRepository,
                                       UserJpaRepository userRepository,
                                       CarJpaRepository carRepository) {
        this.requestRepository = requestRepository;
        this.testDriveCarRepository = testDriveCarRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    public TestDriveCarDto addCarToTestDrive(UUID managerId, UUID carId) {
        getManager(managerId);
        CarJpaEntity car = carRepository.findByIdAndRemovedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
        if (testDriveCarRepository.findByCar_IdAndRemovedFalse(carId).isPresent()) {
            throw new DomainValidationException("Car already in test drive list: " + carId);
        }
        TestDriveCarJpaEntity entity = new TestDriveCarJpaEntity();
        entity.setCar(car);
        return toDto(testDriveCarRepository.save(entity));
    }

    @Transactional
    public void removeCarFromTestDrive(UUID managerId, UUID carId) {
        getManager(managerId);
        TestDriveCarJpaEntity entity = testDriveCarRepository.findByCar_IdAndRemovedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundExeption("Car is not in test drive list: " + carId));
        entity.setRemoved(true);
        testDriveCarRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<TestDriveCarDto> listCarsForTestDrive(UUID actorId) {
        getActor(actorId);
        return testDriveCarRepository.findAllByRemovedFalse().stream().map(this::toDto).toList();
    }

    @Transactional
    public TestDriveRequestDto createRequest(UUID clientId, UUID carId, Instant startAt) {
        if (startAt == null) {
            throw new DomainValidationException("Test drive startAt is required");
        }
        if (!startAt.isAfter(Instant.now())) {
            throw new DomainValidationException("Test drive startAt must be in the future");
        }

        getClient(clientId);
        CarJpaEntity car = carRepository.findByIdAndRemovedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
        if (testDriveCarRepository.findByCar_IdAndRemovedFalse(carId).isEmpty()) {
            throw new DomainValidationException("Car is not available for test-drive: " + carId);
        }

        TestDriveRequestJpaEntity request = new TestDriveRequestJpaEntity();
        request.setClient(getActor(clientId));
        request.setCar(car);
        request.setStartAt(startAt);

        return toDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<TestDriveRequestDto> listRequests(UUID managerId) {
        getManager(managerId);
        return requestRepository.findAllByRemovedFalse().stream().map(this::toDto).toList();
    }

    private UserJpaEntity getActor(UUID actorId) {
        requireId(actorId, "actorId");
        return userRepository.findById(actorId)
                .filter(user -> !user.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("User not found: " + actorId));
    }

    private UserJpaEntity getClient(UUID clientId) {
        return getActor(clientId);
    }

    private UserJpaEntity getManager(UUID managerId) {
        return getActor(managerId);
    }

    private void requireId(UUID id, String fieldName) {
        if (id == null) {
            throw new DomainValidationException(fieldName + " is required");
        }
    }

    private TestDriveCarDto toDto(TestDriveCarJpaEntity entity) {
        TestDriveCarDto dto = new TestDriveCarDto();
        dto.setId(entity.getId());
        dto.setCarId(entity.getCar().getId());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private TestDriveRequestDto toDto(TestDriveRequestJpaEntity entity) {
        TestDriveRequestDto dto = new TestDriveRequestDto();
        dto.setId(entity.getId());
        dto.setClientId(entity.getClient().getId());
        dto.setCarId(entity.getCar().getId());
        dto.setStartAt(entity.getStartAt());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
