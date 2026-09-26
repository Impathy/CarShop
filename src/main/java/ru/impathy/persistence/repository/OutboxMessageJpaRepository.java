package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.OutboxMessageJpaEntity;

import java.util.List;
import java.util.UUID;

public interface OutboxMessageJpaRepository extends JpaRepository<OutboxMessageJpaEntity, UUID> {
    List<OutboxMessageJpaEntity> findAllByPublishedFalseAndRemovedFalseOrderByCreatedAtAsc();
}
