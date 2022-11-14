package com.meetup.backend.dto.channel;

import com.meetup.backend.entity.channel.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * created by seungyong on 2022/11/07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelCreateRequestDto {

    @NotBlank(message = "팀 아이디는 필수 입력 값입니다.")
    private String teamId;

    @NotBlank(message = "displayName은 필수 입력 값입니다.")
    private String displayName;

    @NotBlank(message = "팀 name은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "채널 타입은 필수 입력 값입니다.")
    private ChannelType type;

    private List<String> inviteList;

}
