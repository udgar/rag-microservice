package com.udgar.rag.learning.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientService {

    private final RestClient restClient;

    public RestClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String testService() {
        OllamaRequest request = new OllamaRequest("llama3.2", "just say hi", false);

        OllamaResponse response = restClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OllamaResponse.class);

        return response != null ? response.response() : "";
    }

    public record OllamaRequest(String model, String prompt, boolean stream) {}

    public record OllamaResponse(
            String model,
            String response,
            boolean done
    ) {}
}
