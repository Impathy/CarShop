package ru.impathy.integration.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.impathy.domain.entities.carParts.ComponentType;

import java.util.HashSet;
import java.util.Set;

public class CreateComponentRequest {
    @NotBlank
    private String name;

    @NotNull
    private ComponentType componentType;

    @Min(0)
    private int price;

    @NotEmpty
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
