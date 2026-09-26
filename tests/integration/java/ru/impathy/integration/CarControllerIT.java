package ru.impathy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.impathy.App;
import ru.impathy.integration.dto.CarDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerIT extends BasePostgresIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnCarsAndSupportFiltering() {
        ResponseEntity<CarDto[]> all = restTemplate.getForEntity("/api/cars", CarDto[].class);
        assertEquals(HttpStatus.OK, all.getStatusCode());
        assertNotNull(all.getBody());
        assertTrue(all.getBody().length >= 1);

        ResponseEntity<CarDto[]> byBrand = restTemplate.getForEntity("/api/cars?brand=BMW", CarDto[].class);
        assertEquals(HttpStatus.OK, byBrand.getStatusCode());
        assertNotNull(byBrand.getBody());
        assertTrue(byBrand.getBody().length >= 1);

        ResponseEntity<CarDto[]> byComponent = restTemplate.getForEntity("/api/cars?componentTypes=WHEELS", CarDto[].class);
        assertEquals(HttpStatus.OK, byComponent.getStatusCode());
        assertNotNull(byComponent.getBody());
        assertTrue(byComponent.getBody().length >= 1);
    }

    @Test
    void shouldReturn404ForMissingCar() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/cars/" + UUID.randomUUID(),
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
