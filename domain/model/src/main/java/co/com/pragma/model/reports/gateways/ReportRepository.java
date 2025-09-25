package co.com.pragma.model.reports.gateways;

import reactor.core.publisher.Mono;

public interface ReportRepository {
    Mono<Long> getTotalApprovedLoans();
    Mono<Void> incrementCounter(); // Para actualizar contador desde SQS
}