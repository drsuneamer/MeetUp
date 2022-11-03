package com.meetup.backend.service.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.hook.IncomingWebhookClient;
import net.bis5.mattermost.model.IncomingWebhookRequest;
import net.bis5.mattermost.model.SlackAttachment;
import net.bis5.mattermost.model.SlackAttachmentField;
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
        List<SlackAttachment> slackAttachments = new ArrayList<>();
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setColor("#cc101f");

        if (nickname.contains("컨설턴트") || nickname.contains("교수") || nickname.contains("코치") || nickname.contains("프로")) {
            slackAttachment.setText("## 권한 변경 필요");
        }

        SlackAttachmentField field = new SlackAttachmentField();
        field.setTitle(nickname+" 첫 로그인");
        field.setValue("https://meet-up.co.kr/admin-login");
        List<SlackAttachmentField> fields = new ArrayList<>();
        fields.add(field);
        slackAttachment.setFields(fields);
        slackAttachments.add(slackAttachment);
        payload.setAttachments(slackAttachments);
        client.postByIncomingWebhook(payload);
    }
}
