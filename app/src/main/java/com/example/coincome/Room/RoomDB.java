package com.example.coincome.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Favorite.class,Setting.class},version = 1)
public abstract class RoomDB extends RoomDatabase {

    public abstract DatabaseDao DatabaseDao();

    private  static RoomDB INSTANCE;

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "coincome").addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            db.execSQL("insert into setting(setname,status) values ('Notice','Y')");
                            db.execSQL("insert into setting(setname,status) values ('DayNight','Y')");
                            db.execSQL("insert into setting(setname,status) values ('AppFirst','Y')");
                        }
                    }).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
