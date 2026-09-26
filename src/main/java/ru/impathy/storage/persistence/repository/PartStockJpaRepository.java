package ru.impathy.storage.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.storage.persistence.entity.PartStockJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface PartStockJpaRepository extends JpaRepository<PartStockJpaEntity, UUID> {
    Optional<PartStockJpaEntity> findByCarPartIdAndRemovedFalse(UUID carPartId);
}
