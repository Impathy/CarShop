package ru.impathy.domain.entities.cars;

import ru.impathy.domain.entities.carParts.CarPart;
import ru.impathy.domain.entities.carParts.ComponentType;

import java.util.Map;
import java.util.UUID;

public class DefaultCarBuilder implements CarBuilder {

    private Car car = new Car();

    public DefaultCarBuilder buildId() {
        car.setId(UUID.randomUUID());
        return this;
    }

    public DefaultCarBuilder buildBrand(String brand) {
        car.setBrand(brand);
        return this;
    }

    public DefaultCarBuilder buildModelName(String modelName) {
        car.setModelName(modelName);
        return this;
    }

    public DefaultCarBuilder buildBodyType(BodyType bodyType) {
        car.setBodyType(bodyType);
        return this;
    }

    public DefaultCarBuilder buildFuelType(FuelType fuelType) {
        car.setFuelType(fuelType);
        return this;
    }

    public DefaultCarBuilder buildGearboxType(GearboxType gearboxType) {
        car.setGearboxType(gearboxType);
        return this;
    }

    public DefaultCarBuilder buildDriveType(DriveType driveType) {
        car.setDriveType(driveType);
        return this;
    }

    public DefaultCarBuilder buildColor(String color) {
        car.setColor(color);
        return this;
    }

    public DefaultCarBuilder buildEnginePowerHP(int enginePowerHP) {
        car.setEnginePowerHP(enginePowerHP);
        return this;
    }

    public DefaultCarBuilder buildEngineVolume(double engineVolume) {
        car.setEngineVolume(engineVolume);
        return this;
    }

    public DefaultCarBuilder buildBasePrice(int basePrice) {
        car.setBasePrice(basePrice);
        return this;
    }

    public DefaultCarBuilder buildExtraPrice(int extraPrice) {
        car.setExtraPrice(extraPrice);
        return this;
    }

    public DefaultCarBuilder buildCarParts(Map<ComponentType, CarPart> carParts) {
        car.setCarParts(carParts);
        return this;
    }

    public Car getCar() {
        Car result = car;
        car = new Car();
        return result;
    }
}
