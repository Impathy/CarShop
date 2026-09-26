package ru.impathy.service;

import ru.impathy.domain.entities.cars.BodyType;
import ru.impathy.domain.entities.cars.DriveType;
import ru.impathy.domain.entities.cars.FuelType;
import ru.impathy.domain.entities.cars.GearboxType;

public class CarFilter {
    private Integer minPrice;
    private Integer maxPrice;
    private String brand;
    private String modelName;
    private BodyType bodyType;
    private FuelType fuelType;
    private Integer minEnginePowerHP;
    private Integer maxEnginePowerHP;
    private Double minEngineVolume;
    private Double maxEngineVolume;
    private GearboxType gearboxType;
    private DriveType driveType;
    private String color;

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
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

    public Integer getMinEnginePowerHP() {
        return minEnginePowerHP;
    }

    public void setMinEnginePowerHP(Integer minEnginePowerHP) {
        this.minEnginePowerHP = minEnginePowerHP;
    }

    public Integer getMaxEnginePowerHP() {
        return maxEnginePowerHP;
    }

    public void setMaxEnginePowerHP(Integer maxEnginePowerHP) {
        this.maxEnginePowerHP = maxEnginePowerHP;
    }

    public Double getMinEngineVolume() {
        return minEngineVolume;
    }

    public void setMinEngineVolume(Double minEngineVolume) {
        this.minEngineVolume = minEngineVolume;
    }

    public Double getMaxEngineVolume() {
        return maxEngineVolume;
    }

    public void setMaxEngineVolume(Double maxEngineVolume) {
        this.maxEngineVolume = maxEngineVolume;
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
}
