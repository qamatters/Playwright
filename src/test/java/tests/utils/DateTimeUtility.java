package tests.utils;

public class DateTimeUtility {

    public static String getCurrentDateTimeInSpecificFormat() {
       return java.time.LocalDateTime.now().toString().replace(":", "-").replace("T", "_").split("\\.")[0];
    }
}
