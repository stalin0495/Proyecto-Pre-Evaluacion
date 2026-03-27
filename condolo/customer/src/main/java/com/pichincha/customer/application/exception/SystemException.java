package com.pichincha.customer.application.exception;

import java.io.Serial;

public class SystemException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
