package ru.impathy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.impathy.App;
import ru.impathy.domain.entities.carParts.ComponentType;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.repository.CarJpaRepository;
import ru.impathy.persistence.specification.CarSpecification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class)
class CarJpaRepositoryIT extends BasePostgresIntegrationTest {

    @Autowired
    private CarJpaRepository carJpaRepository;

    @Test
    @Transactional
    void shouldFilterCarsByBrandAndComponentSpecification() {
        List<CarJpaEntity> byBrand = carJpaRepository.findAll(
                CarSpecification.notRemoved().and(CarSpecification.brandEquals("BMW"))
        );
        assertFalse(byBrand.isEmpty());

        List<CarJpaEntity> byComponent = carJpaRepository.findAll(
                CarSpecification.notRemoved().and(CarSpecification.hasAnyComponentType(List.of(ComponentType.WHEELS)))
        );
        assertFalse(byComponent.isEmpty());

        assertTrue(byComponent.stream().anyMatch(c -> "BMW".equals(c.getBrand())));
    }
}
