package co.com.pragma.model.exceptions;

import co.com.pragma.model.constants.AppMessages;

public class BusinessException  extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(AppMessages messageEnum) {
        super(messageEnum.getMessage());
    }
}
