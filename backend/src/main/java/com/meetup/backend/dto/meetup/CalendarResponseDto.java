package com.meetup.backend.dto.meetup;

import com.meetup.backend.entity.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by seungyong on 2022/11/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarResponseDto {

    @ApiModelProperty(example = "관리자 ID")
    private String id;

    @ApiModelProperty(example = "관리자 이름")
    private String userName;

    public static CalendarResponseDto of(User user){
        return CalendarResponseDto.builder()
                .id(user.getId())
                .userName(user.getNickname())
                .build();
    }


}
