package com.udgar.rag.learning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    RestClient restClient(@Value("${outbound.llm.request.api}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

}
