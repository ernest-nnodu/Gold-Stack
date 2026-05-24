package com.jackalcode.gold_stack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Test
    @DisplayName("getHappyMessage should return happy message and status 200")
    public void getHappyMessage_returnsHappyMessageAndStatus200() throws Exception {

        String expectedTitle = "Happy Message";
        String expectedContent = "Hello From Gold Stack in AWS!";

        mockMvc.perform(get("/")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));
    }

    @Test
    @DisplayName("getErrorMessage should return error message and status 502")
    public void getErrorMessage_returnsErrorMessageAndStatus502() throws Exception {

        String expectedTitle = "Error Message";
        String expectedContent = "This is an error message from Gold Stack in AWS!";

        mockMvc.perform(get("/error")
                        .contentType("application/json"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));

    }

    @Test
    @DisplayName("getCustomMessage should return custom message and status 404")
    public void getCustomMessage_returnsCustomMessageAndStatus404() throws Exception {

        String expectedTitle = "Not Found";
        String expectedContent = "Message not found in Gold Stack in AWS!";

        mockMvc.perform(get("/custom/message")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));
    }
}
