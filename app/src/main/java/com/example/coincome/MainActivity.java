package com.example.coincome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coincome.Fragment.CalcFragment;
import com.example.coincome.Fragment.NoticeFragment;
import com.example.coincome.Fragment.QuoteFragment;
import com.example.coincome.Fragment.SettingFragment;
import com.example.coincome.Implements.ThemeUtil;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.HttpClient;
import com.example.coincome.Room.DatabaseDao;
import com.example.coincome.Room.Favorite;
import com.example.coincome.Room.RoomDB;
import com.example.coincome.Room.Setting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    Fragment quoteFragment;
    Fragment calcFragment;
    Fragment noticeFragment;
    Fragment settingFragment;
    ApiInterface api;
    DatabaseDao roomDB;

    String token;
    private String TAG = "FirebaseMessagingService";
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity","onDestroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RoomDB db = Room.databaseBuilder(this, RoomDB.class,"coincome").allowMainThreadQueries().build();
//      최초실행시 1회 구독
        if(db.DatabaseDao().AppFirstExist()){
            FirebaseMessaging.getInstance().subscribeToTopic("all");
            db.DatabaseDao().updateFirstExist();
        }
        BottomNavigationView bottom_navigationbar = findViewById(R.id.bottom_navigationbar);
        Log.v("MainActivity","onCreate");
        quoteFragment = new QuoteFragment();
        calcFragment = new CalcFragment();
        noticeFragment = new NoticeFragment();
        settingFragment = new SettingFragment();
        if(ThemeUtil.IF_GET_DB==true&&ThemeUtil.IF_USR_SET==0){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,quoteFragment).commit();
            Log.v("setting","IF_GET_DB : MainActivity");
        }else if(ThemeUtil.IF_USR_SET==1){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,settingFragment).commit();
        }


        bottom_navigationbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.a:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,quoteFragment).commit();
                        return true;
                    case R.id.b:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, calcFragment).commit();
                        return true;
                    case R.id.c:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, noticeFragment).commit();
                        return true;
                    case R.id.d:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, settingFragment).commit();
                        return true;
                }

                return false;
            }
        });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.w("fcm log", "get", task.getException());
                            return;
                        }
                        token = task.getResult();
                        Log.d(TAG, token);

                    }
                });

    }

}