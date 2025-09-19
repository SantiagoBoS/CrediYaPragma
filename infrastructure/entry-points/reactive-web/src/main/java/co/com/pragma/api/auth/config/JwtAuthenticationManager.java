package co.com.pragma.api.auth.config;

import co.com.pragma.jwtprovider.JwtProviderAdapter;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    // Para la autenticación reactiva con JWT, covierte el token en una autenticación válida
    public final JwtProviderAdapter jwtProvider;

    public JwtAuthenticationManager(JwtProviderAdapter jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        //Validar el token JWT y extraer la información del usuario
        String token = authentication.getCredentials().toString();

        if (jwtProvider.validateToken(token)) {
            String document = jwtProvider.getDocumentNumber(token);
            String email = jwtProvider.getEmailFromToken(token);
            String role = jwtProvider.getRoleFromToken(token);

            return Mono.just(new UsernamePasswordAuthenticationToken(
                    email,
                    document,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
            ));
        } else {
            return Mono.empty();
        }
    }
}