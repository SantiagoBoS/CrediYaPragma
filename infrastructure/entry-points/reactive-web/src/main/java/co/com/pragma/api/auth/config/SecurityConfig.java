package co.com.pragma.api.auth.config;

import co.com.pragma.api.auth.filter.JwtAuthenticationWebFilter;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.constants.UserRoles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthenticationWebFilter jwtFilter) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                .pathMatchers(ApiPaths.AUTH_BASE).permitAll()
                .pathMatchers(HttpMethod.GET, ApiPaths.USER_BASE + "/**").authenticated()
                .pathMatchers(HttpMethod.POST, ApiPaths.USER_BASE + "/**").hasAnyRole(UserRoles.ADMIN, UserRoles.ASESOR)
                .pathMatchers(HttpMethod.POST, ApiPaths.LOAN_BASE + "/**").hasRole(UserRoles.CLIENTE)
                .anyExchange().authenticated()
            )
            // Agrega el filtro JWT
            .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }
}
