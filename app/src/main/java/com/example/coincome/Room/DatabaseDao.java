package com.example.coincome.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DatabaseDao {



    @Query("SELECT * FROM setting WHERE setname = 'Notice'")
    List<Setting> getNoticeSetting();

    @Query("SELECT * FROM setting WHERE setname = 'DayNight'")
    List<Setting> getDayNightSetting();

    @Query("UPDATE setting SET status = :bool WHERE setname = 'Notice'")
    void updateNotice(String bool);

    @Query("UPDATE setting SET status = :bool WHERE setname = 'DayNight'")
    void updateDayNight(String bool);

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE symbol =:symbol AND exchange =:exchange)")
    boolean favoriteExist(String symbol,String exchange);

    @Insert
    void insesrtFavorite(Favorite favorite);

    @Query("DELETE FROM favorite WHERE symbol =:symbol AND exchange =:exchange")
    void deleteFavorite(String symbol,String exchange);

    @Query("SELECT * FROM favorite WHERE exchange =:exchange")
    LiveData<List<Favorite>> getFavorite(String exchange);

    @Query("SELECT EXISTS(SELECT * FROM setting WHERE setname = 'AppFirst' AND status = 'Y')")
    boolean AppFirstExist();

    @Query("UPDATE setting SET status = 'N' WHERE setname = 'AppFirst' AND status = 'Y'")
    void updateFirstExist();

}
