package ru.impathy.domain.entities.cars;

import ru.impathy.domain.entities.carParts.CarPart;
import ru.impathy.domain.entities.carParts.ComponentType;

import java.util.Map;

public interface CarBuilder {
    CarBuilder buildId();

    CarBuilder buildBrand(String brand);

    CarBuilder buildModelName(String modelName);

    CarBuilder buildBodyType(BodyType bodyType);

    CarBuilder buildFuelType(FuelType fuelType);

    CarBuilder buildGearboxType(GearboxType gearboxType);

    CarBuilder buildDriveType(DriveType driveType);

    CarBuilder buildColor(String color);

    CarBuilder buildEnginePowerHP(int enginePowerHP);

    CarBuilder buildEngineVolume(double engineVolume);

    CarBuilder buildBasePrice(int basePrice);

    CarBuilder buildExtraPrice(int extraPrice);

    CarBuilder buildCarParts(Map<ComponentType, CarPart> carParts);

    Car getCar();
}

