package com.udgar.rag.learning.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfiguration {

    // Here when Chat client is created first default question answer advisor is set, who retrieves the context of user's question from the vector store and add it to prompt
    // Default system provides the default system prompt that is sent via all the prompt provided by the user
    //search request is used to instruct to get top 2 similar search from the similarity search instead of all of them.
    @Bean
    ChatClient chatClient(ChatClient.Builder builder, VectorStore store) {
        return builder
                .defaultSystem("You are a chat assistant that only answers questions, regarding the information stored in vector store," +
                        "if asked for anything else deny the request ")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(store).searchRequest(SearchRequest.builder().topK(2).build()).build())
                .build();
    }

}
