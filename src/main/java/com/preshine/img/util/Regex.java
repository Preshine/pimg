package com.preshine.img.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    public static boolean isMobile(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[2,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone==null || phone.length() != 11) {
            return false;
        } else {
            Pattern p  = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
            }
            return isMatch;
        }
    }

}
