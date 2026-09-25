package com.udgar.rag.learning.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class RestClientService {

    private final ChatClient chatClient;


    public RestClientService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String answers(String question) {
        return chatClient.prompt().user(question).call().content();
    }
}
