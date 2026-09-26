package ru.impathy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.impathy.App;
import ru.impathy.integration.dto.AvailableCarDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvailableCarControllerIT extends AvailableCarPostgresIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldReturnAvailableCarsThroughGrpcBackedRestEndpoint() {
        ResponseEntity<AvailableCarDto[]> response = restTemplate.getForEntity("/api/v1/cars", AvailableCarDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
        assertEquals("BMW", response.getBody()[0].getBrand());
    }

    @Test
    void shouldReturnSingleAvailableCarThroughGrpcBackedRestEndpoint() {
        ResponseEntity<AvailableCarDto> response = restTemplate.getForEntity(
                "/api/v1/cars/20000000-0000-0000-0000-000000000001",
                AvailableCarDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("BMW", response.getBody().getBrand());
    }

    @Test
    void shouldReturnEmptyListWhenNoCarsAreAvailable() {
        Integer updated = jdbcTemplate.update("update car_stock set reserved_quantity = total_quantity");
        assertNotNull(updated);

        try {
            ResponseEntity<AvailableCarDto[]> response = restTemplate.getForEntity("/api/v1/cars", AvailableCarDto[].class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(0, response.getBody().length);
        } finally {
            jdbcTemplate.update("update car_stock set reserved_quantity = 0");
        }
    }
}
