package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.persistence.entity.ProcessedMessageJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProcessedMessageJpaRepository extends JpaRepository<ProcessedMessageJpaEntity, UUID> {
    Optional<ProcessedMessageJpaEntity> findByMessageId(String messageId);
}
