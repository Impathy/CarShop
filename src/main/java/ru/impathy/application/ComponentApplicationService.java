package ru.impathy.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.integration.dto.ComponentDto;
import ru.impathy.integration.dto.CreateComponentRequest;
import ru.impathy.integration.mapper.ComponentMapper;
import ru.impathy.persistence.entity.CarPartJpaEntity;
import ru.impathy.persistence.repository.CarPartJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Profile("storage")
public class ComponentApplicationService {
    private final CarPartJpaRepository repository;
    private final ComponentMapper mapper;

    public ComponentApplicationService(CarPartJpaRepository repository, ComponentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ComponentDto> findAll() {
        List<CarPartJpaEntity> entities = repository.findAll().stream()
                .filter(entity -> !entity.isRemoved())
                .toList();
        return mapper.toDtoList(entities);
    }

    @Transactional(readOnly = true)
    public ComponentDto findById(UUID id) {
        CarPartJpaEntity entity = repository.findById(id)
                .filter(e -> !e.isRemoved())
                .orElseThrow(() -> new EntityNotFoundExeption("Component not found: " + id));
        return mapper.toDto(entity);
    }

    @Transactional
    public ComponentDto create(CreateComponentRequest request) {
        CarPartJpaEntity entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void softDelete(UUID id) {
        CarPartJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundExeption("Component not found: " + id));
        entity.setRemoved(true);
        repository.save(entity);
    }
}
