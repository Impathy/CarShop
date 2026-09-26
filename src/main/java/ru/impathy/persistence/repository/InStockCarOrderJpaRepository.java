package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.InStockCarOrderJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InStockCarOrderJpaRepository extends JpaRepository<InStockCarOrderJpaEntity, UUID> {
    Optional<InStockCarOrderJpaEntity> findByIdAndManager_IdAndRemovedFalse(UUID id, UUID managerId);

    List<InStockCarOrderJpaEntity> findAllByManager_IdAndRemovedFalse(UUID managerId);

    List<InStockCarOrderJpaEntity> findAllByClient_IdAndRemovedFalse(UUID clientId);

    List<InStockCarOrderJpaEntity> findAllByRemovedFalse();

    Optional<InStockCarOrderJpaEntity> findByIdAndClient_IdAndRemovedFalse(UUID id, UUID clientId);

    Optional<InStockCarOrderJpaEntity> findByIdAndRemovedFalse(UUID id);
}
