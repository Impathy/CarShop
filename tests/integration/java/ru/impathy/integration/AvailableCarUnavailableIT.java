package ru.impathy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.impathy.App;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvailableCarUnavailableIT extends OrderOnlyPostgresIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturn503WhenStorageGrpcIsUnavailable() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/cars", String.class);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }
}
