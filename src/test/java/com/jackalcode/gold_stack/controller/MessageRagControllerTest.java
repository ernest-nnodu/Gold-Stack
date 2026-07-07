package com.jackalcode.gold_stack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackalcode.gold_stack.dto.QuestionRequest;
import com.jackalcode.gold_stack.dto.RagAnswer;
import com.jackalcode.gold_stack.dto.RagSource;
import com.jackalcode.gold_stack.service.impl.MessageRagService;
import com.jackalcode.gold_stack.util.MessageDataSeeder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageRagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageRagService messageRagService;

    @MockitoBean
    private MessageDataSeeder messageDataSeeder;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("pgvector/pgvector:pg16");

    @Test
    @DisplayName("Ask question should return 200 status when question is valid")
    public void askQuestion_whenQuestionIsValid_returns200Status() throws Exception {

        String question = "What is your name?";
        QuestionRequest request = new QuestionRequest(question);
        RagAnswer response = new RagAnswer("Answer to your question", null);

        when(messageRagService.ask(question)).thenReturn(response);

        mockMvc.perform(post("/messages/ask")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(messageRagService).ask(question);
    }

    @Test
    @DisplayName("Ask question should return answer and sources when question is valid")
    public void askQuestion_whenQuestionIsValid_returnsAnswerAndSources() throws Exception {

        String question = "What is your name?";
        QuestionRequest request = new QuestionRequest(question);
        List<RagSource> sources = List.of(
                new RagSource(1L, "Source 1"),
                new RagSource(2L, "Source 2")
        );
        RagAnswer response = new RagAnswer("Answer to your question", sources);
        when(messageRagService.ask(question)).thenReturn(response);

        mockMvc.perform(post("/messages/ask")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Answer to your question"))
                .andExpect(jsonPath("$.sources.length()").value(2))
                .andExpect(jsonPath("$.sources[0].id").value(1L))
                .andExpect(jsonPath("$.sources[0].title").value("Source 1"))
                .andExpect(jsonPath("$.sources[1].id").value(2L))
                .andExpect(jsonPath("$.sources[1].title").value("Source 2"));

        verify(messageRagService).ask(question);
    }

    @Test
    @DisplayName("Ask question should return 400 status when question is invalid")
    public void askQuestion_whenQuestionIsInvalid_returns400Status() throws Exception {

        QuestionRequest request = new QuestionRequest(null);

        mockMvc.perform(post("/messages/ask")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Ask question should return 400 status when question is blank")
    public void askQuestion_whenQuestionIsEmpty_returns400Status() throws Exception {

        QuestionRequest request = new QuestionRequest("");

        mockMvc.perform(post("/messages/ask")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
