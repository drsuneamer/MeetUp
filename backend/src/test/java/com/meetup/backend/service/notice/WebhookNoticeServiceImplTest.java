package com.meetup.backend.service.notice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * created by seongmin on 2022/11/03
 */
@SpringBootTest
class WebhookNoticeServiceImplTest {
    @Autowired
    private WebhookNoticeService webhookNoticeService;

    @Test
    @DisplayName("첫 로그인 웹훅 알림")
    void firstLoginNotice() {
        webhookNoticeService.firstLoginNotice("OOO 컨설턴트");
    }
}