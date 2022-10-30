package com.meetup.backend.dto.admin;

import com.meetup.backend.entity.user.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * created by seungyong on 2022/10/31
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeRoleDto {
    private String id;
    private RoleType roleType;
}
