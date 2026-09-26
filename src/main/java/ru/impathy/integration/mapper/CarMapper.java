package ru.impathy.integration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.impathy.integration.dto.CarDto;
import ru.impathy.integration.dto.ComponentDto;
import ru.impathy.integration.dto.CreateCarRequest;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.entity.CarPartJpaEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "components", source = "baseParts")
    CarDto toDto(CarJpaEntity entity);

    List<CarDto> toDtoList(List<CarJpaEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removed", ignore = true)
    @Mapping(target = "baseParts", ignore = true)
    CarJpaEntity toEntity(CreateCarRequest request);

    ComponentDto toComponentDto(CarPartJpaEntity entity);
}
