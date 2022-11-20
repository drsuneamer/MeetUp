package com.meetup.backend.util.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * created by myeongseok on 2022/10/23
 * updated by seongmin on 2022/11/17
 */

public class LocalDateUtil {
    public static LocalDateTime strToLDT(String str) {
        LocalDateTime formatLocalDateTime =
                LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return formatLocalDateTime;
    }

    public static String getDay(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN);
    }
}
