package co.com.pragma.api.loan;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.loan.mapper.LoanMapper;
import co.com.pragma.api.loan.util.LoanUtils;
import co.com.pragma.api.util.Utils;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.usecase.loan.LoanUseCase;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import jakarta.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanHandler {
    private final LoanUseCase loanUseCase;
    private final Validator validator;

    public Mono<ServerResponse> createLoan(ServerRequest request) {
        return request.bodyToMono(LoanDTO.class).flatMap(dto ->
            ValidationUtils.validate(dto, validator).switchIfEmpty(
                loanUseCase.register(LoanMapper.toMain(dto)).flatMap(savedLoanRequest -> {
                    ApiResponse<LoanRequest> response = ApiResponse.<LoanRequest>builder()
                        .code(Utils.CREATE_CODE).message(LoanUtils.CREATE_MESSAGE)
                        .data(savedLoanRequest).build();
                    return ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                })
            )
        ).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder()
                .code(Utils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }

    public Mono<ServerResponse> getAllLoanRequests(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(loanUseCase.getAllLoanRequests(), LoanRequest.class);
    }
}
