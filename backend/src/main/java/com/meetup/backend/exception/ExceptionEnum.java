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
    BAD_REQUEST_LOGOUT(BAD_REQUEST, "40002", "잘못된 로그아웃 접근입니다."),

    TEAM_NOT_FOUND(BAD_REQUEST, "40003", "해당 팀을 찾을 수 없습니다."),

    CHANNEL_NOTFOUND(BAD_REQUEST, "40004", "해당 채널을 찾을 수 없습니다."),

    MEETUP_NOT_FOUND(BAD_REQUEST, "40005", "해당 밋업을 찾을 수 없습니다."),

    SCHEDULE_NOT_FOUND(BAD_REQUEST, "40006", "해당 스케줄(개인일정)을 찾을 수 없습니다."),

    MEETING_NOT_FOUND(BAD_REQUEST, "40007", "해당 미팅(밋업 신청 스케쥴)을 찾을 수 없습니다."),

    EMPTY_CREDENTIAL(UNAUTHORIZED, "40108", "인증 정보가 없습니다."),

    ACCESS_DENIED(FORBIDDEN, "40301", "권한이 없습니다."),

    DUPLICATE_NICKNAME(CONFLICT, "40901", "닉네임이 중복됩니다."),
    MATTERMOST_EXCEPTION(INTERNAL_SERVER_ERROR, "50001", "매터모스트 에러, 잠시 후 다시 시도해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
