package com.votingsystem.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Paz1506
 * Вспомогательный класс для
 * работы с датой/временем.
 * //TODO дублирует форматтер
 */

public class DateTimeUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    // DataBase doesn't support LocalDate.MIN/MAX
    public static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public static final LocalDate MAX_DATE = LocalDate.of(2050, 12, 30);
    public static final LocalTime MIN_TIME = LocalTime.of(0, 0, 0);
    public static final LocalTime MAX_TIME = LocalTime.of(23, 59, 59);
    public static final LocalDateTime BEGIN_CURRENT_DAY = LocalDateTime.of(LocalDate.now(), MIN_TIME);
    public static final LocalDateTime END_CURRENT_DAY = LocalDateTime.of(LocalDate.now(), MAX_TIME);
    public final static LocalTime tresholdTime = LocalTime.of(11, 0, 0);

    private DateTimeUtil() {
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseLocalDate(String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static LocalTime parseLocalTime(String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static LocalDateTime parseLocalDateTime(String str) {
        return StringUtils.isEmpty(str) ? null : LocalDateTime.parse(str);
    }

    public static LocalDateTime safeBeginCurrentDay(LocalDate startDate, LocalTime startTime) {
        return LocalDateTime.of(startDate != null ? startDate : LocalDate.now(), startTime != null ? startTime : LocalTime.of(0, 0, 0));
    }

    public static LocalDateTime safeEndCurrentDay(LocalDate startDate, LocalTime startTime) {
        return LocalDateTime.of(startDate != null ? startDate : LocalDate.now(), startTime != null ? startTime : LocalTime.of(23, 59, 59));
    }

}
