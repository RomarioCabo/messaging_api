package com.br.messaging_api.controller;

import com.br.messaging_api.application.controller.impl.SendControllerImpl;
import com.br.messaging_api.domain.otp.OtpRequest;
import com.br.messaging_api.domain.send.SendService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static com.br.messaging_api.application.controller.constants.ControllerConstants.SEND;
import static com.br.messaging_api.application.controller.constants.ControllerConstants.V1;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SendControllerTest {

    private MockMvc mockMvc;

    private SendService sendService;

    private ObjectMapper objectMapper;

    private static final String TEMPLATE_ID = "otp-template";
    private static final String EMAIL_TO = "destinatario@example.com";
    private static final String SUBJECT = "Código OTP";
    private static final Map<String, Object> TEMPLATE_VARIABLES = Map.of("code", "123456");

    @BeforeEach
    void setUp() {
        sendService = Mockito.mock(SendService.class);
        SendControllerImpl controller = new SendControllerImpl(sendService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    void shouldSendEmailWithSuccess() {
        OtpRequest request = buildOtpRequest();

        mockMvc.perform(post(V1.concat(SEND))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(sendService, times(1))
                .email(eq(TEMPLATE_ID), eq(EMAIL_TO),
                        eq(SUBJECT), eq(TEMPLATE_VARIABLES));
    }

    private OtpRequest buildOtpRequest() {
        return OtpRequest.builder()
                .templateId(TEMPLATE_ID)
                .emailTo(EMAIL_TO)
                .subject(SUBJECT)
                .templateVariables(TEMPLATE_VARIABLES)
                .build();
    }
}
