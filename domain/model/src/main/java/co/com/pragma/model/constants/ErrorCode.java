package co.com.pragma.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //USER
    USER_SUCCESS("US200.00"),
    USER_CREATED("US201.01"),
    USER_VALIDATION_ERROR("US400.01"),
    USER_GENERAL_VALIDATION_ERROR("US400.02"),

    //LOAN
    LOAN_CREATED("LN201.01"),
    LOAN_VALIDATION_ERROR("LN400.01"),
    LOAN_GENERAL_VALIDATION_ERROR("LN400.02"),
    LOAN_SUCCESS("LN200.00"),

    //AUTH
    AUTH_CREATE_CODE("AU201.01"),
    AUTH_GENERAL_VALIDATION_ERROR("AU400.02"),

    //COMMON
    INTERNAL_SERVER_ERROR("CO500.01"),
    VALIDATION_ERROR("CO400.01"),
    GENERAL_VALIDATION_ERROR("CO400.02"),
    CONFLICT_CODE("CO409.01");

    private final String businessCode;
}
