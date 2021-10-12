package com.example.coincome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.coincome.Fragment.CalcFragment;
import com.example.coincome.Fragment.NoticeFragment;
import com.example.coincome.Fragment.QuoteFragment;
import com.example.coincome.Fragment.SettingFragment;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.HttpClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    Fragment quoteFragment;
    Fragment calcFragment;
    Fragment noticeFragment;
    Fragment settingFragment;
    ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottom_navigationbar = findViewById(R.id.bottom_navigationbar);

        quoteFragment = new QuoteFragment();
        calcFragment = new CalcFragment();
        noticeFragment = new NoticeFragment();
        settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,quoteFragment).commit();

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

    }


}