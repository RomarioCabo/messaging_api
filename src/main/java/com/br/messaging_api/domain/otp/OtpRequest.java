package com.br.messaging_api.domain.otp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {

    @NotBlank(message = "O campo templateId não pode ser vazio ou nulo.")
    private String templateId;

    @NotNull(message = "O templateVariables não pode nulo.")
    private Map<String, Object> templateVariables;

    @Email(message = "Digite um e-mail válido.")
    @NotBlank(message = "O campo emailTo não pode ser vazio ou nulo.")
    private String emailTo;

    @NotBlank(message = "O campo subject não pode ser vazio ou nulo.")
    private String subject;
}
