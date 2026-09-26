package ru.impathy.integration;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles({"test", "order", "storage"})
public abstract class BasePostgresIntegrationTest {
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("impathy_test")
            .withUsername("impathy")
            .withPassword("impathy");

    static {
        if (!postgres.isRunning()) {
            postgres.start();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("app.grpc.server-port", () -> 0);
        registry.add("app.grpc.storage-host", () -> "localhost");
        registry.add("app.grpc.storage-port", () -> 0);
        registry.add("app.grpc.timeout-ms", () -> 300L);
    }
}
