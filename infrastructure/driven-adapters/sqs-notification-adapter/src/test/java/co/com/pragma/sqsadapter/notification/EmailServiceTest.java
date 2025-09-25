package co.com.pragma.sqsadapter.notification;

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
    void testSendLoanStatusUpdate_sendsEmailApprovedWithPaymentPlan() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendLoanStatusUpdate(
                "test@example.com",
                "CAR",
                "APPROVED",
                "REQ123",
                10000.0,
                12.0,
                6
        );

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendLoanStatusUpdate_sendsEmailRejectedWithoutPaymentPlan() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendLoanStatusUpdate(
                "test@example.com",
                "MORTGAGE",
                "REJECTED",
                "REQ999",
                5000.0,
                10.0,
                12
        );

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());

        assertNotNull(captor.getValue(), "Debe haberse construido un MimeMessage");
    }

    @Test
    void testSendLoanStatusUpdate_whenMailSenderThrowsRuntimeException() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(sender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Fallo interno")).when(sender).send(any(MimeMessage.class));

        EmailService service = new EmailService(sender);

        assertThrows(RuntimeException.class, () ->
                service.sendLoanStatusUpdate(
                        "test@example.com",
                        "CAR",
                        "APPROVED",
                        "REQ123",
                        1000.0,
                        8.0,
                        3
                )
        );
    }
}
