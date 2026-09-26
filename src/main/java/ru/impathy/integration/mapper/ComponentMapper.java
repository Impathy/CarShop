package ru.impathy.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.impathy.integration.dto.ComponentDto;
import ru.impathy.integration.dto.CreateComponentRequest;
import ru.impathy.persistence.entity.CarPartJpaEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComponentMapper {
    ComponentDto toDto(CarPartJpaEntity entity);

    List<ComponentDto> toDtoList(List<CarPartJpaEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    CarPartJpaEntity toEntity(CreateComponentRequest request);
}
