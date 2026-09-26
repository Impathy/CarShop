package ru.impathy.storage.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.storage.persistence.entity.BuildOrderJpaEntity;

import java.util.List;
import java.util.UUID;

public interface BuildOrderJpaRepository extends JpaRepository<BuildOrderJpaEntity, UUID> {
    List<BuildOrderJpaEntity> findAllByRemovedFalseOrderByCreatedAtDesc();
}
