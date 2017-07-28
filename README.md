phswiperefrelayout

自定义swiperefreshlayout下拉刷新和上拉加载更多

依赖添加: compile 'com.ph:phswiperefreshlayout:1.0.1'

在布局文件中例如:

<com.ph.phswiperefreshlayout.PhSwipeRefreshLayout

    android:id="@+id/my_refresh_layout"
    
    android:layout_width="0dp"
    
    android:layout_height="0dp"
    
    app:layout_constraintBottom_toBottomOf="parent"
    
    app:layout_constraintLeft_toLeftOf="parent"
    
    app:layout_constraintRight_toRightOf="parent"
    
    app:layout_constraintTop_toTopOf="parent">
    
<ListView

    android:id="@+id/lv_data"
    
    android:layout_width="0dp"
    
    android:layout_height="0dp"
    
    app:layout_constraintBottom_toBottomOf="@+id/my_refresh_layout"
    
    app:layout_constraintLeft_toLeftOf="@+id/my_refresh_layout"
    
    app:layout_constraintRight_toRightOf="@+id/my_refresh_layout"
    
    app:layout_constraintTop_toTopOf="@+id/my_refresh_layout"/>
    
</com.ph.phswiperefreshlayout.PhSwipeRefreshLayout>
</android.support.constraint.ConstraintLayout>

使用和swiperefresh类似,swiperefreshlayout设置依旧,上下拉监听为

mRefreshLayout.setOnPullToLoadMoreInfoListener(new PhSwipeRefreshLayout.OnPullToLoadMoreListener() { @Override public void startLoadMoreInfo() { initLoadMoreDatas(); }

        @Override
        public void startRefreshInfo() {
            initRefreshDatas();
        }
    });
数据加载完成调用: 一个是加载状态,第二个是是否还有更多数据 mRefreshLayout.setRefreshLoadMoreInfoFinish(PhSwipeRefreshLayout.LOAD_MORE_FINISH, false);


recyclerview使用 需要用里面的adapter, PhRecyclerViewAdapter
布局文件

<com.ph.phswiperefreshlayout.PhSwipeRefreshLayout

        android:id="@+id/ph_refreshlayout"
        
        android:layout_width="match_parent"
        
        android:layout_height="match_parent">
        
        <android.support.v7.widget.RecyclerView
        
            android:id="@+id/rv_list"
            
            android:layout_width="match_parent"
            
            android:layout_height="match_parent">

        </android.support.v7.widget.RecyclerView>
        
    </com.ph.phswiperefreshlayout.PhSwipeRefreshLayout>
    
    在activity中
    mRefreshLayout.setOnPullToLoadMoreInfoListener(new PhSwipeRefreshLayout.OnPullToLoadMoreListener() {
            @Override
            public void startLoadMoreInfo() {
            //加载更多数据
                initLoadMoreDatas();
            }

            @Override
            public void startRefreshInfo() {
            //刷新数据
                initRefreshDatas();
            }
        });

adapter的使用

mAdapter = new PhRecyclerViewAdapter<String>(this, R.layout.item_recyclerview, mDatas) {
            @Override
            protected void setClickListener(ViewHolder holder, final int position) {
            //item监听
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //移除单个子view,需要调用 mAdapter.setOneItemRemoved();因为脚布局位置也需要改变
                        mDatas.remove(position);
                        mAdapter.setOneItemRemoved();
                        mAdapter.notifyDataSetChanged();
                    }
                });
                //item中的个别控件设置监听方法
                holder.getView(R.id.tv_rv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecyclerviewActivity.this, "点击了textview" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //绑定数据
            @Override
            public void convert(PhRecyclerViewAdapter.ViewHolder holder, String s) {
                holder.setText(R.id.tv_rv, s);
            }
        };
        mListView.setLayoutManager(mLinearLayoutManager);
        mListView.setAdapter(mAdapter);

