package com.udgar.rag.learning.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder, VectorStore store) {
        return builder
                .defaultSystem("You are a chat assistant that only answers questions, regarding the information stored in vector store," +
                        "if asked for anything else deny the request ")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(store).build())
                .build();
    }

}
