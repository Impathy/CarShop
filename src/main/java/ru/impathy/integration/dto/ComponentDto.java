package ru.impathy.integration.dto;

import ru.impathy.domain.entities.carParts.ComponentType;

import java.util.Set;
import java.util.UUID;

public class ComponentDto {
    private UUID id;
    private String name;
    private ComponentType componentType;
    private int price;
    private Set<String> compatibleModels;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
