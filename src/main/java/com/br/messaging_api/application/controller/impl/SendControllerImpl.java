package com.br.messaging_api.application.controller.impl;

import com.br.messaging_api.application.controller.SendController;
import com.br.messaging_api.domain.otp.OtpRequest;
import com.br.messaging_api.domain.send.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SendControllerImpl implements SendController {

    private final SendService sendService;

    @Override
    public ResponseEntity<Void> generate(final OtpRequest request) {
        sendService.email(request.getTemplateId(), request.getEmailTo(), request.getSubject(),
                request.getTemplateVariables());
        return ResponseEntity.ok().build();
    }
}
