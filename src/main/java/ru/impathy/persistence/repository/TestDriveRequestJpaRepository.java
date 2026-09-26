package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.TestDriveRequestJpaEntity;

import java.util.List;
import java.util.UUID;

public interface TestDriveRequestJpaRepository extends JpaRepository<TestDriveRequestJpaEntity, UUID> {
    List<TestDriveRequestJpaEntity> findAllByRemovedFalse();
}
