package com.meetup.backend.dto.meetup;

import com.meetup.backend.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by seungyong on 2022/10/31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetupUserResponseDto {

    private List<UserInfoDto> userInfoDtoList;

    public static MeetupUserResponseDto of(List<UserInfoDto> userList) {
        return MeetupUserResponseDto.builder()
                .userInfoDtoList(userList)
                .build();
    }

}
