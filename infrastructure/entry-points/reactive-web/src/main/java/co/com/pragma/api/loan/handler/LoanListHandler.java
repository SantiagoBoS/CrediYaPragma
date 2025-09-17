package co.com.pragma.api.loan.handler;

import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.usecase.loanlist.LoanListUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanListHandler {
    private final LoanListUseCase loanListUseCase;

    public Mono<ServerResponse> getLoanList(ServerRequest request) {
        String statusesParam = request.queryParam("statuses").orElse("");
        Integer page = Integer.parseInt(request.queryParam("page").orElse("0"));
        Integer size = Integer.parseInt(request.queryParam("size").orElse("10"));

        List<RequestStatus> statuses = statusesParam.isBlank()
                ? List.of()
                : Arrays.stream(statusesParam.split(","))
                .map(String::trim)
                .map(RequestStatus::valueOf)
                .toList();

        Flux<LoanList> loans = loanListUseCase.list(statuses, page, size);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(loans, LoanList.class);
    }
}
