package com.meetup.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seongmin on 2022/10/25
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionEnum {

    USER_NOT_FOUND(BAD_REQUEST, "40001", "사용자를 찾을 수 없습니다."),
    BAD_REQUEST_LOGOUT(BAD_REQUEST, "40002","잘못된 로그아웃 접근입니다."),

    EMPTY_CREDENTIAL(UNAUTHORIZED, "40101", "인증 정보가 없습니다."),

    ACCESS_DENIED(FORBIDDEN, "40301","권한이 없습니다."),

    DUPLICATE_NICKNAME(CONFLICT, "40901", "닉네임이 중복됩니다."),
    MATTERMOST_EXCEPTION(INTERNAL_SERVER_ERROR, "50001", "매터모스트 에러, 잠시 후 다시 시도해주세요.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
