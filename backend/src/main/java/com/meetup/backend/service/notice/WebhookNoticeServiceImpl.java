package com.meetup.backend.service.notice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.hook.IncomingWebhookClient;
import net.bis5.mattermost.model.IncomingWebhookRequest;
import net.bis5.mattermost.model.SlackAttachment;
import net.bis5.mattermost.model.SlackAttachmentField;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * created by seongmin on 2022/11/03
 * updated by seongmin on 2022/11/04
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookNoticeServiceImpl implements WebhookNoticeService {

    private final IncomingWebhookClient client;
    private final String ICON = "https://a106mtld.s3.ap-northeast-2.amazonaws.com/logo_square.png";
    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void firstLoginNotice(String nickname) {
        if (profile.equals("local")) {
            log.info("local");
            return;
        }
        IncomingWebhookRequest payload = new IncomingWebhookRequest();
        List<SlackAttachment> slackAttachments = new ArrayList<>();
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setColor("#cc101f");

        if (nickname.contains("컨설턴트") || nickname.contains("교수") || nickname.contains("코치") || nickname.contains("프로")) {
            slackAttachment.setText("## :alert_siren: 권한 변경 필요 :alert_siren:");
        }

        SlackAttachmentField field = new SlackAttachmentField();
        field.setTitle(nickname + " 첫 로그인");
        field.setValue("https://meet-up.co.kr/admin-login");
        List<SlackAttachmentField> fields = new ArrayList<>();
        fields.add(field);
        slackAttachment.setFields(fields);
        slackAttachment.setFooter("MeetUp");
        slackAttachment.setFooterIcon(ICON);
        slackAttachments.add(slackAttachment);
        payload.setAttachments(slackAttachments);
        client.postByIncomingWebhook(payload);
    }
}
