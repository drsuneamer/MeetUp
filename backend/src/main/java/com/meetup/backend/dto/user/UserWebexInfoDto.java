package com.meetup.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserWebexInfoDto {

    @NotBlank(message = "웹엑스 주소는 필수 입력 값입니다.")
    private String webexUrl;

    public static UserWebexInfoDto of(String webexUrl) {
        return new UserWebexInfoDto(webexUrl);
    }

}
