package com.br.messaging_api.application.controller;

import com.br.messaging_api.application.exception.ErrorHttpResponseDto;
import com.br.messaging_api.domain.otp.OtpRequest;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tags(@Tag(name = "Send", description = "Send REST Controller"))
public interface SendController {

    @ApiResponse(
            responseCode = "200",
            description = "Email enviado com sucesso.",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema())})
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error.",
            content = {@Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorHttpResponseDto.class))
            })
    @PostMapping(value = "send/otp", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> generate(@RequestBody @Valid final OtpRequest request);
}
