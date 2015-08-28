package com.tongban.corelib.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 上拉加载-gridview
 * Created by zhangleilei on 8/28/15.
 */
public class LoadMoreGridView extends PullRefreshBase<GridView> {


    public LoadMoreGridView(Context context) {
        this(context, null);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initContentView() {
        // 初始化mContentView
        mContentView = new GridView(getContext());
        // 设置OnScrollListener, 用以实现滑动到底部时的自动加载功能，如果不需要该功能可以不设置.
        mContentView.setOnScrollListener(this);
    }

    @Override
    protected boolean isShowFooterView() {
        if (mContentView == null || mContentView.getAdapter() == null) {
            return false;
        }

        return mContentView.getLastVisiblePosition() == mContentView.getAdapter().getCount() - 1;
    }
}
