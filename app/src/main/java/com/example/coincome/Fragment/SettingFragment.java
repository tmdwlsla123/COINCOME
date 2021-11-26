package com.example.coincome.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.coincome.Implements.ThemeUtil;
import com.example.coincome.MainActivity;
import com.example.coincome.R;
import com.example.coincome.Room.DatabaseDao;
import com.example.coincome.Room.Favorite;
import com.example.coincome.Room.RoomDB;
import com.example.coincome.Room.Setting;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends Fragment {
    /**
     *  네번째 탭 설정창
     *
     */
    private Context context;
    Switch noticeSwitch,dayNightMode;
    RoomDB roomDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        noticeSwitch = rootView.findViewById(R.id.notice_switch);
        dayNightMode = rootView.findViewById(R.id.day_night_switch);
        roomDB = RoomDB.getDatabase(context);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String status;
                List<Setting> data = roomDB.DatabaseDao().getNoticeSetting();
                for(int i = 0; i < data.size(); i++){
                    status = data.get(i).getStatus();
                    if(status.equals("Y")){
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noticeSwitch.setChecked(true);
                            }
                        });
                    }

                    else{
                       requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noticeSwitch.setChecked(false);
                            }
                        });
                    }

                }
            }
        });
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String status;
                List<Setting> data = roomDB.DatabaseDao().getDayNightSetting();
                for(int i = 0; i < data.size(); i++){
                    status = data.get(i).getStatus();
                    if(status.equals("Y"))
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v("run","true");
                                dayNightMode.setChecked(true);
                            }
                        });
                    else
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dayNightMode.setChecked(false);
                                Log.v("run","false");
                            }
                        });
                }
            }
        });
        noticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCehcked) {
                if(isCehcked){

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDB.DatabaseDao().updateNotice("Y");
                            FirebaseMessaging.getInstance().subscribeToTopic("all").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }
                    });
                }else{

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDB.DatabaseDao().updateNotice("N");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("all").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });

                        }
                    });

                }
            }
        });

        dayNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCehcked) {
                if(isCehcked){

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDB.DatabaseDao().updateDayNight("Y");
                        }
                    });
                    ThemeUtil.applyTheme(ThemeUtil.DARK_MODE);
                }else{
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            roomDB.DatabaseDao().updateDayNight("N");
                        }
                    });
                    ThemeUtil.applyTheme(ThemeUtil.LIGHT_MODE);
                }
            }
        });

        Log.v("SettingFragment","생성");
        return rootView;
    }

    private void runOnUiThread(Runnable runnable) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("SettingFragment","파괴");
    }

}