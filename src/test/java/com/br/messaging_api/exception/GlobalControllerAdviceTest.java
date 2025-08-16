package com.br.messaging_api.exception;

import com.br.messaging_api.application.exception.GlobalControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class GlobalControllerAdviceTest {
    private GlobalControllerAdvice advice;
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        messageSource = mock(MessageSource.class);
        advice = new GlobalControllerAdvice(messageSource);
    }

    @Test
    void handleRuntimeException_returnsInternalServerError() {
        Exception ex = new RuntimeException("Erro inesperado");

        var response = advice.handleRuntimeException(ex);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("500 INTERNAL_SERVER_ERROR", response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals("Erro inesperado", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleMethodArgumentNotValidException_returnsBadRequest() {
        // mock do BindingResult e FieldError
        FieldError fieldError = new FieldError("obj", "campo", "Erro no campo");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // mock do MessageSource
        when(messageSource.getMessage(any(FieldError.class), any(Locale.class)))
                .thenReturn("Erro traduzido");

        var response = advice.handle(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("400 BAD_REQUEST", response.getBody().getCode());
        assertEquals("Ops! Ocorreu um erro", response.getBody().getTitle());
        assertEquals("[Erro traduzido]", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}
