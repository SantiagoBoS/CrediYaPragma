package co.com.pragma.sqsadapter.notification;

import co.com.pragma.model.loan.constants.RequestStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    /**
     * Envía la actualización de estado del préstamo al usuario.
     * Solo se genera el plan de pagos si el préstamo fue aprobado.
     */
    public void sendLoanStatusUpdate(
            String to,
            String loanType,
            String status,
            String loanRequestId,
            double amount,
            double annualInterestRate,
            int termMonths
    ) {
        try {
            String statusInSpanish = mapStatusToSpanish(status);
            String loanTypeInSpanish = mapLoanTypeToSpanish(loanType);

            String subject = "Actualización de tu préstamo";
            StringBuilder contentBuilder = new StringBuilder("""
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
                    """.formatted(loanTypeInSpanish, statusInSpanish, statusInSpanish.toLowerCase(), loanRequestId));

            // Solo si fue aprobado, mostrar el plan de pagos
            if (RequestStatus.APPROVED.toString().equalsIgnoreCase(status)) {
                contentBuilder.append("<h3>Plan de pagos:</h3>");
                contentBuilder.append("<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">")
                        .append("<tr><th>Mes</th><th>Cuota</th><th>Intereses</th><th>Capital</th><th>Saldo Restante</th></tr>");

                for (String row : generatePaymentPlan(amount, annualInterestRate, termMonths)) {
                    contentBuilder.append(row);
                }

                contentBuilder.append("</table>");
            }

            contentBuilder.append("""
                            <p style="font-size: 12px; color: #7f8c8d;">
                                <br>
                                Gracias por confiar en CrediYa
                                <br>
                            </p>
                        </body>
                    </html>
                    """);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contentBuilder.toString(), true);

            mailSender.send(mimeMessage);
            log.info("Correo enviado a {}", to);

        } catch (MessagingException e) {
            log.error("Error enviando correo a {}: {}", to, e.getMessage(), e);
        }
    }

    private List<String> generatePaymentPlan(double amount, double annualRate, int termMonths) {
        List<String> rows = new ArrayList<>();
        double monthlyRate = annualRate / 12.0 / 100.0; // Convertir % anual a decimal mensual
        double remainingBalance = amount;

        for (int month = 1; month <= termMonths; month++) {
            double monthlyPayment = (amount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termMonths));
            double interest = remainingBalance * monthlyRate;
            double principal = monthlyPayment - interest;
            remainingBalance -= principal;

            if (remainingBalance < 0) remainingBalance = 0;

            rows.add("""
                    <tr>
                        <td>%d</td>
                        <td>%.2f</td>
                        <td>%.2f</td>
                        <td>%.2f</td>
                        <td>%.2f</td>
                    </tr>
                    """.formatted(month, monthlyPayment, interest, principal, remainingBalance));
        }

        return rows;
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
