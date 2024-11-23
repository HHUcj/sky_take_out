package com.sky.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ClassName: TimeUtils
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/21 - 下午6:38
 * @Version: v1.0
 */
public class TimeUtils {
    public static LocalDateTime parseLocalDateTime(String date) {
        if (date == null) {
            return null;
        } else {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
