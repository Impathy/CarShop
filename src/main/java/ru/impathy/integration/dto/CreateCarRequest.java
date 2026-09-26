package ru.impathy.integration.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.impathy.domain.entities.cars.BodyType;
import ru.impathy.domain.entities.cars.DriveType;
import ru.impathy.domain.entities.cars.FuelType;
import ru.impathy.domain.entities.cars.GearboxType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateCarRequest {
    @NotBlank
    private String brand;

    @NotBlank
    private String modelName;

    @NotNull
    private BodyType bodyType;

    @NotNull
    private FuelType fuelType;

    @NotNull
    private GearboxType gearboxType;

    @NotNull
    private DriveType driveType;

    @NotBlank
    private String color;

    @Min(1)
    private int enginePowerHP;

    @Min(1)
    private double engineVolume;

    @Min(0)
    private int basePrice;

    @Min(0)
    private int extraPrice;

    private List<UUID> basePartIds = new ArrayList<>();

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

    public List<UUID> getBasePartIds() {
        return basePartIds;
    }

    public void setBasePartIds(List<UUID> basePartIds) {
        this.basePartIds = basePartIds;
    }
}
