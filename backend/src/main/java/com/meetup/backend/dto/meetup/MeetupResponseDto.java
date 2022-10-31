package com.meetup.backend.dto.meetup;

import com.meetup.backend.entity.meetup.Meetup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetupResponseDto {
    private Long id;

    private String title;

    private String color;

    public static MeetupResponseDto of(Meetup meetup) {
        return MeetupResponseDto.builder()
                .id(meetup.getId())
                .title(meetup.getTitle())
                .color(meetup.getColor())
                .build();
    }

}
