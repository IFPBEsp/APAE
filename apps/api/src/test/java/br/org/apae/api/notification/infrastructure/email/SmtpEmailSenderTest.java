package br.org.apae.api.notification.infrastructure.email;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import br.org.apae.api.notification.domain.exceptions.EmailSendingException;
import br.org.apae.api.notification.domain.model.EmailMessage;

class SmtpEmailSenderTest {

    @Mock
    private JavaMailSender javaMailSender;

    private SmtpEmailSender smtpEmailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        smtpEmailSender = new SmtpEmailSender(javaMailSender);

        ReflectionTestUtils.setField(smtpEmailSender, "mailHost", "localhost");
        ReflectionTestUtils.setField(smtpEmailSender, "mailPort", 2525);
        ReflectionTestUtils.setField(smtpEmailSender, "mailUsername", "user");
        ReflectionTestUtils.setField(smtpEmailSender, "mailPassword", "password");
    }

    @Test
    void shouldSendEmailSuccessfully() {
        EmailMessage email = new EmailMessage(
                List.of("teste@email.com"),
                "Assunto",
                "Corpo");

        smtpEmailSender.send(email);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertArrayEquals(new String[] { "teste@email.com" }, message.getTo());
        assertEquals("Assunto", message.getSubject());
        assertEquals("Corpo", message.getText());
    }

    @Test
    void shouldThrowEmailSendingExceptionWhenJavaMailSenderFails() {
        EmailMessage email = new EmailMessage(
                List.of("teste@email.com"),
                "Assunto",
                "Corpo");

        doThrow(new RuntimeException("Erro SMTP"))
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        assertThrows(
                EmailSendingException.class,
                () -> smtpEmailSender.send(email));

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}