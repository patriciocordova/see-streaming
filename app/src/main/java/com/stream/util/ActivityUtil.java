package com.stream.util;

import android.app.Activity;

public final class ActivityUtil {

    private static Activity mainActivity;

    public static void setMainActivity(Activity activity){
        mainActivity = activity;
    }

    public static Activity getMainActivity(){
        return mainActivity;
    }
}
