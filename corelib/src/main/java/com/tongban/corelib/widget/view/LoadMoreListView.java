package com.tongban.corelib.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.corelib.R;

/**
 * 自动加载更多-listView
 */
public class LoadMoreListView extends ListView implements OnScrollListener{

    private View footer;
    private TextView tvMore;

    private boolean isLoading;// 判断是否正在加载
    private boolean isLoadFull;// 判断是否全部加载完
    private int pageSize = 10;

    private OnLoadMoreListener onLoadListener;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 加载更多监听
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadListener) {
        this.onLoadListener = onLoadListener;
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    // 初始化组件
    private void initView(Context context) {
        footer = LayoutInflater.from(context).inflate(R.layout.footer_more_data, null);
        tvMore = (TextView) footer.findViewById(R.id.tv_more_data);
    }

    // 用于加载更多结束后的回调
    public void onLoadComplete() {
        isLoading = false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        try {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && !isLoading
                    && view.getLastVisiblePosition() == view
                    .getPositionForView(footer) && !isLoadFull) {
                if (onLoadListener != null) {
                    onLoadListener.onLoadMore();
                }
                isLoading = true;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 这个方法是根据结果的大小来决定footer显示的。
     * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。
     * 如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
     *
     * @param resultSize
     */
    public void setResultSize(int resultSize) {
        if (resultSize == 0) {
            isLoadFull = true;
            tvMore.setText("没有更多数据");
        } else if (resultSize > 0 && resultSize < pageSize) {
            isLoadFull = true;
            tvMore.setText("数据已经全部加载");
        } else if (resultSize == pageSize) {
            isLoadFull = false;
            tvMore.setVisibility(View.GONE);
        }

    }


    /**
     * 定义加载更多接口
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

}