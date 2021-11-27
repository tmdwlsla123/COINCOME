package com.example.coincome.Implements;

import android.app.Application;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.coincome.Room.RoomDB;
import com.example.coincome.Room.Setting;

import java.util.List;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RoomDB roomDB = RoomDB.getDatabase(getApplicationContext());


                AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Setting> data = roomDB.DatabaseDao().getDayNightSetting();
                for(int i = 0; i < data.size(); i++){
                    if(data.get(i).getStatus().equals("Y")) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                ThemeUtil.applyTheme(ThemeUtil.DARK_MODE);
                                ThemeUtil.IF_GET_DB = true;
                                Log.v("setting", "IF_GET_DB : myapp");
                            }
                        });
                    }
                }
            }
        });
    }
}
