package com.example.coincome.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coincome.R;


public class SettingFragment extends Fragment {
    /**
     *  네번째 탭 설정창
     *
     */
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        Log.v("NoticeFragment","생성");
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("NoticeFragment","파괴");
    }
}