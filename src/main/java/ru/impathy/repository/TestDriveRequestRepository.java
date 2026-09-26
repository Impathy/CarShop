package ru.impathy.repository;

import ru.impathy.domain.entities.testDrive.TestDriveRequest;
import ru.impathy.domain.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestDriveRequestRepository {
    TestDriveRequest createByAdmin(User actor, TestDriveRequest request);

    Optional<TestDriveRequest> readByIdByAdmin(User actor, UUID id);

    TestDriveRequest updateByAdmin(User actor, UUID id, TestDriveRequest request);

    void deleteByIdByAdmin(User actor, UUID id);

    boolean validateRequestByAdmin(User actor, TestDriveRequest request);

    TestDriveRequest saveByClient(User actor, TestDriveRequest request);

    List<TestDriveRequest> findAllForManager(User actor);

    List<TestDriveRequest> findAllForAdmin(User actor);
}
