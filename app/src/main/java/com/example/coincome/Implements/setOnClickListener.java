package com.example.coincome.Implements;

import android.graphics.drawable.ClipDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coincome.R;
import com.example.coincome.ViewModel.CoinRepository;

public class setOnClickListener implements View.OnClickListener{
    protected int index;
    protected ImageView[] sortImage = new ImageView[4];
    protected int flag,flag2;
    protected ScrollListener scrollListener;



    public setOnClickListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;

    }
    public void AddListener(LinearLayout l1,LinearLayout l2,LinearLayout l3,LinearLayout l4){
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        l4.setOnClickListener(this);
    }
    public void AddClip(ImageView c1,ImageView c2,ImageView c3,ImageView c4){
        sortImage[0] = c1;
        sortImage[1] = c2;
        sortImage[2] = c3;
        sortImage[3] = c4;

    }


    @Override
    public void onClick(View view) {
//        if (flag != flag2) index = 0;
        Log.v("onsclick", view.getResources().getResourceEntryName(view.getId()));
        if(view.getResources().getResourceEntryName(view.getId()).equals("name_sort")){
            flag = 0;
        }else if(view.getResources().getResourceEntryName(view.getId()).equals("price_sort")){
            flag = 1;
        }else if(view.getResources().getResourceEntryName(view.getId()).equals("daytoday_sort")){
            flag = 2;
        }else{
            flag = 3;
        }

        if(flag!=flag2){
            index = 0;
            sortImage[flag2].setImageResource(R.drawable.sort_up_clip_source);
            sortImage[flag2].setImageLevel(10000);

        }
        scrollListener.scrollFlag = 0;
        switch (index){
            case -1:index =1;
                //오름차순
                sortImage[flag].setImageResource(R.drawable.sort_up_clip_source);
                sortImage[flag].setImageLevel(5000);
                break;
            case 1:index=0;
                //원래대로
                sortImage[flag].setImageLevel(10000);
                break;
            default:index=-1;
                //내림차순
                sortImage[flag].setImageResource(R.drawable.sort_down_clip_source);
                sortImage[flag].setImageLevel(5000);
                break;
        }
        CoinRepository.getInstance().setIndex(index);
        CoinRepository.getInstance().setFlag(flag);
        flag2 = flag;

    }
}
