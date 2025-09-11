package co.com.pragma.model.constants;

public final class ApiPaths {
    private ApiPaths() {
        throw new UnsupportedOperationException(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    //USER
    public static final String USER_BASE = "/api/v1/usuarios";
    public static final String USER_BY_DOCUMENT = USER_BASE + "/{documentNumber}";

    //LOAN
    public static final String LOAN_BASE = "/api/v1/solicitud";

    //AUTH
    public static final String AUTH_BASE = "/api/v1/login";
}
