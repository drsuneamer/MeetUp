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

    @NotBlank
    private String teamId;

    @NotBlank
    private String displayName;

    @NotBlank
    private String name;

    @NotBlank
    private ChannelType type;

    List<String> inviteList;

}
