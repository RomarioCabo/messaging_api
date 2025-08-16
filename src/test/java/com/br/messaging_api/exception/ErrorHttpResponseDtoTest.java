package com.br.messaging_api.exception;

import com.br.messaging_api.application.exception.ErrorHttpResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHttpResponseDtoTest {

    @Test
    void testGetterSetter() {
        ErrorHttpResponseDto dto = new ErrorHttpResponseDto();

        LocalDateTime now = LocalDateTime.now();
        dto.setCode("400");
        dto.setTitle("Bad Request");
        dto.setMessage("Dados inválidos");
        dto.setTimestamp(now);

        assertEquals("400", dto.getCode());
        assertEquals("Bad Request", dto.getTitle());
        assertEquals("Dados inválidos", dto.getMessage());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorHttpResponseDto dto = new ErrorHttpResponseDto(
                "404",
                "Not Found",
                "Recurso não encontrado",
                now
        );

        assertEquals("404", dto.getCode());
        assertEquals("Not Found", dto.getTitle());
        assertEquals("Recurso não encontrado", dto.getMessage());
        assertEquals(now, dto.getTimestamp());
    }

    @Test
    @SneakyThrows
    void testJsonSerialization() {
        LocalDateTime now = LocalDateTime.of(2025, 8, 16, 17, 30, 45);
        ErrorHttpResponseDto dto = new ErrorHttpResponseDto(
                "500",
                "Internal Server Error",
                "Erro inesperado",
                now
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = mapper.writeValueAsString(dto);

        assertTrue(json.contains("\"code\":\"500\""));
        assertTrue(json.contains("\"title\":\"Internal Server Error\""));
        assertTrue(json.contains("\"message\":\"Erro inesperado\""));
        assertTrue(json.contains("\"timestamp\":\"16-08-2025 17:30:45\""));
    }
}
