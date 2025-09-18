package co.com.pragma.api.loan.handler;

import co.com.pragma.api.loan.dto.LoanListResponseDTO;
import co.com.pragma.api.loan.dto.PageResponseDTO;
import co.com.pragma.api.loan.mapper.LoanListResponseMapper;
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
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        List<RequestStatus> statuses = statusesParam.isBlank()
                ? List.of()
                : Arrays.stream(statusesParam.split(","))
                .map(String::trim)
                .map(RequestStatus::valueOf)
                .toList();

        // Flux de resultados paginados
        Flux<LoanListResponseDTO> loans = loanListUseCase.list(statuses, page, size).map(LoanListResponseMapper::toDto);

        // Count total para calcular paginación
        Mono<Long> totalCount = loanListUseCase.countByStatuses(statuses);

        return loans.collectList()
                .zipWith(totalCount)
                .flatMap(tuple -> {
                    var content = tuple.getT1();
                    var total = tuple.getT2();

                    PageResponseDTO<LoanListResponseDTO> response = PageResponseDTO.<LoanListResponseDTO>builder().content(content)
                            .page(page)
                            .size(size)
                            .total(total)
                            .totalPages((int) Math.ceil((double) total / size))
                            .build();

                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
    }
}
