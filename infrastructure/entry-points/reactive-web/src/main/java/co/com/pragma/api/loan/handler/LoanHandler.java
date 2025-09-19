package co.com.pragma.api.loan.handler;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.loan.mapper.LoanMapper;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.model.constants.ErrorCode;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.gateways.UserGateway;
import co.com.pragma.usecase.loan.LoanUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
    private final UserGateway userGateway;

    public Mono<ServerResponse> createLoan(ServerRequest request) {
        String token = request.headers().firstHeader("Authorization");
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(securityContext -> {
                String clientDocumentFromJwt = (String) securityContext.getAuthentication().getCredentials();
                return request.bodyToMono(LoanDTO.class)
                    .flatMap(dto -> validateClientDocument(dto.getClientDocument(), clientDocumentFromJwt)
                        .then(ValidationUtils.validate(dto, validator))
                        .switchIfEmpty(loanUseCase.register(LoanMapper.toMain(dto), token)
                            .flatMap(savedLoanRequest -> ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(ApiResponse.<LoanRequest>builder()
                                    .code(ErrorCode.LOAN_CREATED.getBusinessCode()).message(AppMessages.LOAN_CREATED.getMessage())
                                    .data(savedLoanRequest).build()
                                )
                            )
                        )
                    );
            });
    }

    private Mono<Void> validateClientDocument(String dtoDocument, String jwtDocument) {
        if (!dtoDocument.equals(jwtDocument)) {
            return Mono.error(new BusinessException(
                    AppMessages.LOAN_CANNOT_BE_CREATED_FOR_ANOTHER_USER.getMessage()
            ));
        }
        return Mono.empty();
    }
}
