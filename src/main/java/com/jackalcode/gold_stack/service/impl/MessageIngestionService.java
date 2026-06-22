package com.jackalcode.gold_stack.service.impl;

import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.mapper.MessageDocumentMapper;
import com.jackalcode.gold_stack.service.IngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageIngestionService implements IngestionService<Message> {

    private final VectorStore vectorStore;
    private final MessageDocumentMapper messageDocumentMapper;

    @Override
    public void ingest(Message message) {

        Document document = messageDocumentMapper.toDocument(message);

        vectorStore.add(List.of(document));
    }

    @Override
    public void reIngest(Message message) {

        //Delete existing document in vector database using its document ID, then add the updated document
        var documentId = messageDocumentMapper.toDocumentId(message.getId());
        vectorStore.delete(List.of(documentId));

        var document =  messageDocumentMapper.toDocument(message);
        vectorStore.add(List.of(document));
    }
}
