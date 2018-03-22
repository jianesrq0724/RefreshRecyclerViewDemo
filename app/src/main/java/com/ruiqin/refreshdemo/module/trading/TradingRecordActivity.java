package com.ruiqin.refreshdemo.module.trading;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruiqin.refreshdemo.R;
import com.ruiqin.refreshdemo.base.BaseActivity;
import com.ruiqin.refreshdemo.module.trading.adapter.TradingRecordAdapter;
import com.ruiqin.refreshdemo.module.trading.entiy.RespTradingRecord;
import com.ruiqin.refreshdemo.util.GetJsonDataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ruiqin.shen
 * 类说明：test
 */
public class TradingRecordActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * 自动自身
     *
     * @return
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), TradingRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading_record);
        initToolBar();
        refreshTradingRecord();//联网请求数据
        setSwipeRefreshListener();//设置下拉监听事件
    }

    /**
     * 刷新监听
     */
    private void setSwipeRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTradingRecord();
            }
        });
    }

    private List<RespTradingRecord.RecordsBean> mTradingRecordList = new ArrayList<>();

    private int pageNow = 1;//当前页
    private int pageSize = 12;//每页显示的数据
    private int TotalCount = Integer.MAX_VALUE;//服务器那边总的数目

    /**
     * 联网刷新交易明细的数据
     */
    private void refreshTradingRecord() {
//        mSwipeRefreshLayout.setRefreshing(true);//设置刷新动画

        /**
         * 获取assets目录下的json文件数据
         */
        String jsonData = new GetJsonDataUtil().getJson(this, "trading.json");
        Gson gson = new Gson();
        RespTradingRecord respTradingRecord = gson.fromJson(jsonData, RespTradingRecord.class);

        mTradingRecordList = respTradingRecord.getRecords();

        setTradingRecordAdapter();

    }

    /**
     * 联网请求更多交易明细的数据
     */
    private void requestMoreTradingRecord() {
        isLoading = true;
        /**
         * 获取assets目录下的json文件数据
         */
        String jsonData = new GetJsonDataUtil().getJson(this, "trading.json");
        Gson gson = new Gson();
        RespTradingRecord respTradingRecord = gson.fromJson(jsonData, RespTradingRecord.class);

    }

    /**
     * 延迟隐藏加载进度条
     */
    public void delayHintLoadProgress() {
        Flowable.timer(600, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (mTradingRecordAdapter != null) {
                        if (pageNow * pageSize < TotalCount) {//判断服务器上的条数是否大于当前请求的数目
                            mTradingRecordAdapter.changeMoreStatus(TradingRecordAdapter.PULLUP_LOAD_MORE);
                        } else {
                            mTradingRecordAdapter.changeMoreStatus(TradingRecordAdapter.LOADING_END);
                        }
                        mTradingRecordAdapter.notifyDataSetChanged();
                    }
                }, Throwable::printStackTrace);
    }


    TradingRecordAdapter mTradingRecordAdapter;

    /**
     * 创建适配器
     */
    private void setTradingRecordAdapter() {
        if (mTradingRecordAdapter == null) {
            mTradingRecordAdapter = new TradingRecordAdapter(mTradingRecordList);
            if (TotalCount < pageSize) {//判断是否显示加载更多
                mTradingRecordAdapter.setHideLoadMoreHint();
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(mTradingRecordAdapter);
            setScrollListener(linearLayoutManager);//设置上拉加载更多
        } else {
            mTradingRecordAdapter.notifyDataSetChanged();
        }
    }

    boolean isLoading;
    private int mLastVisibleItemPosition;

    /**
     * 设置上拉加载更多
     */
    private void setScrollListener(LinearLayoutManager layoutManager) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                LogUtils.e("TAG", "onScrollStateChanged" + ", " + newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }

                if (mTradingRecordAdapter != null) {
                    if (!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == mTradingRecordAdapter.getItemCount()) {
                        boolean isRefreshing = mSwipeRefreshLayout.isRefreshing();
                        if (isRefreshing) {
                            return;
                        }
                        if (pageNow * pageSize > TotalCount) {//判断服务器上的条数是否大于当前请求的数目
                            return;
                        }
                        mTradingRecordAdapter.changeMoreStatus(TradingRecordAdapter.LOADING_MORE);//更改状态刷新中
                        Log.e("TAG", "刷新中");
                        pageNow++;
                        requestMoreTradingRecord();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 初始化toolBar
     */
    private void initToolBar() {
        mToolbarTitle.setText("test");
    }

}
