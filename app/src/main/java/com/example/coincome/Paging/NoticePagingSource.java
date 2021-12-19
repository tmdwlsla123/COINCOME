package com.example.coincome.Paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.coincome.RecyclerView.Notice;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.HttpClient;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.coroutines.Continuation;
import retrofit2.HttpException;

public class NoticePagingSource extends RxPagingSource<Integer, NoticeModel.Data> {
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, NoticeModel.Data> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, NoticeModel.Data> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

         Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }


    @NotNull
    @Override
    public Single<LoadResult<Integer, NoticeModel.Data>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        Integer page = loadParams.getKey();
        if (page == null) {
            page = 1;
        }
        Log.v("page", String.valueOf(page));
        Integer finalPage = page;
        return HttpClient.getRetrofit().create( ApiInterface.class ).requestGetNotice(page,"10")
                .subscribeOn(Schedulers.io())
                .map(NoticeModel::getData)
                .map(data -> toLoadResult(data, finalPage))
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, NoticeModel.Data> toLoadResult(@NonNull List<NoticeModel.Data> notices , Integer page) {
        Log.d("toLoadResult ", notices.toString());
        if (notices.isEmpty()){
            page = null;
        }
        return
            new LoadResult.Page<>(
                    notices,
                    page.equals(1) ? null: page - 1,
                    page + 1);


        }
}
