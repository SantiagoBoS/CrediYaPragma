package co.com.pragma.usecase.reports;

import co.com.pragma.model.reports.ReportResponse;
import co.com.pragma.model.reports.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReportUseCase {

    private final ReportRepository reportRepository;

    // Obtener total de préstamos aprobados
    public Mono<ReportResponse> getTotalApprovedLoans() {
        Mono<Long> totalLoans = reportRepository.getTotalApprovedLoans();
        Mono<Double> totalAmount = reportRepository.getTotalApprovedAmount();

        return Mono.zip(totalLoans, totalAmount)
                .map(tuple -> new ReportResponse(tuple.getT1(), tuple.getT2()))
                .onErrorResume(e -> Mono.just(new ReportResponse(0L, 0.0)));
    }
}
