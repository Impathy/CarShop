package ru.impathy.storage.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.storage.persistence.entity.CarStockJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarStockJpaRepository extends JpaRepository<CarStockJpaEntity, UUID> {
    Optional<CarStockJpaEntity> findByCarIdAndRemovedFalse(UUID carId);
    List<CarStockJpaEntity> findAllByRemovedFalseOrderByCreatedAtDesc();
}
