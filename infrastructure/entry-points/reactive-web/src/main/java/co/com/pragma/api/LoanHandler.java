package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.LoanDTO;
import co.com.pragma.api.exception.LoanUtils;
import co.com.pragma.api.exception.ValidationErrorHandler;
import co.com.pragma.api.mapper.LoanMapper;
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
public class LoanHandler {
    private final LoanUseCase loanUseCase;
    private final Validator validator;

    public Mono<ServerResponse> createLoanRequest(ServerRequest request) {
        return request.bodyToMono(LoanDTO.class).flatMap(dto -> {
            //CLASE UTIL para que quede en una sola linea
            var violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ValidationErrorHandler.buildValidationErrorResponse(violations);
            }
            //toEntity para toMain
            LoanRequest loan = LoanMapper.toMain(dto);
            //RESPUESTA DEL FLUJO EL .MAP
            return loanUseCase.register(loan).flatMap(savedLoanRequest ->{
                ApiResponse<LoanRequest> response = ApiResponse.<LoanRequest>builder().code(LoanUtils.CREATE_CODE).message(LoanUtils.CREATE_MESSAGE).data(savedLoanRequest).build();
                return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            });
        }).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder().code(LoanUtils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }

    public Mono<ServerResponse> getAllLoanRequests(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(loanUseCase.getAllLoanRequests(), LoanRequest.class);
    }
}
