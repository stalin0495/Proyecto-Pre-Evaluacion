package com.pichincha.account.application.exception;

import java.io.Serial;

public class BadRequestException extends IllegalArgumentException{
    @Serial
    private static final long serialVersionUID = 1L;
    public BadRequestException(String message) {
        super(message);
    }
}
