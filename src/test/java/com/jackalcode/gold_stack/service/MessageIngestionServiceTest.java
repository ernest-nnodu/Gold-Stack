package com.jackalcode.gold_stack.service;

import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.mapper.MessageDocumentMapper;
import com.jackalcode.gold_stack.service.impl.MessageIngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageIngestionServiceTest {

    @Mock
    private VectorStore vectorStore;

    @InjectMocks
    private MessageIngestionService messageIngestionService;

    @BeforeEach
    void setUp() {
        messageIngestionService = new MessageIngestionService(vectorStore,
                new MessageDocumentMapper());
    }

    @Test
    @DisplayName("ingest should ingest message embeddings to vector store")
    public void ingest_withMessage_ingestsMessageEmbeddingsToVectorStore() {

        Message message = Message.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .build();

        messageIngestionService.ingest(message);

        var expectedId = UUID.nameUUIDFromBytes(
                ("message-" + 1L).getBytes(StandardCharsets.UTF_8)).toString();

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(vectorStore).add(captor.capture());

        List<Document> documents = captor.getValue();

        assertThat(documents).hasSize(1);
        Document document = documents.getFirst();
        assertThat(document.getId()).isEqualTo(expectedId);
        assertThat(document.getText()).contains("Test Title", "Test Content");
        assertThat(document.getMetadata()).containsEntry("messageId", "1")
                .containsEntry("title", "Test Title");
    }

    @Test
    @DisplayName("reIngest should re-ingest message embeddings to vector store")
    public void reIngest_withMessage_reIngestsMessageEmbeddingsToVectorStore() {

        Message message = Message.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .build();

        messageIngestionService.reIngest(message);

        var expectedId = UUID.nameUUIDFromBytes(
                ("message-" + 1L).getBytes(StandardCharsets.UTF_8)).toString();

        ArgumentCaptor<List<Document>> captor = ArgumentCaptor.forClass(List.class);
        verify(vectorStore).delete(List.of(expectedId));
        verify(vectorStore).add(captor.capture());

        List<Document> documents = captor.getValue();
        assertThat(documents).hasSize(1);
        Document document = documents.getFirst();
        assertThat(document.getId()).isEqualTo(expectedId);
        assertThat(document.getText()).contains("Updated Title", "Updated Content");
        assertThat(document.getMetadata()).containsEntry("messageId", "1")
                .containsEntry("title", "Updated Title");

    }
}
