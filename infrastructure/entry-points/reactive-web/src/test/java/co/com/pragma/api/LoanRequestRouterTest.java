package co.com.pragma.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoanRequestRouterTest {

    private LoanRequestHandler handler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        handler = Mockito.mock(LoanRequestHandler.class);
        LoanRequestRouter router = new LoanRequestRouter();

        // construimos un WebTestClient solo con el router
        webTestClient = WebTestClient.bindToRouterFunction(router.loanRequestRoutes(handler))
                .configureClient()
                .baseUrl("/api/v1")
                .build();
    }

    @Test
    void shouldRouteToCreateLoanRequest() {
        // Verificar que la ruta POST /solicitud llama al handler
        when(handler.createLoanRequest(any())).thenReturn(ServerResponse.ok().bodyValue("created"));
        webTestClient.post()
                .uri("/solicitud")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("created");
    }

    @Test
    void shouldRouteToGetAllLoanRequests() {
        // Verificar que la ruta GET /solicitud llama al handler
        when(handler.getAllLoanRequests(any())).thenReturn(ServerResponse.ok().bodyValue("all"));
        webTestClient.get()
                .uri("/solicitud")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("all");
    }
}
