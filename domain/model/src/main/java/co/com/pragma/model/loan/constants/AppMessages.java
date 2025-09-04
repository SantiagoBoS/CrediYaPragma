package co.com.pragma.model.loan.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppMessages {
    APPLICATION_IN_PROCESS("El cliente ya tiene una solicitud en proceso"),
    DUPLICATE_APPLICATION("Ya existe una solicitud duplicada para este cliente"),
    INTERNAL_ERROR("Error interno al registrar solicitud");

    private final String message;
}
