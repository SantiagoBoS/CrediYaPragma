package co.com.pragma.model.auth.exception;

import co.com.pragma.model.auth.constants.AppMessages;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(AppMessages messageEnum) {
        super(messageEnum.getMessage());
    }
}
