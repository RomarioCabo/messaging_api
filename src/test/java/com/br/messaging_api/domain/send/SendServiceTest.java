package com.br.messaging_api.domain.send;

import com.br.messaging_api.domain.send.impl.SendServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SendServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    private AutoCloseable closeable;

    @InjectMocks
    private SendServiceImpl sendService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(sendService, "from", "test@example.com");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void shouldSendEmailWithSuccess() {
        String templateId = "email-template";
        String to = "destinatario@example.com";
        String subject = "Assunto de Teste";
        Map<String, Object> templateModel = Map.of("nome", "Romário");

        Mockito.when(templateEngine.process(Mockito.eq(templateId), Mockito.any(Context.class)))
                .thenReturn("<html><body>Email de Teste</body></html>");

        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        Mockito.when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        sendService.email(templateId, to, subject, templateModel);

        Mockito.verify(mailSender, Mockito.times(1)).send(mimeMessage);
        Mockito.verify(templateEngine, Mockito.times(1)).process(Mockito.eq(templateId), Mockito.any(Context.class));
    }

    @Test
    void shouldThrowsExceptionWhenTrySendEmail() {
        String templateId = "template";
        String to = "destinatario@example.com";
        String subject = "Erro Teste";

        Mockito.when(templateEngine.process(Mockito.eq(templateId), Mockito.any(Context.class)))
                .thenReturn("<html>conteúdo</html>");
        Mockito.doThrow(new RuntimeException("Falha ao enviar e-mail"))
                .when(mailSender).send(Mockito.any(MimeMessage.class));

        assertThrows(RuntimeException.class,
                () -> sendService.email(templateId, to, subject, Map.of("x", "y")));
    }
}
