package com.meetup.backend.service.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.hook.IncomingWebhookClient;
import net.bis5.mattermost.model.IncomingWebhookRequest;
import net.bis5.mattermost.model.SlackAttachment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by seongmin on 2022/11/03
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookNoticeServiceImpl implements WebhookNoticeService {

    private final IncomingWebhookClient client;

    @Override
    public void firstLoginNotice(String nickname) {
        IncomingWebhookRequest payload = new IncomingWebhookRequest();
        StringBuilder text = new StringBuilder();
        if (nickname.contains("컨설턴트") || nickname.contains("교수") || nickname.contains("코치") || nickname.contains("프로")) {
            text.append("## 권한 변경 필요 \n");
        }
        text.append("#### ").append(nickname).append(" 첫 로그인 \n").append("https://meet-up.co.kr/admin-login");

        payload.setText(text.toString());
        client.postByIncomingWebhook(payload);
    }
}
