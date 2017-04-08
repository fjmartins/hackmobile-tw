package com.tw.hackmob.saferide.utils;

import java.util.UUID;

/**
 * Created by phgm on 08/04/2017.
 */

public class Utils {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String emailToUser(String email) {
        return email.split("@")[0].replaceAll("[^\\w\\s\\-_]", "");
    }
}
