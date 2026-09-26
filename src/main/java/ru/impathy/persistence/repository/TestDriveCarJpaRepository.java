package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.TestDriveCarJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestDriveCarJpaRepository extends JpaRepository<TestDriveCarJpaEntity, UUID> {
    Optional<TestDriveCarJpaEntity> findByCar_IdAndRemovedFalse(UUID carId);

    List<TestDriveCarJpaEntity> findAllByRemovedFalse();
}
