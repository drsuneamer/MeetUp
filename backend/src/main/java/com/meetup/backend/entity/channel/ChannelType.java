package com.meetup.backend.entity.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * created by myeongseok on 2022/10/20
 * updated by myeongseok on 2022/10/20
 */
@Getter
@AllArgsConstructor
public enum ChannelType {
    Open("O"), Private("P"), Direct("D"), Group("G");

    private final String code;

    public static ChannelType of(String code) {
        for (ChannelType channelType : ChannelType.values()) {
            if (channelType.getCode().equals(code)) {
                return channelType;
            }
        }
        return ChannelType.Private;
    }

}
