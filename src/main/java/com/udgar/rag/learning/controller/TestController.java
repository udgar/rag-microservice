package com.udgar.rag.learning.controller;

import com.udgar.rag.learning.service.RestClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/test/model")
public class TestController {

    private final RestClientService service;

    public TestController(RestClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> testMessage() {
        String response = service.testService();
        return ResponseEntity.ok().body(response);
    }
}
