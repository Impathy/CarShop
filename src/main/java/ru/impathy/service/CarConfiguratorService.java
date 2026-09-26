package ru.impathy.service;

import ru.impathy.domain.entities.carParts.CarPart;
import ru.impathy.domain.entities.carParts.ComponentType;
import ru.impathy.domain.entities.cars.Car;
import ru.impathy.domain.exeptions.DomainValidationException;
import ru.impathy.domain.exeptions.IncompatibleComponentExeption;

import java.util.HashMap;
import java.util.Map;

public class CarConfiguratorService {

    public Car configure(Car baseCar, Map<ComponentType, CarPart> selectedParts) {
        validateBaseCar(baseCar);
        validateRequiredComponents(selectedParts);
        validateCompatibility(baseCar, selectedParts);

        Car configuredCar = copyBaseCar(baseCar);
        configuredCar.setCarParts(new HashMap<>(selectedParts));

        int extraPrice = selectedParts.values().stream()
                .mapToInt(CarPart::getExtraPrice)
                .sum();
        configuredCar.setExtraPrice(extraPrice);

        return configuredCar;
    }

    private void validateBaseCar(Car baseCar) {
        if (baseCar == null) {
            throw new DomainValidationException("Base car is required");
        }
        if (baseCar.getBrand() == null || baseCar.getBrand().isBlank()) {
            throw new DomainValidationException("Base car brand is required");
        }
        if (baseCar.getModelName() == null || baseCar.getModelName().isBlank()) {
            throw new DomainValidationException("Base car modelName is required");
        }
        if (baseCar.getBasePrice() < 0) {
            throw new DomainValidationException("Base car price cannot be negative");
        }
    }

    private void validateRequiredComponents(Map<ComponentType, CarPart> selectedParts) {
        if (selectedParts == null) {
            throw new DomainValidationException("Selected components are required");
        }
        for (ComponentType componentType : ComponentType.values()) {
            if (!selectedParts.containsKey(componentType)) {
                throw new DomainValidationException("Missing required component: " + componentType);
            }
            CarPart part = selectedParts.get(componentType);
            if (part == null) {
                throw new DomainValidationException("Component value is required: " + componentType);
            }
            if (part.getType() != componentType) {
                throw new DomainValidationException("Component type mismatch: " + componentType);
            }
        }
    }

    private void validateCompatibility(Car car, Map<ComponentType, CarPart> selectedParts) {
        CarPart incompatiblePart = selectedParts.values().stream()
                .filter(part -> !part.isCompatibleWith(car.getBrand(), car.getModelName()))
                .findFirst()
                .orElse(null);

        if (incompatiblePart != null) {
            throw new IncompatibleComponentExeption("Component is not compatible: " + incompatiblePart.getName());
        }
    }

    private Car copyBaseCar(Car baseCar) {
        Car car = new Car();
        car.setId(baseCar.getId());
        car.setBrand(baseCar.getBrand());
        car.setModelName(baseCar.getModelName());
        car.setBodyType(baseCar.getBodyType());
        car.setFuelType(baseCar.getFuelType());
        car.setGearboxType(baseCar.getGearboxType());
        car.setDriveType(baseCar.getDriveType());
        car.setColor(baseCar.getColor());
        car.setEnginePowerHP(baseCar.getEnginePowerHP());
        car.setEngineVolume(baseCar.getEngineVolume());
        car.setBasePrice(baseCar.getBasePrice());
        car.setExtraPrice(baseCar.getExtraPrice());
        car.setCarParts(baseCar.getCarParts());
        return car;
    }
}
