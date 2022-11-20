package com.meetup.backend.config;

import net.bis5.mattermost.client4.hook.IncomingWebhookClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by seongmin on 2022/11/03
 */
@Configuration
public class IncomingWebhookClientConfig {

    @Bean
    public IncomingWebhookClient getIncomingWebhookClient() {
        return new IncomingWebhookClient("https://meeting.ssafy.com/hooks/ow4dpfgun7ntbdwgfdsea64o6e");
    }
}
