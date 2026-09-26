package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.CustomCarOrderJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomCarOrderJpaRepository extends JpaRepository<CustomCarOrderJpaEntity, UUID> {
    Optional<CustomCarOrderJpaEntity> findByIdAndManager_IdAndRemovedFalse(UUID id, UUID managerId);

    List<CustomCarOrderJpaEntity> findAllByManager_IdAndRemovedFalse(UUID managerId);

    List<CustomCarOrderJpaEntity> findAllByClient_IdAndRemovedFalse(UUID clientId);

    List<CustomCarOrderJpaEntity> findAllByRemovedFalse();

    Optional<CustomCarOrderJpaEntity> findByIdAndClient_IdAndRemovedFalse(UUID id, UUID clientId);

    Optional<CustomCarOrderJpaEntity> findByIdAndRemovedFalse(UUID id);
}
