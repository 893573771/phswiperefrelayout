package com.ph.myswiperefreshlayoutdemo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ph.phswiperefreshlayout.PhSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ph on 2017/7/26.
 */
public class MainActivity extends AppCompatActivity {

    private PhSwipeRefreshLayout mRefreshLayout;
    private ListView mListView;
    private List<String> mDatas;
    private List<String> mNewDatas;
    private ArrayAdapter<String> mAdapter;
    private int mNowPage = 1;
    private int mTotalPageNum = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRefresh();
        initRefreshDatas();
        initClick();
    }

    private void initClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view == mRefreshLayout.getFooterView()){
                    Toast.makeText(MainActivity.this, "底部", Toast.LENGTH_SHORT).show();
                }else if (position == 4){
                    Log.i("hh", "跳转前");
                    startActivity(new Intent(MainActivity.this, RecyclerviewActivity.class));
                }else {
                    mDatas.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
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
    }

    private void initLoadMoreDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshLoadMoreInfoFinish(PhSwipeRefreshLayout.REFRESH_FINISH, true);
            }
        }, 2000);
    }

    private void initView() {
        mRefreshLayout = (PhSwipeRefreshLayout) findViewById(R.id.my_refresh_layout);
        mListView = (ListView) findViewById(R.id.lv_data);
        mDatas = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(true);
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
