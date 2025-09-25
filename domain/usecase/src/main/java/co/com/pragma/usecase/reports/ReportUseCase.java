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
        return reportRepository.getTotalApprovedLoans()
                .map(ReportResponse::new)
                .onErrorResume(e -> Mono.just(new ReportResponse(0L)));
    }
}
