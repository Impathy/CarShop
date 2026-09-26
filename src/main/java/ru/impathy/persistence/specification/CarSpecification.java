package ru.impathy.persistence.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.impathy.domain.entities.carParts.ComponentType;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.entity.CarPartJpaEntity;

import java.util.List;

public final class CarSpecification {
    private CarSpecification() {
    }

    public static Specification<CarJpaEntity> notRemoved() {
        return (root, query, cb) -> cb.isFalse(root.get("removed"));
    }

    public static Specification<CarJpaEntity> brandEquals(String brand) {
        return (root, query, cb) -> cb.equal(root.get("brand"), brand);
    }

    public static Specification<CarJpaEntity> hasAnyComponentType(List<ComponentType> componentTypes) {
        return (root, query, cb) -> {
            Join<CarJpaEntity, CarPartJpaEntity> parts = root.join("baseParts");
            query.distinct(true);
            return parts.get("componentType").in(componentTypes);
        };
    }
}
