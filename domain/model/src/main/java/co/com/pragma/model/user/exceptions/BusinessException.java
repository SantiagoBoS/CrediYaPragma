package co.com.pragma.model.user.exceptions;

import co.com.pragma.model.user.constants.AppMessages;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(AppMessages message) {
        super(message.getMessage());
    }
}
