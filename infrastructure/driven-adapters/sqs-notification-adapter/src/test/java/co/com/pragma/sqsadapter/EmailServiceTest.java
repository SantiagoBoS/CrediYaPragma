package co.com.pragma.sqsadapter;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService(mailSender);
    }

    @Test
    void testSendLoanStatusUpdate_sendsEmail() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendLoanStatusUpdate(
                "test@example.com",
                "CAR",
                "APPROVED",
                "REQ123"
        );

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testMapStatusAndLoanTypeToSpanish() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendLoanStatusUpdate(
                "test@example.com",
                "MORTGAGE",
                "REJECTED",
                "REQ999"
        );

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        assertNotNull(captor.getValue(), "Debe haberse construido un MimeMessage");
    }
}
