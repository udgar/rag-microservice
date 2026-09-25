package com.udgar.rag.learning.controller;

import com.udgar.rag.learning.service.RestClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/test/model")
public class TestController {

    private final RestClientService service;

    public TestController(RestClientService service) {
        this.service = service;
    }

    @Operation(
            operationId = "call-llm-rag-service",
            summary = "Api is invoked to anwer the questions regarding available documentation",
            description = "Currently we have an Constitution of nepal as a source docs, in this endpoint the users are allowed to ask questions regarding that document and nothing else"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "LLM provides the appropriate answers as mentioned in document",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string")
                    )
            )
    })
    @PostMapping
    public String answers(@RequestBody String questions) {
        return service.answers(questions);
    }
}
