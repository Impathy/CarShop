package ru.impathy.domain.entities.carParts;

import java.util.Set;

public class CarPart {
    private String name;
    private ComponentType componentType;
    private int price;
    private Set<String> compatibleModels;

    public CarPart (String name,
                    ComponentType componentType,
                    int price,
                    Set<String> compatibleModels){
        this.name = name;
        this.componentType = componentType;
        this.price = price;
        this.compatibleModels = Set.copyOf(compatibleModels);
    }
    public boolean isCompatibleWith(String brand, String modelName) {
        String fullName = brand + " " + modelName;
        return compatibleModels.contains(fullName);
    }
    public String getName() {
        return name;
    }

    public ComponentType getType() {
        return componentType;
    }

    public int getExtraPrice() {
        return price;
    }
}
