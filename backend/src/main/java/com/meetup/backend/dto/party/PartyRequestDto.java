package com.meetup.backend.dto.party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * created by seongmin on 2022/11/08
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartyRequestDto {

    @NotBlank(message = "그룹 이름은 필수 입력값입니다.")
    private String name;

    private List<String> members;
}
