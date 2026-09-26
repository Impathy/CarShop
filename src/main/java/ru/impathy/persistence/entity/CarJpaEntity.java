package ru.impathy.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import ru.impathy.domain.entities.cars.BodyType;
import ru.impathy.domain.entities.cars.DriveType;
import ru.impathy.domain.entities.cars.FuelType;
import ru.impathy.domain.entities.cars.GearboxType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cars")
public class CarJpaEntity extends BaseJpaEntity {
    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type", nullable = false)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gearbox_type", nullable = false)
    private GearboxType gearboxType;

    @Enumerated(EnumType.STRING)
    @Column(name = "drive_type", nullable = false)
    private DriveType driveType;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "engine_power_hp", nullable = false)
    private int enginePowerHP;

    @Column(name = "engine_volume", nullable = false)
    private double engineVolume;

    @Column(name = "base_price", nullable = false)
    private int basePrice;

    @Column(name = "extra_price", nullable = false)
    private int extraPrice;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "car_base_parts",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "car_part_id")
    )
    private Set<CarPartJpaEntity> baseParts = new HashSet<>();

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

    public Set<CarPartJpaEntity> getBaseParts() {
        return baseParts;
    }

    public void setBaseParts(Set<CarPartJpaEntity> baseParts) {
        this.baseParts = baseParts;
    }
}
