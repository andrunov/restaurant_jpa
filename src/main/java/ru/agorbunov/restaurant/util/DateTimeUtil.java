package ru.agorbunov.restaurant.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for actions with LocalDate, LocalTime and LocalDateTime classes
 */
public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateTimeUtil() {
    }

    /*check that value is between start and end*/
    public static <T extends Comparable<? super T>> boolean isBetween(T value, T start, T end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    /*return toString-performance of LocalDateTime according DATE_TIME_FORMATTER*/
    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    /*parsing string-performance of LocalDate to LocalDate*/
    public static LocalDate parseLocalDate(String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    /*parsing string-performance of LocalTime to LocalTime*/
    public static LocalTime parseLocalTime(String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    /*parsing string-performance of LocalDateTime to LocalDateTime according DATE_TIME_FORMATTER*/
    public static LocalDateTime parseLocalDateTime(String str) {
        return parseLocalDateTime(str, DATE_TIME_FORMATTER);
    }

    /*parsing string-performance of LocalDateTime to LocalDateTime according DateTimeFormatter pass in parameters*/
    public static LocalDateTime parseLocalDateTime(String str, DateTimeFormatter formatter) {
        return StringUtils.isEmpty(str) ? LocalDateTime.now() : LocalDateTime.parse(str, formatter);
    }
}
