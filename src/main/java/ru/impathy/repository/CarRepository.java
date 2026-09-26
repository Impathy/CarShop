package ru.impathy.repository;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    Car createByAdmin(User actor, Car car);

    Optional<Car> readByIdByAdmin(User actor, UUID id);

    Car updateByAdmin(User actor, UUID id, Car car);

    void deleteByIdByAdmin(User actor, UUID id);

    boolean validateCarByAdmin(User actor, Car car);

    Car saveBySkladAdmin(User actor, Car car);

    Car saveByAdmin(User actor, Car car);

    Optional<Car> findByIdForClient(User actor, UUID id);

    Optional<Car> findByIdForManager(User actor, UUID id);

    Optional<Car> findByIdForSkladAdmin(User actor, UUID id);

    Optional<Car> findByIdForAdmin(User actor, UUID id);

    List<Car> findAllForClient(User actor);

    List<Car> findAllForManager(User actor);

    List<Car> findAllForSkladAdmin(User actor);

    List<Car> findAllForAdmin(User actor);

    void deleteByIdBySkladAdmin(User actor, UUID id);
}
