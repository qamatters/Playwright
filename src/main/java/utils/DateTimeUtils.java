package utils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    // Returns current date as a string in given format
    public static String getCurrentDate(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now().format(formatter);
    }

    // Returns current time as a string in given format
    public static String getCurrentTime(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalTime.now().format(formatter);
    }

    // Returns current datetime as a string in given format
    public static String getCurrentDateTime(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter);
    }

    // Adds days to current date and returns formatted date
    public static String getDateAfterDays(int days, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now().plusDays(days).format(formatter);
    }

    // Subtracts days from current date and returns formatted date
    public static String getDateBeforeDays(int days, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now().minusDays(days).format(formatter);
    }

    // Parses a date string into LocalDate
    public static LocalDate parseDate(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, formatter);
    }

    // Converts java.util.Date to LocalDateTime
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    // Returns current timestamp in default format
    public static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String getCurrentYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        return LocalDate.now().format(formatter);
    }

    public static String getCurrentMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return LocalDate.now().format(formatter);
    }

    public static String getCurrentDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return LocalDate.now().format(formatter);
    }

    public static String returnAFutureDateAfterDays(int days, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now().plusDays(days).format(formatter);
    }

    public static String returnAPastDateBeforeDays(int days, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.now().minusDays(days).format(formatter);
    }


    /**
        * Returns the current date as a string formatted according to the specified pattern.
        *
        * @param format the date format pattern (e.g., "yyyy-MM-dd", "MM/dd/yyyy").
        *               The pattern must follow the rules of {@link java.time.format.DateTimeFormatter}.
        * @return the current date as a formatted string.
        * @throws IllegalArgumentException if the provided format is invalid.
        */
       public static String getCurrentDateInFormat(String format) {
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
           return LocalDate.now().format(formatter);
       }



}