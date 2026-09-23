package com.udgar.rag.learning.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientService {

    private final RestClient restClient;

    public RestClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String testService() {
        return restClient.post()
                .uri("/api/generate")
                .body("{\"model\"}:{\"llama3.2\"},{\"prompt\"}:{\"just say hi\"}")
                .retrieve()
                .body(String.class);
    }
}
