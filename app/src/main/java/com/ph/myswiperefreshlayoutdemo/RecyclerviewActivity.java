package com.ph.myswiperefreshlayoutdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ph.phswiperefreshlayout.PhRecyclerViewAdapter;
import com.ph.phswiperefreshlayout.PhSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ph on 2017/7/26.
 */

public class RecyclerviewActivity extends AppCompatActivity{
    private PhSwipeRefreshLayout mRefreshLayout;
    private RecyclerView mListView;
    LinearLayoutManager mLinearLayoutManager;
    private List<String> mDatas;
    private List<String> mNewDatas;
    private PhRecyclerViewAdapter<String> mAdapter;
    private int mNowPage = 1;
    private int mTotalPageNum = 3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initView();
        initRefresh();
//        initRefreshDatas();
    }

    private void initRefresh() {
        mRefreshLayout.setOnPullToLoadMoreInfoListener(new PhSwipeRefreshLayout.OnPullToLoadMoreListener() {
            @Override
            public void startLoadMoreInfo() {
                initLoadMoreDatas();
            }

            @Override
            public void startRefreshInfo() {
                initRefreshDatas();
            }
        });
        mRefreshLayout.setRefreshing(true);
        initRefreshDatas();
    }

    private void initLoadMoreDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("hh", "加载更多数据");
                mDatas.addAll(getMoreData());
                // 加载完数据设置为不加载状态，将加载进度收起来
//                mSwipeRefreshView.setLoading(false);
                ++mNowPage;
                if (mNowPage >= mTotalPageNum){
                    mRefreshLayout.setRefreshLoadMoreInfoFinish(PhSwipeRefreshLayout.LOAD_MORE_FINISH, false);
                }else {
                    mRefreshLayout.setRefreshLoadMoreInfoFinish(PhSwipeRefreshLayout.LOAD_MORE_FINISH, true);
                }
            }
        }, 2000);
    }

    private void initRefreshDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNowPage = 1;
                mDatas.clear();
                mDatas.addAll(getData());
                mAdapter.setHasAddShowFooterView(false, false);
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshLoadMoreInfoFinish(PhSwipeRefreshLayout.REFRESH_FINISH, true);
            }
        }, 2000);
    }

    private void initView() {
        mRefreshLayout = (PhSwipeRefreshLayout) findViewById(R.id.ph_refreshlayout);
        mListView = (RecyclerView) findViewById(R.id.rv_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDatas = new ArrayList<>();
        mAdapter = new PhRecyclerViewAdapter<String>(this, R.layout.item_recyclerview, mDatas) {
            @Override
            protected void setClickListener(ViewHolder holder, final int position) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatas.remove(position);
                        mAdapter.setOneItemRemoved();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                holder.getView(R.id.tv_rv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecyclerviewActivity.this, "点击了textview" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void convert(PhRecyclerViewAdapter.ViewHolder holder, String s) {
                holder.setText(R.id.tv_rv, s);
            }
        };
        mListView.setLayoutManager(mLinearLayoutManager);
        mListView.setAdapter(mAdapter);
    }



    public List<String> getData() {
        mNewDatas = new ArrayList<>();
        for (int i = 0; i < 15; i++){
            mNewDatas.add("测试数据" + i);
        }
        return mNewDatas;
    }

    public List<String> getMoreData() {
        mNewDatas = new ArrayList<>();
        for (int i = 15 * mNowPage; i < 15 * (mNowPage + 1); i++){
            mNewDatas.add("测试数据" + i);
        }
        return mNewDatas;
    }
}
