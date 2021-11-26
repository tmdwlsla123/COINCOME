package com.example.coincome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coincome.Fragment.CalcFragment;
import com.example.coincome.Fragment.NoticeDetailFragment;
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

import java.util.Iterator;
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
    public BottomNavigationView bottomNavigationView;
    String token;
    private String TAG = "FirebaseMessagingService";
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity","onDestroy");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        bottomNavigationView.setSelectedItemId(R.id.d);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Log.e("뭐가있는데", key + " : " + (extras.get(key) != null ? extras.get(key) : "NULL"));
            }
                if(extras.getString("id")!=null){
                    String fromNotification = extras.getString("id");
                    Bundle bundle = new Bundle();
                    Fragment fr = new NoticeFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    bundle.putString("id", fromNotification);
                    fr.setArguments(bundle);
                    getIntent().replaceExtras(new Bundle());
                    getIntent().setAction("");
                    getIntent().setData(null);
                    getIntent().setFlags(0);
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, fr);
                    fragmentTransaction.commit();
                }

        }
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
        bottomNavigationView = findViewById(R.id.bottom_navigationbar);
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


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment page = getSupportFragmentManager().findFragmentByTag("noticeDetail");
                if(R.id.c!=item.getItemId()&&page!=null){
                    getSupportFragmentManager().beginTransaction().remove(page).commit();
                    getSupportFragmentManager().popBackStack();
                }
                switch (item.getItemId()){

                    case R.id.a:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,quoteFragment).commit();
                        return true;
                    case R.id.b:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, calcFragment).commit();
                        return true;
                    case R.id.c:
                        // based on the current position you can then cast the page to the correct
                        // class and call the method:
                        if (page == null) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, noticeFragment).commit();
                        }
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