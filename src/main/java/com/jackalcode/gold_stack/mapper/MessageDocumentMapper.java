package com.jackalcode.gold_stack.mapper;

import com.jackalcode.gold_stack.entity.Message;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Component
public class MessageDocumentMapper {

    public Document toDocument(Message message) {
        String content = """
                Title: %s
                
                Content: %s
                """.formatted(message.getTitle(), message.getContent());

        return new Document(
                toDocumentId(message.getId()),
                content,
                Map.of("messageId", message.getId().toString(),
                        "title", message.getTitle()));
    }

    public String toDocumentId(Long messageId) {

        return UUID.nameUUIDFromBytes(
                ("message-" + messageId).getBytes(StandardCharsets.UTF_8)).toString();
    }
}
