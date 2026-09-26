package ru.impathy.storage.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.domain.users.AccessLevel;
import ru.impathy.storage.domain.BuildOrderStatus;
import ru.impathy.storage.integration.dto.BuildOrderDto;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.entity.CarPartJpaEntity;
import ru.impathy.persistence.entity.UserJpaEntity;
import ru.impathy.storage.persistence.entity.BuildOrderJpaEntity;
import ru.impathy.storage.persistence.repository.BuildOrderJpaRepository;
import ru.impathy.persistence.repository.CarJpaRepository;
import ru.impathy.persistence.repository.CarPartJpaRepository;
import ru.impathy.persistence.repository.UserJpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Profile("storage")
public class BuildOrderApplicationService {
    private final BuildOrderJpaRepository repository;
    private final CarJpaRepository carRepository;
    private final CarPartJpaRepository carPartRepository;
    private final UserJpaRepository userRepository;

    public BuildOrderApplicationService(BuildOrderJpaRepository repository,
                                        CarJpaRepository carRepository,
                                        CarPartJpaRepository carPartRepository,
                                        UserJpaRepository userRepository) {
        this.repository = repository;
        this.carRepository = carRepository;
        this.carPartRepository = carPartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BuildOrderDto create(UUID sourceOrderId,
                                String sourceOrderType,
                                UUID carId,
                                UUID managerId,
                                UUID warehouseEmployeeId,
                                String traceId,
                                Set<UUID> requiredComponentIds) {
        validateRequired(sourceOrderId, "sourceOrderId");
        validateRequired(sourceOrderType, "sourceOrderType");
        validateRequired(warehouseEmployeeId, "warehouseEmployeeId");
        validateRequired(traceId, "traceId");
        validateCar(carId);
        validateManager(managerId);
        validateComponents(requiredComponentIds);

        BuildOrderJpaEntity entity = new BuildOrderJpaEntity();
        entity.setSourceOrderId(sourceOrderId);
        entity.setSourceOrderType(sourceOrderType);
        entity.setCarId(carId);
        entity.setManagerId(managerId);
        entity.setWarehouseEmployeeId(warehouseEmployeeId);
        entity.setTraceId(traceId);
        entity.setStatus(BuildOrderStatus.CREATED.name());
        if (requiredComponentIds != null) {
            entity.setRequiredComponentIds(requiredComponentIds);
        }
        return toDto(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<BuildOrderDto> list() {
        return repository.findAllByRemovedFalseOrderByCreatedAtDesc().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BuildOrderDto get(UUID id) {
        return toDto(find(id));
    }

    @Transactional
    public BuildOrderDto update(UUID id, String status) {
        BuildOrderJpaEntity entity = find(id);
        validateStatus(status);
        entity.setStatus(status);
        return toDto(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        BuildOrderJpaEntity entity = find(id);
        entity.setRemoved(true);
        repository.save(entity);
    }

    @Transactional
    public BuildOrderDto markAssembled(UUID sourceOrderId) {
        BuildOrderJpaEntity entity = repository.findAll().stream()
                .filter(item -> !item.isRemoved() && sourceOrderId.equals(item.getSourceOrderId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Build order not found: " + sourceOrderId));
        entity.setStatus(BuildOrderStatus.ASSEMBLED.name());
        return toDto(repository.save(entity));
    }

    @Transactional
    public BuildOrderDto markFailed(UUID sourceOrderId) {
        BuildOrderJpaEntity entity = repository.findAll().stream()
                .filter(item -> !item.isRemoved() && sourceOrderId.equals(item.getSourceOrderId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Build order not found: " + sourceOrderId));
        entity.setStatus(BuildOrderStatus.FAIL.name());
        return toDto(repository.save(entity));
    }

    private BuildOrderJpaEntity find(UUID id) {
        validateRequired(id, "id");
        return repository.findById(id)
                .filter(item -> !item.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("Build order not found: " + id));
    }

    private void validateRequired(Object value, String name) {
        if (value == null || value instanceof String stringValue && stringValue.isBlank()) {
            throw new DomainValidationException(name + " is required");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new DomainValidationException("status is required");
        }
    }

    private void validateCar(UUID carId) {
        if (carId == null) {
            return;
        }
        carRepository.findByIdAndRemovedFalse(carId)
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + carId));
    }

    private void validateManager(UUID managerId) {
        if (managerId == null) {
            return;
        }
        UserJpaEntity manager = userRepository.findById(managerId)
                .filter(user -> !user.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("User not found: " + managerId));
        if (manager.getAccessLevel() != AccessLevel.MANAGER && manager.getAccessLevel() != AccessLevel.ADMIN) {
            throw new DomainValidationException("managerId must reference a manager");
        }
    }

    private void validateComponents(Set<UUID> requiredComponentIds) {
        if (requiredComponentIds == null) {
            return;
        }
        for (UUID componentId : requiredComponentIds) {
            if (componentId == null) {
                throw new DomainValidationException("requiredComponentIds must not contain null values");
            }
            carPartRepository.findById(componentId)
                    .filter(item -> !item.isRemoved())
                    .orElseThrow(() -> new EntityNotFoundExeption("Component not found: " + componentId));
        }
    }

    private BuildOrderDto toDto(BuildOrderJpaEntity entity) {
        BuildOrderDto dto = new BuildOrderDto();
        dto.setId(entity.getId());
        dto.setSourceOrderId(entity.getSourceOrderId());
        dto.setSourceOrderType(entity.getSourceOrderType());
        dto.setCarId(entity.getCarId());
        dto.setManagerId(entity.getManagerId());
        dto.setWarehouseEmployeeId(entity.getWarehouseEmployeeId());
        dto.setTraceId(entity.getTraceId());
        dto.setStatus(entity.getStatus());
        dto.setRequiredComponentIds(entity.getRequiredComponentIds());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setRemoved(entity.isRemoved());
        return dto;
    }
}
