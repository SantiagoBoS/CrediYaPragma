package co.com.pragma.sqsadapter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendLoanStatusUpdate(String to, String loanType, String status, String loanRequestId) {
        //Para el envio del correo electronico, su estrcutura
        try {
            String statusInSpanish = mapStatusToSpanish(status);
            String loanTypeInSpanish = mapLoanTypeToSpanish(loanType);

            String subject = "Actualización de tu préstamo";
            String content = """
                    <html>
                        <body style="font-family: Arial, sans-serif; color: #333;">
                            <h2 style="color: #2C3E50;">Tu préstamo <span style="color:#27AE60;">%s</span></h2>
                            <p>Queremos informarte que el estado de tu préstamo ha cambiado.</p>
                            <p><strong>Nuevo estado:</strong> <span style="color:#2980B9;">%s</span></p>
                            <p>Tu préstamo ha sido revisado y ahora está <strong>%s</strong>.</p>
                            <p>
                                Acércate a nuestras oficinas y muéstrale a alguno de nuestros asesores
                                este código: <strong style="color:#E74C3C;">%s</strong>,
                                o escríbenos en nuestra página web.
                            </p>
                            <p style="font-size: 12px; color: #7f8c8d;">
                                <br>
                                Gracias por confiar en CrediYa
                                <br>
                            </p>
                        </body>
                    </html>
                    """.formatted(loanTypeInSpanish, statusInSpanish, statusInSpanish.toLowerCase(), loanRequestId);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(mimeMessage);
            log.info("Correo enviado a {}", to);

        } catch (MessagingException e) {
            log.error("Error enviando correo a {}: {}", to, e.getMessage(), e);
        }
    }

    private String mapStatusToSpanish(String status) {
        return switch (status.toUpperCase()) {
            case "APPROVED" -> "Aprobado";
            case "REJECTED" -> "Rechazado";
            default -> status;
        };
    }

    private String mapLoanTypeToSpanish(String loanType) {
        return switch (loanType.toUpperCase()) {
            case "CAR" -> "Vehicular";
            case "MORTGAGE" -> "Hipotecario";
            case "PERSONAL" -> "Personal";
            default -> loanType;
        };
    }
}
