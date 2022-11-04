package com.meetup.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * created by seongmin on 2022/10/25
 * updated by seongmin on 2022/11/04
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionEnum {

    USER_NOT_FOUND(BAD_REQUEST, "40001", "사용자를 찾을 수 없습니다."),
    BAD_REQUEST_LOGOUT(BAD_REQUEST, "40002", "잘못된 로그아웃 접근입니다."),

    TEAM_NOT_FOUND(BAD_REQUEST, "40003", "해당 팀을 찾을 수 없습니다."),

    CHANNEL_NOT_FOUND(BAD_REQUEST, "40004", "해당 채널을 찾을 수 없습니다."),

    MEETUP_NOT_FOUND(BAD_REQUEST, "40005", "해당 밋업을 찾을 수 없습니다."),

    SCHEDULE_NOT_FOUND(BAD_REQUEST, "40006", "해당 스케줄(개인일정)을 찾을 수 없습니다."),

    MEETING_NOT_FOUND(BAD_REQUEST, "40007", "해당 미팅(밋업 신청 스케쥴)을 찾을 수 없습니다."),

    KEY_NOT_MATCHING(BAD_REQUEST, "40008", "키 값이 일치하지 않습니다."),

    DATE_FORMAT_EX(BAD_REQUEST, "40009", "date의 형식은 yyyy-MM-dd HH:mm:ss 입니다."),

    EMPTY_CREDENTIAL(UNAUTHORIZED, "40101", "인증 정보가 없습니다."),

    EMPTY_MM_CREDENTIAL(UNAUTHORIZED, "40101", "메터모스트 인증 정보가 없습니다."),

    ACCESS_DENIED(FORBIDDEN, "40301", "권한이 없습니다."),

    ACCESS_DENIED_THIS_SCHEDULE(FORBIDDEN, "40302", "해당 스케줄을 볼 권한이 없습니다."),

    ID_PWD_NOT_MATCHING(FORBIDDEN, "40303", "아이디 또는 패스워드가 일치하지 않습니다."),

    ADMIN_ACCESS_DENIED(FORBIDDEN, "40304", "관리자 권한이 없습니다. 관리자만 이용할 수 있습니다."),

    CHANNEL_ACCESS_DENIED(FORBIDDEN, "40305", "해당 채널에 대한 권한이 없습니다."),

    MEETUP_ACCESS_DENIED(FORBIDDEN, "40306", "해당 밋업 작업에 대한 권한이 없습니다."),

    DUPLICATE_NICKNAME(CONFLICT, "40901", "닉네임이 중복됩니다."),

    DUPLICATE_ID(CONFLICT, "40902", "아이디가 중복됩니다."),
    DUPLICATE_INSERT_DATETIME(CONFLICT, "40903", "기존에 있는 일정과 중복되어 등록이 불가합니다."),

    DUPLICATE_UPDATE_DATETIME(CONFLICT, "40904", "기존에 있는 일정과 중복되어 수정이 불가합니다."),

    MATTERMOST_EXCEPTION(INTERNAL_SERVER_ERROR, "50001", "매터모스트 에러, 잠시 후 다시 시도해주세요.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
