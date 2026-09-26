package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.CarPartJpaEntity;

import java.util.UUID;

public interface CarPartJpaRepository extends JpaRepository<CarPartJpaEntity, UUID> {
}
