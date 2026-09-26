package ru.impathy.integration.dto;

import ru.impathy.domain.entities.cars.BodyType;
import ru.impathy.domain.entities.cars.DriveType;
import ru.impathy.domain.entities.cars.FuelType;
import ru.impathy.domain.entities.cars.GearboxType;

import java.util.List;
import java.util.UUID;

public class CarDto {
    private UUID id;
    private String brand;
    private String modelName;
    private BodyType bodyType;
    private FuelType fuelType;
    private GearboxType gearboxType;
    private DriveType driveType;
    private String color;
    private int enginePowerHP;
    private double engineVolume;
    private int basePrice;
    private int extraPrice;
    private List<ComponentDto> components;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public GearboxType getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(GearboxType gearboxType) {
        this.gearboxType = gearboxType;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveType driveType) {
        this.driveType = driveType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getEnginePowerHP() {
        return enginePowerHP;
    }

    public void setEnginePowerHP(int enginePowerHP) {
        this.enginePowerHP = enginePowerHP;
    }

    public double getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(double engineVolume) {
        this.engineVolume = engineVolume;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(int extraPrice) {
        this.extraPrice = extraPrice;
    }

    public List<ComponentDto> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentDto> components) {
        this.components = components;
    }
}
