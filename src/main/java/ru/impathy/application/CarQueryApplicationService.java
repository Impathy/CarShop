package ru.impathy.application;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.domain.entities.carParts.ComponentType;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.integration.dto.CarDto;
import ru.impathy.integration.dto.CreateCarRequest;
import ru.impathy.integration.mapper.CarMapper;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.entity.CarPartJpaEntity;
import ru.impathy.persistence.repository.CarJpaRepository;
import ru.impathy.persistence.repository.CarPartJpaRepository;
import ru.impathy.persistence.specification.CarSpecification;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Profile("storage")
public class CarQueryApplicationService {
    private final CarJpaRepository carJpaRepository;
    private final CarPartJpaRepository carPartJpaRepository;
    private final CarMapper carMapper;

    public CarQueryApplicationService(CarJpaRepository carJpaRepository,
                                      CarPartJpaRepository carPartJpaRepository,
                                      CarMapper carMapper) {
        this.carJpaRepository = carJpaRepository;
        this.carPartJpaRepository = carPartJpaRepository;
        this.carMapper = carMapper;
    }

    @Transactional(readOnly = true)
    public List<CarDto> findAll(String brand, List<ComponentType> componentTypes) {
        Specification<CarJpaEntity> specification = CarSpecification.notRemoved();

        if (brand != null && !brand.isBlank()) {
            specification = specification.and(CarSpecification.brandEquals(brand));
        }
        if (componentTypes != null && !componentTypes.isEmpty()) {
            specification = specification.and(CarSpecification.hasAnyComponentType(componentTypes));
        }

        return carMapper.toDtoList(carJpaRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public CarDto findById(UUID id) {
        CarJpaEntity car = carJpaRepository.findById(id)
                .filter(entity -> !entity.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
        return carMapper.toDto(car);
    }

    @Transactional
    public CarDto create(CreateCarRequest request) {
        CarJpaEntity entity = carMapper.toEntity(request);

        if (request.getBasePartIds() != null && !request.getBasePartIds().isEmpty()) {
            List<CarPartJpaEntity> parts = carPartJpaRepository.findAllById(request.getBasePartIds());
            Set<UUID> foundIds = parts.stream().map(CarPartJpaEntity::getId).collect(Collectors.toSet());
            for (UUID id : request.getBasePartIds()) {
                if (!foundIds.contains(id)) {
                    throw new EntityNotFoundExeption("Car part not found: " + id);
                }
            }
            entity.setBaseParts(parts.stream().collect(Collectors.toSet()));
        }

        return carMapper.toDto(carJpaRepository.save(entity));
    }

    @Transactional
    public void softDelete(UUID id) {
        CarJpaEntity car = carJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
        car.setRemoved(true);
        carJpaRepository.save(car);
    }
}
