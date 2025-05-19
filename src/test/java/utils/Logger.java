package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import utils.enums.*;
public class Logger {
    static int test_number = 0;
    static String timestamp = null;
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd [HH:mm:ss]");

    /**
     * Increments curent test number by 1.
     */
    public static void increment_test_number(){
        
        test_number ++;
    }

    /**
     * Logs a message to console and log file from the test.
     * @param s The message to be logged.
     * @param mode LogMode to be used All|Console|File|None.
     */
    public static void log(String s, LogMode mode){
        LocalDateTime now = LocalDateTime.now();
        if (timestamp == null){ // Ensure timestamp is set only once in a single lifecycle
            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(now);
        }
        if (mode == LogMode.All){
            System.out.println(s);
            FileLogger.log(s,timestamp);
        }
        else if (mode == LogMode.Console){
            System.out.println(s);
        }
        else if (mode == LogMode.File){
            FileLogger.log(s,timestamp);
        }
    }
    /**
     * Logs a message to console and log file from the test along with a timestamp and the current test number.
     * @param s The message to be logged.
     * @param mode LogMode to be used All|Console|File|None.
     */
    public static void formattedLog(String s, LogMode mode){
        LocalDateTime now = LocalDateTime.now();
        if (timestamp == null){ // Ensure timestamp is set only once in a single lifecycle
            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(now);
        }

        if (mode == LogMode.All){
            System.out.println(String.format("%s -- Test Number %s -- %s",dtf.format(now),test_number,s));
            FileLogger.log(String.format("%s -- Test Number %s -- %s",dtf.format(now),test_number,s),timestamp);
        } else if(mode == LogMode.Console){
            System.out.println(String.format("%s -- Test Number %s -- %s",dtf.format(now),test_number,s));
        } else if(mode == LogMode.File){
            FileLogger.log(String.format("%s -- Test Number %s -- %s",dtf.format(now),test_number,s),timestamp);
        }
    }
}
