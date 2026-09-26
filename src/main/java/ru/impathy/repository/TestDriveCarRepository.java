package ru.impathy.repository;

import ru.impathy.domain.users.User;

import java.util.List;
import java.util.UUID;

public interface TestDriveCarRepository {
    void createByAdmin(User actor, UUID carId);

    UUID readAnyByAdmin(User actor);

    void updateByAdmin(User actor, UUID oldCarId, UUID newCarId);

    void deleteByAdmin(User actor, UUID carId);

    boolean validateCarReferenceByAdmin(User actor, UUID carId);

    void addByManager(User actor, UUID carId);

    void addByAdmin(User actor, UUID carId);

    void removeByManager(User actor, UUID carId);

    void removeByAdmin(User actor, UUID carId);

    boolean containsForClient(User actor, UUID carId);

    boolean containsForManager(User actor, UUID carId);

    boolean containsForAdmin(User actor, UUID carId);

    List<UUID> findAllForClient(User actor);

    List<UUID> findAllForManager(User actor);

    List<UUID> findAllForAdmin(User actor);
}
