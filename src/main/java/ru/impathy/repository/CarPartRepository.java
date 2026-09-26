package ru.impathy.repository;

import ru.impathy.domain.entities.carParts.CarPart;
import ru.impathy.domain.entities.carParts.ComponentType;
import ru.impathy.domain.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarPartRepository {
    CarPart createByAdmin(User actor, UUID id, CarPart carPart);

    Optional<CarPart> readByIdByAdmin(User actor, UUID id);

    CarPart updateByAdmin(User actor, UUID id, CarPart carPart);

    void deleteByIdByAdmin(User actor, UUID id);

    boolean validateCarPartByAdmin(User actor, CarPart carPart);

    CarPart saveBySkladAdmin(User actor, CarPart carPart);

    CarPart saveByAdmin(User actor, CarPart carPart);

    Optional<CarPart> findByIdForSkladAdmin(User actor, UUID id);

    Optional<CarPart> findByIdForAdmin(User actor, UUID id);

    Optional<CarPart> findByIdForManager(User actor, UUID id);

    Optional<CarPart> findByIdForClient(User actor, UUID id);

    List<CarPart> findAllForSkladAdmin(User actor);

    List<CarPart> findAllForAdmin(User actor);

    List<CarPart> findAllForManager(User actor);

    List<CarPart> findAllForClient(User actor);

    List<CarPart> findByComponentTypeForSkladAdmin(User actor, ComponentType componentType);

    List<CarPart> findByCompatibleModelForSkladAdmin(User actor, String brand, String modelName);

    void deleteByIdBySkladAdmin(User actor, UUID id);
}
