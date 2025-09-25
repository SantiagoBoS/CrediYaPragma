package co.com.pragma.model.constants;

public final class ApiPaths {
    //USER
    public static final String USER_BASE = "/api/v1/usuarios";
    public static final String USER_BY_DOCUMENT = USER_BASE + "/{documentNumber}";

    //LOAN
    public static final String LOAN_BASE = "/api/v1/solicitud";

    //AUTH
    public static final String AUTH_BASE = "/api/v1/login";

    //CAPACITY
    public static final String CAPACITY_BASE = "api/v1/calcular-capacidad";

    //REPORTS
    public static final String REPORT_BASE = "/api/v1/reportes";
}
