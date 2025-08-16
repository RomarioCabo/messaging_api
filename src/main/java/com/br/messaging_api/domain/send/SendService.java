package com.br.messaging_api.domain.send;

import java.util.Map;

public interface SendService {
    void email(final String templateId, final String to, final String subject, final Map<String, Object> templateModel);
}
