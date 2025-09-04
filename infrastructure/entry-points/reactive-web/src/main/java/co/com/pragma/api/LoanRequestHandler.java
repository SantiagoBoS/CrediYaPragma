package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.LoanRequestDTO;
import co.com.pragma.api.exception.LoanRequestUtils;
import co.com.pragma.api.exception.ValidationErrorHandler;
import co.com.pragma.api.mapper.LoanRequestMapper;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.usecase.loan.LoanUseCase;
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
public class LoanRequestHandler {
    private final LoanUseCase loanRequestUseCase;
    private final Validator validator;

    public Mono<ServerResponse> createLoanRequest(ServerRequest request) {
        return request.bodyToMono(LoanRequestDTO.class).flatMap(dto -> {
            var violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ValidationErrorHandler.buildValidationErrorResponse(violations);
            }
            LoanRequest loan = LoanRequestMapper.toEntity(dto);
            return loanRequestUseCase.register(loan).flatMap(savedLoanRequest ->{
                ApiResponse<LoanRequest> response = ApiResponse.<LoanRequest>builder().code(LoanRequestUtils.CREATE_CODE).message(LoanRequestUtils.CREATE_MESSAGE).data(savedLoanRequest).build();
                return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            });
        }).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder().code(LoanRequestUtils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }

    public Mono<ServerResponse> getAllLoanRequests(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(loanRequestUseCase.getAllLoanRequests(), LoanRequest.class);
    }
}
