package ru.impathy.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import ru.impathy.domain.entities.carParts.ComponentType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car_parts")
public class CarPartJpaEntity extends BaseJpaEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false)
    private ComponentType componentType;

    @Column(name = "price", nullable = false)
    private int price;

    @ElementCollection
    @CollectionTable(name = "car_part_compatible_models", joinColumns = @JoinColumn(name = "car_part_id"))
    @Column(name = "model_name", nullable = false)
    private Set<String> compatibleModels = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Set<String> getCompatibleModels() {
        return compatibleModels;
    }

    public void setCompatibleModels(Set<String> compatibleModels) {
        this.compatibleModels = compatibleModels;
    }
}
