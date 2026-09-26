package ru.impathy.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.impathy.App;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = App.class)
class LiquibaseMigrationIT extends BasePostgresIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldApplyMigrationsAndSeedData() {
        Integer carsCount = jdbcTemplate.queryForObject("select count(*) from cars", Integer.class);
        Integer partsCount = jdbcTemplate.queryForObject("select count(*) from car_parts", Integer.class);
        Integer testDriveCarsCount = jdbcTemplate.queryForObject("select count(*) from test_drive_cars", Integer.class);

        assertTrue(carsCount != null && carsCount >= 1);
        assertTrue(partsCount != null && partsCount >= 2);
        assertTrue(testDriveCarsCount != null && testDriveCarsCount >= 1);
    }
}
