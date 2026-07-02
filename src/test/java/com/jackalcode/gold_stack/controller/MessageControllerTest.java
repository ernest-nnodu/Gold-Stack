package com.jackalcode.gold_stack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.repository.MessageRepository;
import com.jackalcode.gold_stack.service.impl.MessageIngestionService;
import com.jackalcode.gold_stack.util.MessageDataSeeder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockitoBean
    private MessageIngestionService messageIngestionService;

    @MockitoBean
    private MessageDataSeeder messageDataSeeder;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg16");

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
    public void getMessage_whenMessageExists_returnsMessageAndStatus200() throws Exception {

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
    @DisplayName("getMessage when message does not exist should return status 404")
    public void getMessage_whenMessageDoesNotExist_returnsStatus404() throws Exception {

        var nonExistentMessageId = 999L;

        mockMvc.perform(get("/messages/{id}", nonExistentMessageId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
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

    @Test
    @DisplayName("createMessage when request is invalid should return status 400")
    public void createMessage_whenRequestIsInvalid_returnsStatus400() throws Exception {

        var invalidMessageRequest = new CreateMessageRequest("", "");

        mockMvc.perform(post("/messages")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidMessageRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("updateMessage should return updated message and status 200")
    public void updateMessage_whenMessageExists_shouldReturnUpdatedMessageAndStatus200() throws Exception {

        var persistedMessage = messageRepository.saveAndFlush(createMessage("Title 1", "Content 1"));
        var persistedMessageId = persistedMessage.getId();

        var updateMessageRequest = createMessage("Updated Title", "Updated Content");
        var expectedTitle = "Updated Title";
        var expectedContent = "Updated Content";

        mockMvc.perform(put("/messages/{id}", persistedMessageId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateMessageRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(persistedMessageId))
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.content").value(expectedContent));
    }

    @Test
    @DisplayName("updateMessage should return status 404 when message does not exist")
    public void updateMessage_whenMessageDoesNotExist_returnsStatus404() throws Exception {

        var nonExistentMessageId = 999L;

        mockMvc.perform(put("/messages/{id}", nonExistentMessageId)
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                createMessage("Updated Title", "Updated Content"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMessage_whenRequestIsInvalid_returnsStatus400() throws Exception {

        var persistedMessage = messageRepository.saveAndFlush(createMessage("Title 1", "Content 1"));
        var persistedMessageId = persistedMessage.getId();

        var invalidUpdateRequest = new CreateMessageRequest("", "");

        mockMvc.perform(put("/messages/{id}", persistedMessageId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUpdateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("deleteMessage should return status 204 when message exists")
    public void deleteMessage_whenMessageExists_returnsStatus204() throws Exception {

        var persistedMessage = messageRepository.saveAndFlush(createMessage("Title 1", "Content 1"));
        var persistedMessageId = persistedMessage.getId();

        mockMvc.perform(delete("/messages/{id}", persistedMessageId)
                        .contentType("application/json"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deleteMessage should return status 404 when message does not exist")
    public void deleteMessage_whenMessageDoesNotExist_returnsStatus404() throws Exception {

        var nonExistentMessageId =  999L;

        mockMvc.perform(delete("/messages/{id}", nonExistentMessageId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    private Message createMessage(String title, String content) {

        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);
        message.setCreatedAt(Instant.now());

        return message;
    }
}
