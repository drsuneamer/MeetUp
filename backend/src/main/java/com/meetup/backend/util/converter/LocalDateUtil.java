package com.meetup.backend.util.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * created by myeongseok on 2022/10/23
 */

public class LocalDateUtil {
    public static LocalDateTime strToLDT(String str) {
        LocalDateTime formatLocalDateTime =
                LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return formatLocalDateTime;
    }
}
