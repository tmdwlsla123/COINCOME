package com.example.coincome.Implements;

import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtil {
    public static final String LIGHT_MODE = "light";
    public static final String DARK_MODE = "dark";
    public static final String DEFAULT_MODE = "default";
    public static Integer NOW_MODE;
    public static int IF_USR_SET;


    public static void applyTheme(String themeColor) {

        switch (themeColor) {
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                NOW_MODE = AppCompatDelegate.MODE_NIGHT_NO;
                break;

            case DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                NOW_MODE = AppCompatDelegate.MODE_NIGHT_YES;
                break;

            default:
                // 안드로이드 10 이상
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    NOW_MODE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                }
                // 안드로이드 10 미만
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    NOW_MODE = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
                }
                break;
        }
    }
}
