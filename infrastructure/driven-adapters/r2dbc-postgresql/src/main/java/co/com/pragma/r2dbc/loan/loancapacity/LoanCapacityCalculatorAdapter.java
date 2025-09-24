package co.com.pragma.r2dbc.loan.loancapacity;

import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanCapacityCalculatorService;
import co.com.pragma.r2dbc.loan.loanupdate.LoanUpdateReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class LoanCapacityCalculatorAdapter implements LoanCapacityCalculatorService {

    private final LoanUpdateReactiveRepository loanRepository;

    public LoanCapacityCalculatorAdapter(LoanUpdateReactiveRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Mono<CapacityResult> calculateCapacity(
            String userId,
            BigDecimal income,
            Double loanAmount,
            Double annualInterestRate,
            Integer termInMonths
    ) {
        return loanRepository.findAll() // Obtener todos los préstamos activos
                .filter(loan -> loan.getClientDocument().equals(userId) &&
                        loan.getStatus().name().equals(RequestStatus.APPROVED.toString())
                )
                .collectList()
                .map(activeLoans -> {
                    double maxCapacity = income.doubleValue() * 0.35;
                    double monthlyRate = (annualInterestRate / 100) / 12;

                    //Deuda mensual actual
                    double currentDebt = activeLoans.stream()
                            .mapToDouble(loan -> calculateLoanPayment(
                                    loan.getAmount(),
                                    monthlyRate,
                                    loan.getTermMonths()))
                            .sum();

                    //Capacidad disponible
                    double availableCapacity = maxCapacity - currentDebt;

                    //Cuota del nuevo préstamo
                    double newLoanPayment = calculateLoanPayment(loanAmount, monthlyRate, termInMonths);

                    //Decisión final
                    String decision;
                    if (newLoanPayment <= availableCapacity) {
                        decision = (loanAmount > income.doubleValue() * 5) ? RequestStatus.MANUAL_REVIEW.toString() : RequestStatus.APPROVED.toString();
                    } else {
                        decision = RequestStatus.REJECTED.toString();
                    }

                    //Generar plan de pagos si aplica
                    List<LoanInstallment> paymentPlan = new ArrayList<>();
                    if (decision.equals(RequestStatus.APPROVED.toString()) || decision.equals(RequestStatus.MANUAL_REVIEW.toString())) {
                        paymentPlan = generatePaymentPlan(loanAmount, monthlyRate, termInMonths);
                    }

                    return CapacityResult.builder()
                            .decision(decision)
                            .paymentPlan(paymentPlan)
                            .build();
                });
    }

    //Calcula la cuota mensual de un préstamo usando la fórmula de amortización.
    //Fórmula: Resltado = (monto del préstamo * tasa de interés mensual) / (1 - (1 + i)^(-plazo en meses))
    private double calculateLoanPayment(double amount, double monthlyRate, int termMonths) {
        // Si la tasa de interés es 0%, simplemente dividimos el monto por la cantidad de meses
        if (monthlyRate == 0) return amount / termMonths;

        //Monto del préstamo * tasa de interés mensual
        double numerator = amount * monthlyRate;

        //Fórmula: 1 - (1 + i)^(-plazo en meses)
        double denominator = 1 - Math.pow(1 + monthlyRate, -termMonths);

        //Cuota mensual
        return numerator / denominator;
    }

    //Genera el plan de pagos mensual (amortización) de un préstamo.
    private List<LoanInstallment> generatePaymentPlan(double amount, double monthlyRate, int termMonths) {
        //Calcular la cuota mensual total usando la fórmula de amortización
        double monthlyPayment = calculateLoanPayment(amount, monthlyRate, termMonths);
        double balance = amount;
        List<LoanInstallment> plan = new ArrayList<>();

        //Iterar mes a mes para calcular principal, intereses y balance restante
        for (int month = 1; month <= termMonths; month++) {
            // Pago de intereses del mes actual: saldo restante * tasa mensual
            double interestPayment = balance * monthlyRate;

            // Pago a capital: cuota mensual - intereses del mes
            double principalPayment = monthlyPayment - interestPayment;

            // Actualizar saldo restante
            balance -= principalPayment;

            //Para que el ultimo mes quede en 0
            if (month == termMonths) {
                principalPayment += balance;
                balance = 0;
            }

            //Agregar la cuota calculada a la lista de pago
            plan.add(new LoanInstallment(
                    month,
                    BigDecimal.valueOf(principalPayment),
                    BigDecimal.valueOf(interestPayment),
                    BigDecimal.valueOf(Math.max(balance, 0))
            ));
        }

        return plan;
    }
}
