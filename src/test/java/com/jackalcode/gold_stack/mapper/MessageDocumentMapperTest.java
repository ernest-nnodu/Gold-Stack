package com.jackalcode.gold_stack.mapper;

import com.jackalcode.gold_stack.entity.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDocumentMapperTest {


    private MessageDocumentMapper messageDocumentMapper;

    @BeforeEach
    void setUp() {
        messageDocumentMapper = new MessageDocumentMapper();
    }

    @Test
    @DisplayName("toDocument should return a Document with expected id, content, and meta data when given a valid Message")
    public void toDocument_withValidMessage_shouldReturnDocument() {

        // Create a Message object with test data
        Message testMessage = Message.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .build();

        // Act
        // Call the toDocument method of MessageDocumentMapper
        Document resultDocument = messageDocumentMapper.toDocument(testMessage);

        // Assert
        // Verify that the returned Document has the expected values
        String expectedId = UUID.nameUUIDFromBytes(
                ("message-" + 1L).getBytes(StandardCharsets.UTF_8)).toString();
        String expectedTitle = "Test Title";
        String expectedContent = "Test Content";

        assertThat(resultDocument.getId()).isEqualTo(expectedId);
        assertThat(resultDocument.getText()).contains(expectedTitle, expectedContent);
        assertThat(resultDocument.getMetadata()).containsEntry("messageId", "1")
                .containsEntry("title", expectedTitle);
    }
}
