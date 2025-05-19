package utils.enums;

public enum BrowserEngine {
    Chromium,Firefox,Webkit;

    public static BrowserEngine parse(String prop){
        if (prop.toLowerCase().contains("firefox")){
            return Firefox;
        }else if (prop.toLowerCase().contains("webkit") || prop.toLowerCase().contains("webkit") ){
            return Webkit;
        }else{
            return Chromium;
        }
    }
}