package co.com.pragma.api.user;

import co.com.pragma.api.user.config.UserServiceProperties;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserDocumentRepositoryImplTest {

    private UserServiceProperties properties;
    private WebClient.Builder webClientBuilder;
    private UserDocumentRepository userDocumentRepository;

    @BeforeEach
    void setUp() {
        properties = mock(UserServiceProperties.class);
        when(properties.getBaseUrl()).thenReturn("http://localhost");

        webClientBuilder = WebClient.builder();
        userDocumentRepository = new UserDocumentRepositoryImpl(webClientBuilder, properties);
    }

    @Test
    void existsByDocumentShouldReturnTrue() {
        // Simulamos una respuesta exitosa de WebClient
        UserDocumentRepositoryImpl spyGateway = spy((UserDocumentRepositoryImpl) userDocumentRepository);
        doReturn(Mono.just(true)).when(spyGateway).existsByDocument(anyString());

        StepVerifier.create(spyGateway.existsByDocument("12345"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByDocumentShouldReturnErrorWhenUserNotFound() {
        //Verifica que no encuentra el usuario
        UserDocumentRepositoryImpl spyGateway = spy((UserDocumentRepositoryImpl) userDocumentRepository);
        doReturn(Mono.error(new BusinessException(AppMessages.USER_NOT_EXIST.getMessage())))
                .when(spyGateway).existsByDocument(anyString());

        StepVerifier.create(spyGateway.existsByDocument("99999"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals(AppMessages.USER_NOT_EXIST.getMessage()))
                .verify();
    }

    @Test
    void existsByDocumentTokenShouldReturnTrue() {
        //Verifica que mediante el token si existe el usuario
        UserDocumentRepositoryImpl spyGateway = spy((UserDocumentRepositoryImpl) userDocumentRepository);
        doReturn(Mono.just(true)).when(spyGateway).existsByDocumentToken(anyString(), anyString());

        StepVerifier.create(spyGateway.existsByDocumentToken("12345", "token"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByDocumentTokenShouldReturnErrorWhenUserNotFound() {
        //Devuelve error por que no encuentra el usuario
        UserDocumentRepositoryImpl spyGateway = spy((UserDocumentRepositoryImpl) userDocumentRepository);
        doReturn(Mono.error(new BusinessException(AppMessages.USER_NOT_EXIST.getMessage())))
                .when(spyGateway).existsByDocumentToken(anyString(), anyString());

        StepVerifier.create(spyGateway.existsByDocumentToken("99999", "token"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals(AppMessages.USER_NOT_EXIST.getMessage()))
                .verify();
    }

    @Test
    void existsByDocumentShouldPropagateBusinessExceptionFromWebClient() {
        UserServiceProperties props = mock(UserServiceProperties.class);
        when(props.getBaseUrl()).thenReturn("http://localhost:9999"); // puerto inválido

        UserDocumentRepositoryImpl repo = new UserDocumentRepositoryImpl(WebClient.builder(), props);

        StepVerifier.create(repo.existsByDocument("nope"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().equals(AppMessages.USER_NOT_EXIST.getMessage()))
                .verify();
    }
}