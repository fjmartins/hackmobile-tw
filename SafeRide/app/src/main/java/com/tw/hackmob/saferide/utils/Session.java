package com.tw.hackmob.saferide.utils;

import android.content.Context;

import com.tw.hackmob.saferide.model.User;

/**
 * Created by phgm on 08/04/2017.
 */

public class Session {
    private static User sUser;
    public static String sToken;

    public static void setUser(User user) {
        sUser = user;
    }

    public static User getUser(Context activity) {
        if (sUser == null) {
            sUser = Data.getUser(activity);
        }
        return sUser;
    }
}