package com.rent_a_car.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    // Calculates the number of days between two dates
    public static long getDaysBetween(Date startDate, Date endDate) {
        LocalDate start = convertToLocalDate(startDate);
        LocalDate end = convertToLocalDate(endDate);
        return ChronoUnit.DAYS.between(start, end);
    }

    // Calculates the number of weekend days between two dates
    public static long getWeekendDaysBetween(Date startDate, Date endDate) {
        LocalDate start = convertToLocalDate(startDate);
        LocalDate end = convertToLocalDate(endDate);
        long weekendDays = 0;

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendDays++;
            }
        }

        return weekendDays;
    }

    // Converts java.util.Date to java.time.LocalDate
    private static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
