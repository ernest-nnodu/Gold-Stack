package com.jackalcode.gold_stack.service;

import com.jackalcode.gold_stack.dto.RagAnswer;
import com.jackalcode.gold_stack.service.impl.MessageRagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageRagServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private VectorStore vectorStore;

    @Mock
    ChatClient.ChatClientRequestSpec chatClientRequestSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    @InjectMocks
    private MessageRagService messageRagService;

    @BeforeEach
    void setUp() {
        when(chatClient.prompt()).thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.user(anyString())).thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.call()).thenReturn(callResponseSpec);
    }

    @Test
    @DisplayName("ask method should return an answer and sources for a given question")
    public void ask_withQuestion_returnsAnswerAndSources() {

        String documentId = UUID.nameUUIDFromBytes(
                ("message-1").getBytes(StandardCharsets.UTF_8)).toString();
        String messageTitle = "Docker";
        String messageContent = "Docker is used to run containers.";
        String content = """
                Title: %s
                
                Content: %s
                """.formatted(messageTitle, messageContent);

        Document doc = new Document(
                documentId,
                content,
                Map.of("messageId", "1", "title", "Docker")
        );

        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of(doc));
        when(callResponseSpec.content())
                .thenReturn("Docker is a platform that uses containers to run applications.");

        RagAnswer response = messageRagService.ask("What is Docker?");

        assertThat(response.answer())
                .contains("containers", "platform that uses containers to run applications.");
        assertThat(response.sources()).hasSize(1);
        assertThat(String.valueOf(response.sources().getFirst().id()))
                .isEqualTo(doc.getMetadata().get("messageId"));
        assertThat(response.sources().getFirst().title())
                .isEqualTo(doc.getMetadata().get("title"));
    }

    @Test
    @DisplayName("ask method should return default message when no relevant context exists")
    public void ask_whenNoRelevantContextExists_returnsDefaultMessage() {

        String expectedResponse = "I could not find this information in the message database.";
        when(vectorStore.similaritySearch(any(SearchRequest.class)))
                .thenReturn(List.of());
        when(callResponseSpec.content())
                .thenReturn(expectedResponse);

        RagAnswer response = messageRagService.ask("What is current company policy?");

        assertThat(response.answer()).isEqualTo(expectedResponse);
        assertThat(response.sources()).isEmpty();
    }
}
