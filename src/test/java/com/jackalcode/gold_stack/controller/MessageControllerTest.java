package com.jackalcode.gold_stack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.repository.MessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageRepository messageRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Test
    @DisplayName("getHappyMessage should return happy message and status 200")
    public void getHappyMessage_returnsHappyMessageAndStatus200() throws Exception {

        String expectedTitle = "Happy Message";
        String expectedContent = "Hello From Gold Stack in AWS!!!";

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

    @Test
    @DisplayName("getMessages should return list of messages and status 200")
    public void getMessages_returnsListOfMessagesAndStatus200() throws Exception {

        messageRepository.saveAllAndFlush(List.of(
                createMessage("Title 1", "Content 1"),
                createMessage("Title 2", "Content 2"),
                createMessage("Title 3", "Content 3")
        ));

        mockMvc.perform(get("/messages")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("getMessage should return message and status 200")
    public void getMessage_returnsMessageAndStatus200() throws Exception {

        var expectedTitle = "Title 1";
        var expectedContent = "Content 1";

        var savedMessage = messageRepository.saveAndFlush(createMessage("Title 1", "Content 1"));
        var savedMessageId = savedMessage.getId();

        mockMvc.perform(get("/messages/{id}", savedMessageId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedMessageId))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));
    }

    @Test
    @DisplayName("createMessage should return created message and status 201")
    public void createMessage_returnsCreatedMessageAndStatus201() throws Exception {

        var messageRequest = new CreateMessageRequest("Title 1", "Content 1");
        var expectedTitle = "Title 1";
        var expectedContent = "Content 1";

        mockMvc.perform(post("/messages")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));
    }

    private Message createMessage(String title, String content) {

        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setCreatedAt(Instant.now());

        return message;
    }
}
