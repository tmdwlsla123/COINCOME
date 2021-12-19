package com.example.coincome.Paging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.rxjava3.PagingRx;

import com.example.coincome.RecyclerView.Notice;
import com.example.coincome.Retrofit2.ApiInterface;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class NoticeViewModel extends ViewModel {
    public Flowable<PagingData<NoticeModel.Data>> getNotices(){
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer,NoticeModel.Data> pager = new Pager<>(
                new PagingConfig(10),1,  NoticePagingSource::new);

        Flowable<PagingData<NoticeModel.Data>> flowable = PagingRx.getFlowable(pager);
        return PagingRx.cachedIn(flowable,viewModelScope );
    }
}
