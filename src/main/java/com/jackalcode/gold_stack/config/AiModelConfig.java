package com.jackalcode.gold_stack.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiModelConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, VectorStore  vectorStore) {

        return chatClientBuilder
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
}
