package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.LoanRequestDTO;
import co.com.pragma.api.exception.LoanRequestUtils;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.usecase.registerloanrequest.RegisterLoanRequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import jakarta.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanRequestHandler {
    private final RegisterLoanRequestUseCase loanRequestUseCase;
    private final Validator validator;

    public Mono<ServerResponse> createLoanRequest(ServerRequest request) {
        return request.bodyToMono(LoanRequestDTO.class).flatMap(dto -> {
            var violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                List<Map<String, String>> errors = violations.stream()
                    .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", violation.getMessage()))
                    .collect(Collectors.toList());
                ApiResponse<Object> response = ApiResponse.builder().code(LoanRequestUtils.VALIDATION_CODE).message(LoanRequestUtils.VALIDATION_MESSAGE).errors(errors).build();
                return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            }

            LoanRequest requestEntity = LoanRequest.builder()
                    .clientDocument(dto.getClientDocument())
                    .amount(BigDecimal.valueOf(dto.getAmount()))
                    .termMonths(dto.getTermMonths())
                    .loanType(dto.getLoanType())
                    .status(dto.getStatus())
                    .createdAt(dto.getCreatedAt())
                    .build();

            return loanRequestUseCase.registerLoanRequest(requestEntity).flatMap(savedLoanRequest ->{
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
