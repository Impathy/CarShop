package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.impathy.persistence.entity.CarJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID>, JpaSpecificationExecutor<CarJpaEntity> {
    Optional<CarJpaEntity> findByIdAndRemovedFalse(UUID id);
}
