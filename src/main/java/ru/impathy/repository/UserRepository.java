package ru.impathy.repository;

import ru.impathy.domain.users.AccessLevel;
import ru.impathy.domain.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User createByAdmin(User actor, User user);

    Optional<User> readByIdByAdmin(User actor, UUID id);

    User updateByAdmin(User actor, UUID id, User user);

    void deleteByIdByAdmin(User actor, UUID id);

    boolean validateUserByAdmin(User actor, User user);

    User save(User user);

    Optional<User> findById(UUID id);

    List<User> findByAccessLevel(AccessLevel accessLevel);

    List<User> findAll();
}
