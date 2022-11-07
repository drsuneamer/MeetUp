package com.meetup.backend.dto.team;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * created by seungyong on 2022/11/06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamActivateRequestDto {

    @NotBlank
    private String teamId;

}
