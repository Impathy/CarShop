package ru.impathy.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.impathy.domain.users.AccessLevel;
import ru.impathy.persistence.entity.UserJpaEntity;

import java.util.List;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    List<UserJpaEntity> findAllByAccessLevelAndRemovedFalse(AccessLevel accessLevel);
}
