package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoanUpdateUseCaseTest {

    private LoanUpdateRepository loanUpdateRepository;
    private NotificationServiceGateway notificationServiceGateway;
    private UserRepository userRepository;
    private LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;
    private LoanUpdateUseCase useCase;
    private UUID publicId;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        loanUpdateRepository = Mockito.mock(LoanUpdateRepository.class);
        notificationServiceGateway = Mockito.mock(NotificationServiceGateway.class);
        userRepository = Mockito.mock(UserRepository.class);
        useCase = new LoanUpdateUseCase(loanUpdateRepository, notificationServiceGateway, userRepository, loanCalculateCapacityUseCase);

        publicId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        loanRequest = LoanRequest.builder()
                .publicId(publicId)
                .clientDocument("CC123")
                .loanType("PERSONAL")
                .build();
    }

    @Test
    void shouldSendNotificationWithLoanTypeWhenLoanIsApproved() {
        //Para validar el envio de una notificacion con el tipo de prestamo cuando se apruebe
        loanRequest.setLoanType("CAR");

        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString())).thenReturn(Mono.just(loanRequest));
        when(userRepository.findByDocumentNumber("CC123")).thenReturn(Mono.just(User.builder().email("test@mail.com").build()));
        when(notificationServiceGateway.sendLoanStatusUpdateNotification(anyString(), anyString(), anyString(), anyString())).thenReturn(Mono.empty());

        Mono<LoanRequest> result = useCase.updateLoanStatus(publicId.toString(), "APPROVED", "advisor-1");

        StepVerifier.create(result)
                .expectNextMatches(lr -> lr.getLoanType().equals("CAR"))
                .verifyComplete();

        verify(notificationServiceGateway, times(1))
                .sendLoanStatusUpdateNotification(publicId.toString(), "APPROVED", "test@mail.com", "CAR");
    }

    @Test
    void shouldNotSendNotificationWhenStatusIsPending() {
        //Para que no se envie alguna notificacion con otro estado
        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString())).thenReturn(Mono.just(loanRequest));
        Mono<LoanRequest> result = useCase.updateLoanStatus("123", "PENDING_REVIEW", "advisor-1");

        StepVerifier.create(result)
                .expectNext(loanRequest)
                .verifyComplete();

        verify(notificationServiceGateway, never()).sendLoanStatusUpdateNotification(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void shouldReturnErrorWhenUserNotFound() {
        //Por si el usuario no se encuentra
        loanRequest.setLoanType("MORTGAGE");

        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString())).thenReturn(Mono.just(loanRequest));
        when(userRepository.findByDocumentNumber("CC123")).thenReturn(Mono.empty());
        Mono<LoanRequest> result = useCase.updateLoanStatus(publicId.toString(), "APPROVED", "advisor-1");
        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof RuntimeException
                                && error.getMessage().contains("LOAN_EMAIL_NOT_FOUND"))
                .verify();

        verify(notificationServiceGateway, never()).sendLoanStatusUpdateNotification(anyString(), anyString(), anyString(), anyString());
    }
}
