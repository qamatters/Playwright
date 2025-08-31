package utils.enums;

public enum LogMode {
    All,Console,File,None;

    public static LogMode parse(String prop){
        if (prop.toLowerCase().contains("none")){
            return None;
        }else if (prop.toLowerCase().contains("console")){
            return Console;
        }else if (prop.toLowerCase().contains("file")){
            return File;
        }else{
            return All;
        }
    }
}