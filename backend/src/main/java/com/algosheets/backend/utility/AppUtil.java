package com.algosheets.backend.utility;

public class AppUtil {

    public static boolean isValidString(String data){
       return data!=null && !data.isBlank();
    }

    public static boolean isValidPattern(String pattern,String data){

        return data.matches(pattern);
    }

}
