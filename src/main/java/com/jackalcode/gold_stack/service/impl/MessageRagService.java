package com.jackalcode.gold_stack.service.impl;

import com.jackalcode.gold_stack.service.RagService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class MessageRagService implements RagService {

    private final ChatClient chatClient;

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
    }

    @Override
    public String ask(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}
