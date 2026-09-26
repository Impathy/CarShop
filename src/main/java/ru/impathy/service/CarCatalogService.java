package ru.impathy.service;

import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.domain.users.AccessLevel;
import ru.impathy.domain.users.User;
import ru.impathy.repository.CarRepository;
import ru.impathy.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarCatalogService {
    private CarRepository carRepository;
    private UserRepository userRepository;

    public CarCatalogService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public Car addCar(UUID actorId, Car car) {
        requireId(actorId, "actorId");
        validateCar(car);

        User actor = getActor(actorId);
        if (actor.getAccessLevel() == AccessLevel.SKLAD_ADMIN) {
            return carRepository.saveBySkladAdmin(actor, car);
        }
        if (actor.getAccessLevel() == AccessLevel.ADMIN) {
            if (!carRepository.validateCarByAdmin(actor, car)) {
                throw new DomainValidationException("Invalid car characteristics");
            }
            return carRepository.createByAdmin(actor, car);
        }
        throw new DomainValidationException("User cannot add car: " + actorId);
    }

    public Car getCar(UUID actorId, UUID id) {
        requireId(actorId, "actorId");
        requireId(id, "carId");

        User actor = getActor(actorId);
        if (actor.getAccessLevel() == AccessLevel.CLIENT) {
            return carRepository.findByIdForClient(actor, id).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
        }
        if (actor.getAccessLevel() == AccessLevel.MANAGER) {
            return carRepository.findByIdForManager(actor, id).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
        }
        if (actor.getAccessLevel() == AccessLevel.SKLAD_ADMIN) {
            return carRepository.findByIdForSkladAdmin(actor, id).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
        }
        if (actor.getAccessLevel() == AccessLevel.ADMIN) {
            return carRepository.readByIdByAdmin(actor, id).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
        }
        throw new DomainValidationException("User cannot read cars: " + actorId);
    }

    public List<Car> listAvailableCars(UUID actorId) {
        requireId(actorId, "actorId");

        User actor = getActor(actorId);
        if (actor.getAccessLevel() == AccessLevel.CLIENT) {
            return carRepository.findAllForClient(actor);
        }
        if (actor.getAccessLevel() == AccessLevel.MANAGER) {
            return carRepository.findAllForManager(actor);
        }
        if (actor.getAccessLevel() == AccessLevel.SKLAD_ADMIN) {
            return carRepository.findAllForSkladAdmin(actor);
        }
        if (actor.getAccessLevel() == AccessLevel.ADMIN) {
            return carRepository.findAllForAdmin(actor);
        }
        throw new DomainValidationException("User cannot list cars: " + actorId);
    }

    public void deleteCar(UUID actorId, UUID id) {
        requireId(actorId, "actorId");
        requireId(id, "carId");

        User actor = getActor(actorId);
        if (actor.getAccessLevel() == AccessLevel.SKLAD_ADMIN) {
            carRepository.findByIdForSkladAdmin(actor, id).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
            carRepository.deleteByIdBySkladAdmin(actor, id);
            return;
        }
        if (actor.getAccessLevel() == AccessLevel.ADMIN) {
            carRepository.readByIdByAdmin(actor, id).orElseThrow(() -> new EntityNotFoundExeption("Car not found: " + id));
            carRepository.deleteByIdByAdmin(actor, id);
            return;
        }
        throw new DomainValidationException("User cannot delete cars: " + actorId);
    }

    public List<Car> filterCars(UUID actorId, CarFilter filter) {
        requireId(actorId, "actorId");
        if (filter == null) {
            throw new DomainValidationException("Car filter is required");
        }
        validateFilter(filter);

        return listAvailableCars(actorId).stream()
                .filter(car -> matchesFilter(car, filter))
                .collect(Collectors.toList());
    }

    private User getActor(UUID actorId) {
        return userRepository.findById(actorId).orElseThrow(() -> new EntityNotFoundExeption("User not found: " + actorId));
    }

    private void requireId(UUID id, String fieldName) {
        if (id == null) {
            throw new DomainValidationException(fieldName + " is required");
        }
    }

    private void validateCar(Car car) {
        if (car == null) {
            throw new DomainValidationException("Car is required");
        }
        if (car.getBrand() == null || car.getBrand().isBlank()) {
            throw new DomainValidationException("Car brand is required");
        }
        if (car.getModelName() == null || car.getModelName().isBlank()) {
            throw new DomainValidationException("Car modelName is required");
        }
        if (car.getBasePrice() < 0 || car.getExtraPrice() < 0) {
            throw new DomainValidationException("Car price cannot be negative");
        }
    }

    private void validateFilter(CarFilter filter) {
        if (filter.getMinPrice() != null && filter.getMinPrice() < 0) {
            throw new DomainValidationException("minPrice cannot be negative");
        }
        if (filter.getMaxPrice() != null && filter.getMaxPrice() < 0) {
            throw new DomainValidationException("maxPrice cannot be negative");
        }
        if (filter.getMinPrice() != null && filter.getMaxPrice() != null && filter.getMinPrice() > filter.getMaxPrice()) {
            throw new DomainValidationException("minPrice cannot be greater than maxPrice");
        }
        if (filter.getMinEnginePowerHP() != null && filter.getMinEnginePowerHP() < 0) {
            throw new DomainValidationException("minEnginePowerHP cannot be negative");
        }
        if (filter.getMaxEnginePowerHP() != null && filter.getMaxEnginePowerHP() < 0) {
            throw new DomainValidationException("maxEnginePowerHP cannot be negative");
        }
        if (filter.getMinEnginePowerHP() != null && filter.getMaxEnginePowerHP() != null
                && filter.getMinEnginePowerHP() > filter.getMaxEnginePowerHP()) {
            throw new DomainValidationException("minEnginePowerHP cannot be greater than maxEnginePowerHP");
        }
        if (filter.getMinEngineVolume() != null && filter.getMinEngineVolume() < 0) {
            throw new DomainValidationException("minEngineVolume cannot be negative");
        }
        if (filter.getMaxEngineVolume() != null && filter.getMaxEngineVolume() < 0) {
            throw new DomainValidationException("maxEngineVolume cannot be negative");
        }
        if (filter.getMinEngineVolume() != null && filter.getMaxEngineVolume() != null
                && filter.getMinEngineVolume() > filter.getMaxEngineVolume()) {
            throw new DomainValidationException("minEngineVolume cannot be greater than maxEngineVolume");
        }
    }

    private boolean matchesFilter(Car car, CarFilter filter) {
        if (filter.getMinPrice() != null && car.getPrice() < filter.getMinPrice()) {
            return false;
        }
        if (filter.getMaxPrice() != null && car.getPrice() > filter.getMaxPrice()) {
            return false;
        }
        if (filter.getBrand() != null && !filter.getBrand().equals(car.getBrand())) {
            return false;
        }
        if (filter.getModelName() != null && !filter.getModelName().equals(car.getModelName())) {
            return false;
        }
        if (filter.getBodyType() != null && filter.getBodyType() != car.getBodyType()) {
            return false;
        }
        if (filter.getFuelType() != null && filter.getFuelType() != car.getFuelType()) {
            return false;
        }
        if (filter.getMinEnginePowerHP() != null && car.getEnginePowerHP() < filter.getMinEnginePowerHP()) {
            return false;
        }
        if (filter.getMaxEnginePowerHP() != null && car.getEnginePowerHP() > filter.getMaxEnginePowerHP()) {
            return false;
        }
        if (filter.getMinEngineVolume() != null && car.getEngineVolume() < filter.getMinEngineVolume()) {
            return false;
        }
        if (filter.getMaxEngineVolume() != null && car.getEngineVolume() > filter.getMaxEngineVolume()) {
            return false;
        }
        if (filter.getGearboxType() != null && filter.getGearboxType() != car.getGearboxType()) {
            return false;
        }
        if (filter.getDriveType() != null && filter.getDriveType() != car.getDriveType()) {
            return false;
        }
        if (filter.getColor() != null && !filter.getColor().equals(car.getColor())) {
            return false;
        }
        return true;
    }
}
