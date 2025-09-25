package co.com.pragma.api.reports;

import co.com.pragma.model.reports.ReportResponse;
import co.com.pragma.usecase.reports.ReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReportHandler {
    private final ReportUseCase reportUseCase;

    public Mono<ServerResponse> getTotalApprovedLoans(ServerRequest request) {
        return reportUseCase.getTotalApprovedLoans()
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(e -> ServerResponse.ok().bodyValue(new ReportResponse(0L, 0.0)));
    }
}
