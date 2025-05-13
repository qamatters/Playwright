package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    static int test_number = 0;
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[yyyy/MM/dd] HH:mm:ss");

    /**
     * Increments curent test number by 1.
     */
    public static void increment_test_number(){
        
        test_number ++;
    }

    /**
     * Logs a message from the test along with a timestamp and the current test number.
     * @param s The message to be logged.
     */
    public static void log(String s){
        LocalDateTime now = LocalDateTime.now();
        System.out.print(dtf.format(now));
        System.out.print(String.format(" -- Test Number %s -- ",test_number));
        System.out.println(s);
    }
}
