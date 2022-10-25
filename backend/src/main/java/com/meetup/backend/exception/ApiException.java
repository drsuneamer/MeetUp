package com.meetup.backend.exception;

import lombok.Getter;

/**
 * created by seongmin on 2022/10/25
 */
@Getter
public class ApiException extends RuntimeException {

    ExceptionEnum error;

    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
