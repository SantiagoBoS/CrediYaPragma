package co.com.pragma.model.reports.gateways;

import reactor.core.publisher.Mono;

public interface ReportRepository {
    //Count APPROVED
    Mono<Long> getTotalApprovedLoans();
    Mono<Void> incrementCounter(); //Contador desde SQS

    //TOTAL APPROVED
    Mono<Void> addApprovedAmount(Double amount);
    Mono<Double> getTotalApprovedAmount();
}