package co.com.pragma.api.loan.handler;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.loan.dto.LoanStatusUpdateDTO;
import co.com.pragma.api.loan.mapper.LoanMapper;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.constants.ErrorCode;
import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanUpdateHandler {
    private final LoanUpdateUseCase loanUpdateUseCase;
    private final Validator validator;

    public Mono<ServerResponse> updateLoanStatus(ServerRequest request) {
        String publicId = request.pathVariable("publicId");

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    // Extraer advisorId del token JWT
                    String advisorId = (String) securityContext.getAuthentication().getPrincipal();

                    return request.bodyToMono(LoanStatusUpdateDTO.class)
                            .flatMap(dto -> ValidationUtils.validate(dto, validator)
                                    .switchIfEmpty(loanUpdateUseCase.updateLoanStatus(publicId, dto.getStatus(), advisorId)
                                            .flatMap(updatedLoan -> buildSuccessResponse(updatedLoan))
                                    )
                            );
                })
                .onErrorResume(Exception.class, this::buildErrorResponse);
    }

    private Mono<ServerResponse> buildSuccessResponse(LoanRequest updatedLoan) {
        ApiResponse<LoanRequest> response = ApiResponse.<LoanRequest>builder()
                .code(ErrorCode.LOAN_SUCCESS.getBusinessCode())
                .message(AppMessages.LOAN_STATUS_UPDATED.getMessage())
                .data(updatedLoan)
                .build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

    private Mono<ServerResponse> buildErrorResponse(Throwable error) {
        ApiResponse<Object> response = ApiResponse.builder()
                .code(ErrorCode.VALIDATION_ERROR.getBusinessCode())
                .message(error.getMessage())
                .build();

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
