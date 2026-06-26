package com.jackalcode.gold_stack.util;

import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.repository.MessageRepository;
import com.jackalcode.gold_stack.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDataSeeder implements CommandLineRunner {

    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final int NUMBER_OF_MESSAGES_TO_SEED = 20;

    @Override
    public void run(String... args) {

        if (messageRepository.count() > 0) {
            System.out.println("Data already seeded");
            return;
        }

        for (int index = 1; index <= NUMBER_OF_MESSAGES_TO_SEED; index++) {

            CreateMessageRequest request = new CreateMessageRequest(
                    "Sample Message " + index,
                    generateContent(index));

            messageService.createMessage(request);
        }

        System.out.println("Seeded " + NUMBER_OF_MESSAGES_TO_SEED + " sample messages.");
    }

    private String generateContent(int index) {

        return switch (index) {

            case 1 -> """
                    Spring Boot automatically configures many beans based on the dependencies
                    available on the classpath.
                    """;

            case 2 -> """
                    Docker images are immutable templates used to create containers.
                    Containers share the host operating system kernel.
                    """;

            case 3 -> """
                    PostgreSQL supports ACID transactions and provides strong consistency
                    guarantees for relational data.
                    """;

            case 4 -> """
                    GitHub Actions automates build, test and deployment pipelines using YAML workflows.
                    """;

            case 5 -> """
                    Spring Security provides authentication and authorization for web applications.
                    """;

            case 6 -> """
                    Prometheus periodically scrapes metrics from configured targets.
                    """;

            case 7 -> """
                    Grafana visualises metrics collected from Prometheus using dashboards.
                    """;

            case 8 -> """
                    Flyway manages version-controlled database migrations.
                    """;

            case 9 -> """
                    pgvector enables semantic similarity search inside PostgreSQL.
                    """;

            case 10 -> """
                    Spring AI integrates LLMs, embeddings and vector databases into Spring Boot applications.
                    """;

            case 11 -> """
                    Docker Compose manages multi-container applications using declarative configuration.
                    """;

            case 12 -> """
                    Nginx commonly acts as a reverse proxy in front of Spring Boot applications.
                    """;

            case 13 -> """
                    Amazon Lightsail provides simple virtual machines for hosting web applications.
                    """;

            case 14 -> """
                    Testcontainers creates disposable Docker containers for integration testing.
                    """;

            case 15 -> """
                    HikariCP is the default connection pool used by Spring Boot.
                    """;

            case 16 -> """
                    JWT tokens are commonly used for stateless authentication.
                    """;

            case 17 -> """
                    REST APIs should return appropriate HTTP status codes for each operation.
                    """;

            case 18 -> """
                    Vector embeddings capture semantic meaning instead of exact keyword matches.
                    """;

            case 19 -> """
                    Retrieval Augmented Generation combines vector search with large language models.
                    """;

            default -> """
                    Gold Stack is a Spring Boot backend demonstrating production-ready
                    architecture, observability, testing, Docker, CI/CD and AI integration.
                    """;
        };
    }
}
