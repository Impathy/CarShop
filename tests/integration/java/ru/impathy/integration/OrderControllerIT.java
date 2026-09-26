package ru.impathy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.impathy.App;
import ru.impathy.integration.dto.InStockOrderDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerIT extends BasePostgresIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateInStockOrderAndListForCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String createBody = """
                {
                  "carId": "20000000-0000-0000-0000-000000000001"
                }
                """;

        ResponseEntity<InStockOrderDto> created = restTemplate.postForEntity(
                "/api/orders/in-stock",
                new HttpEntity<>(createBody, headers),
                InStockOrderDto.class
        );

        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertNotNull(created.getBody());
        assertNotNull(created.getBody().getId());
        assertEquals("CREATED", created.getBody().getStatus().name());

        ResponseEntity<InStockOrderDto[]> listed = restTemplate.getForEntity(
                "/api/orders/in-stock",
                InStockOrderDto[].class
        );

        assertEquals(HttpStatus.OK, listed.getStatusCode());
        assertNotNull(listed.getBody());
        assertTrue(listed.getBody().length >= 1);
    }
}
