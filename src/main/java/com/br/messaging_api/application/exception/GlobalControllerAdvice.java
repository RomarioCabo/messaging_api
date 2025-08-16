package com.br.messaging_api.application.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final MessageSource messageSource;
    private final static String TITLE = "Ops! Ocorreu um erro";

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorHttpResponseDto> handleRuntimeException(Exception ex) {
        ErrorHttpResponseDto errorHttpResponseDto = new ErrorHttpResponseDto(
                INTERNAL_SERVER_ERROR.toString(),
                TITLE,
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .contentType(APPLICATION_JSON)
                .body(errorHttpResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHttpResponseDto> handle(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errorsByField = exception.getBindingResult().getFieldErrors().stream()
                .collect(groupingBy(FieldError::getField, LinkedHashMap::new,
                        mapping(e -> messageSource.getMessage(e, LocaleContextHolder.getLocale()), toList())));

        List<String> errors = errorsByField.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .flatMap(entry -> entry.getValue().stream().sorted())
                .toList();

        ErrorHttpResponseDto errorHttpResponseDto = new ErrorHttpResponseDto(
                BAD_REQUEST.toString(),
                TITLE,
                errors.toString(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(errorHttpResponseDto);
    }
}
