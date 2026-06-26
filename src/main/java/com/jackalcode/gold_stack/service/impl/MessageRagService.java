package com.jackalcode.gold_stack.service.impl;

import com.jackalcode.gold_stack.dto.RagAnswer;
import com.jackalcode.gold_stack.dto.RagSource;
import com.jackalcode.gold_stack.service.RagService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageRagService implements RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public MessageRagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are an assistant for the Gold Stack message system.
                        Answer only using the retrieved message context.
                        If the answer is not in the context, say:
                        'I could not find this information in the message database.'
                        """)
                .defaultAdvisors(QuestionAnswerAdvisor
                        .builder(vectorStore)
                        .build())
                .build();
        this.vectorStore = vectorStore;
    }

    @Override
    public RagAnswer ask(String question) {

        //Retrieve messages with the closest similarity to question
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest
                        .builder()
                        .query(question)
                        .topK(5)
                        .build()
        );

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
        
        var answer = chatClient.prompt()
                .user(
                        """
                                Context:
                                %s
                                
                                Question:
                                %s
                                """.formatted(context, question)
                )
                .call()
                .content();

        List<RagSource> messageSources = documents.stream()
                .map(doc -> new RagSource(
                        Long.valueOf(doc.getMetadata().get("messageId").toString()),
                        doc.getMetadata().get("title").toString()))
                .distinct()
                .toList();

        return new RagAnswer(answer, messageSources);
    }
}
