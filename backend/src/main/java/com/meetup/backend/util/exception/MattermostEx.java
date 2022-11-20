package com.meetup.backend.util.exception;

import com.meetup.backend.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import static com.meetup.backend.exception.ExceptionEnum.*;

/**
 * created by seongmin on 2022/11/04
 * updated by seongmin on 2022/11/08
 */
@Slf4j
public class MattermostEx {
    public static void apiException(int status) {
        if (status == 400) {
            log.error("잘못된 요청입니다 status = {}", status);
            throw new ApiException(MM_BAD_REQUEST);
        } else if (status == 401) {
            log.error("인증정보가 없습니다 status = {}", status);
            throw new ApiException(EMPTY_MM_CREDENTIAL);
        } else if (status == 403) {
            log.error("권한이 없습니다 status = {}", status);
            throw new ApiException(MM_FORBIDDEN);
        }
    }
}
