package com.jackalcode.gold_stack.service.impl;

import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MessageIngestionService implements IngestionService<Message> {

    private final VectorStore vectorStore;

    @Override
    public void ingest(Message message) {

        Document document = toDocument(message);

        vectorStore.add(List.of(document));
    }

    private Document toDocument(Message message) {
        String textToIngest = """
                Title: %s
                
                Content: %s
                """.formatted(message.getTitle(), message.getContent());

        return new Document(
                textToIngest,
                Map.of(
                        "messageId", message.getId().toString(),
                        "title", message.getTitle()
                )
        );
    }
}
