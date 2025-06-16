package com.fyrdev.monyia.domain.exception;

public class RequiredParamException extends RuntimeException {
    public RequiredParamException(String categoryNotFoundMessage) {
        super(categoryNotFoundMessage);
    }
}
