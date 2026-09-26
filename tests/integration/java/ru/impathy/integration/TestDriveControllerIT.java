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
import ru.impathy.integration.dto.TestDriveRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestDriveControllerIT extends BasePostgresIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateTestDriveRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
                {
                  "carId": "20000000-0000-0000-0000-000000000001",
                  "startAt": "2030-01-01T10:00:00Z"
                }
                """;

        ResponseEntity<TestDriveRequestDto> created = restTemplate.postForEntity(
                "/api/test-drive/requests",
                new HttpEntity<>(body, headers),
                TestDriveRequestDto.class
        );

        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertNotNull(created.getBody());
        assertNotNull(created.getBody().getId());
        assertEquals("00000000-0000-0000-0000-000000000004", created.getBody().getClientId().toString());
    }
}
