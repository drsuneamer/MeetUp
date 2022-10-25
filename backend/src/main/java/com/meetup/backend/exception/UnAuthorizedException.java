package com.meetup.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * created by seongmin on 2022/10/23
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
