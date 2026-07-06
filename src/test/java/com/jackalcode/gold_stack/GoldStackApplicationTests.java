package com.jackalcode.gold_stack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@ActiveProfiles("test")
class GoldStackApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg16");

    @Test
    void contextLoads() {
    }

}
