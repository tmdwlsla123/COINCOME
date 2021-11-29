package com.example.coincome.Implements;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener
{

    protected RecyclerView recyclerView;
    protected Integer position;
    protected int flag;
    protected int scrollFlag;
    public ScrollListener(RecyclerView recyclerView) {
        super();
        this.recyclerView = recyclerView;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstElementPosition = lManager.findFirstVisibleItemPosition();

            flag = newState;
            if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                scrollFlag = newState;
            }
//            Log.v("onScrollStateChanged", String.valueOf(newState));


//        Log.v("onScrollStateChanged", String.valueOf(firstElementPosition));
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstElementPosition = lManager.findFirstVisibleItemPosition();
        if(flag == 0 && scrollFlag == 0){
            if(position==null){
                position = firstElementPosition;
            }else {
                recyclerView.getLayoutManager().scrollToPosition(position);
//                Log.v("onScrolled","position :" +String.valueOf(position));
//                Log.v("onScrolled","scrollFlag :" +String.valueOf(scrollFlag));
            }
        }



//        Log.v("onScrolled", String.valueOf(firstElementPosition));
    }
}
