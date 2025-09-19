package co.com.pragma.api.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class AuthRouterTest {

    private AuthRouter authRouter;
    private AuthHandler authHandler;

    @BeforeEach
    void setUp() {
        authRouter = new AuthRouter();
        authHandler = mock(AuthHandler.class);
    }

    @Test
    void loginRouteShouldReturnRouterFunction() {
        RouterFunction<ServerResponse> routerFunction = authRouter.loginRoute(authHandler);
        assertNotNull(routerFunction, "RouterFunction should not be null");
    }
}