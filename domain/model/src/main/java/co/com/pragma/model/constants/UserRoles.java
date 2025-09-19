package co.com.pragma.model.constants;

public class UserRoles {
    private UserRoles() {
        throw new UnsupportedOperationException(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    public static final String ADMIN = "ADMIN";
    public static final String ASESOR = "ASESOR";
    public static final String CLIENTE = "CLIENTE";
}
