package com.tongban.corelib.widget.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tongban.corelib.R;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;

/**
 * @param <T>
 * @author mrsimple
 */
public abstract class PullRefreshBase<T extends View> extends LinearLayout implements
        OnScrollListener {

    /**
     * 内容视图, 比如ListView, GridView等
     */
    protected T mContentView;

    /**
     * 下拉头视图,因为要修改其padding,所以类型为ViewGroup类型
     */
    protected ViewGroup mHeaderView;

    /**
     * footer视图,因为要修改其padding,所以类型为ViewGroup类型
     */
    protected ViewGroup mFooterView;
    /**
     * 滑动到底部则自动加载的监听器
     */
    protected OnLoadMoreListener mLoadMoreListener;

    /**
     * LayoutInflater
     */
    protected LayoutInflater mInflater;

    /**
     * Header 的高度
     */
    protected int mHeaderViewHeight;

    /**
     * 空闲状态
     */
    public static final int STATUS_IDLE = 0;

    /**
     * 下拉或者上拉状态
     */
    public static final int STATUS_PULL_TO_REFRESH = 1;

    /**
     * 下拉或者上拉状态
     */
    public static final int STATUS_RELEASE_TO_REFRESH = 2;
    /**
     * 刷新中
     */
    public static final int STATUS_REFRESHING = 3;

    /**
     * LOADING中
     */
    public static final int STATUS_LOADING = 4;

    /**
     * 当前状态
     */
    protected int mCurrentStatus = STATUS_IDLE;

    /**
     * Y轴上滑动的距离
     */
    protected int mYDistance = 0;
    /**
     * 滑动的距离阀值,超过这个阀值则认为是有效滑动
     */
    protected int mTouchSlop = 0;
    /**
     * 触摸事件按下的y坐标
     */
    protected int mYDown = 0;

    /**
     * header view里面的进度条
     */
    protected ProgressBar mHeaderProgressBar;
    /**
     * 下拉头的箭头
     */
    protected ImageView mArrowImageView;
    /**
     * 箭头图标是否是向上的状态
     */
    protected boolean isArrowUp = false;
    /**
     * 下拉刷新的文字TextView
     */
    protected TextView mTipsTextView;
    /**
     * 更新时间的文字TextView
     */
    protected TextView mTimeTextView;
    /**
     * footer view's text
     */
    protected TextView mFooterTextView;
    /**
     * footer view's height
     */
    protected int mFooterHeight;
    /**
     * 屏幕高度
     */
    protected int mScrHeight = 0;

    /**
     * @param context
     */
    public PullRefreshBase(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public PullRefreshBase(Context context, AttributeSet attrs) {

        super(context, attrs);

        mInflater = LayoutInflater.from(context);
        setOrientation(LinearLayout.VERTICAL);
        initLayout(context);
    }

    /**
     * 初始化整体布局, header view放在第一个，然后是content view 和 footer view .其中content view的
     * 宽度和高度都为match parent .
     */
    protected final void initLayout(Context context) {
        // 初始化 content view
        initContentView();
        setContentView(mContentView);

        // 初始化 footer
        initFooterView();

        //
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //
        mScrHeight = context.getResources().getDisplayMetrics().heightPixels;

    }


    /**
     * 初始化footer view
     */
    protected void initFooterView() {

        mFooterView = (ViewGroup) mInflater.inflate(R.layout.footer_more_data, null);

        mFooterTextView = (TextView) mFooterView.findViewById(R.id.tv_more_data);
        this.addView(mFooterView, 2);
    }

    /*
     * 获取header view, footer view的高度
     * @see android.widget.LinearLayout#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed && mHeaderViewHeight <= 0) {
            mHeaderViewHeight = mHeaderView.getHeight();
            // padding
            adjustHeaderPadding(-mHeaderViewHeight);

            mFooterHeight = mFooterView.getHeight();
            adjustFooterPadding(-mFooterHeight);
        }
    }

    /**
     * 子类必须实现这个方法，并且在该方法中初始化mContentView字段，即你要显示的主视图.
     * 例如PullRefreshListView的mContentView就是ListView
     */
    protected abstract void initContentView();

    /**
     * @param view
     */
    public void setContentView(T view) {
        mContentView = view;
        LinearLayout.LayoutParams lvLayoutParams = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        if (lvLayoutParams == null) {
            lvLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        lvLayoutParams.bottomMargin = 0;
        lvLayoutParams.weight = 1.0f;
        mContentView.setLayoutParams(lvLayoutParams);
        this.addView(mContentView, 1);
    }

    /**
     * @return
     */
    public T getContentView() {
        return mContentView;
    }

//    /*
//     * 在适当的时候拦截触摸事件，这里指的适当的时候是当mContentView滑动到顶部，并且是下拉时拦截触摸事件，否则不拦截，交给其child
//     * view 来处理。
//     * @see
//     * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
//     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        /*
//         * This method JUST determines whether we want to intercept the motion.
//         * If we return true, onTouchEvent will be called and we do the actual
//         * scrolling there.
//         */
//        final int action = MotionEventCompat.getActionMasked(ev);
//        // Always handle the case of the touch gesture being complete.
//        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
//            // Do not intercept touch event, let the child handle it
//            return false;
//        }
//
//        switch (action) {
//
//            case MotionEvent.ACTION_DOWN:
//                mYDown = (int) ev.getRawY();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                // int yDistance = (int) ev.getRawY() - mYDown;
//                mYDistance = (int) ev.getRawY() - mYDown;
//                showStatus(mCurrentStatus);
//                Log.d(VIEW_LOG_TAG, "%%% isBottom : " + isBottom() + ", isTop : " + isTop()
//                        + ", mYDistance : " + mYDistance);
//                // 如果拉到了顶部, 并且是下拉,则拦截触摸事件,从而转到onTouchEvent来处理下拉刷新事件
//                if ((isTop() && mYDistance > 0)
//                        || (mYDistance > 0 && mCurrentStatus == STATUS_REFRESHING)) {
//                    Log.d(VIEW_LOG_TAG, "--------- mYDistance : " + mYDistance);
//                    return true;
//                }
//                break;
//
//        }
//
//        // Do not intercept touch event, let the child handle it
//        return false;
//    }

    /**
     * @param status
     */
    private void showStatus(int status) {
        String statusString = "";
        if (status == STATUS_IDLE) {
            statusString = "idle";
        } else if (status == STATUS_PULL_TO_REFRESH) {
            statusString = "pull to refresh";
        } else if (status == STATUS_RELEASE_TO_REFRESH) {
            statusString = "release to refresh";
        } else if (status == STATUS_REFRESHING) {
            statusString = "refreshing";
        }
        Log.d(VIEW_LOG_TAG, "### status = " + statusString);
    }

    /*
     * 在这里处理触摸事件以达到下拉刷新或者上拉自动加载的问题
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(VIEW_LOG_TAG, "@@@ onTouchEvent : action = " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) event.getRawY();
                Log.d(VIEW_LOG_TAG, "#### ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(VIEW_LOG_TAG, "#### ACTION_MOVE");
                int currentY = (int) event.getRawY();
                mYDistance = currentY - mYDown;

                Log.d(VIEW_LOG_TAG, "### touch slop = " + mTouchSlop + ", distance = " + mYDistance);
                showStatus(mCurrentStatus);
                // 高度大于header view的高度才可以刷新
                if (Math.abs(mYDistance) >= mTouchSlop) {
                    if (mCurrentStatus != STATUS_REFRESHING) {
                        //
                        if (mHeaderView.getPaddingTop() > mHeaderViewHeight * 0.7f) {
                            mCurrentStatus = STATUS_RELEASE_TO_REFRESH;
//                            mTipsTextView.setText(R.string.pull_to_refresh_release_label);
                        } else {
                            mCurrentStatus = STATUS_PULL_TO_REFRESH;
//                            mTipsTextView.setText(R.string.pull_to_refresh_pull_label);
                        }
                    }

                    // 对滑动距离取了80%
                    int scaleHeight = (int) (mYDistance * 0.8f);
                    // 滑动的距离小于屏幕高度4分之一时才拉伸header, 否则保持不变
                    if (scaleHeight <= mScrHeight / 4) {
                        adjustHeaderPadding(scaleHeight);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                // 下拉刷新的具体操作
//                doRefresh();
                break;
            default:
                break;

        }

        return true;
    }

    /**
     * 下拉到底部时加载更多
     */
    private void loadMore() {
        if (isShowFooterView() && mLoadMoreListener != null) {
            mFooterTextView.setText(R.string.pull_to_refresh_refreshing_label);
            adjustFooterPadding(0);
            mCurrentStatus = STATUS_LOADING;
            mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    /**
     * 隐藏footer view
     */
    protected void hideFooterView() {
        adjustFooterPadding(-mFooterHeight);
    }

    /**
     * 上拉加载结束
     */
    public void loadMoreComplete() {
        mCurrentStatus = STATUS_IDLE;
        mFooterTextView.setText(R.string.pull_to_refresh_load_label);
    }

    /**
     * 调整header view的bottom padding
     *
     * @param bottomPadding
     */
    private void adjustFooterPadding(int bottomPadding) {
        mFooterView.setPadding(mFooterView.getPaddingLeft(), 0,
                mFooterView.getPaddingRight(), bottomPadding);
    }

    /**
     * 调整header view的top padding
     *
     * @param topPadding
     */
    private void adjustHeaderPadding(int topPadding) {
        mHeaderView.setPadding(mHeaderView.getPaddingLeft(), topPadding,
                mHeaderView.getPaddingRight(), 0);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /*
     * 滚动事件，实现滑动到底部时上拉加载更多
     * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.
     * AbsListView, int, int, int)
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {

        Log.d(VIEW_LOG_TAG, "&&& mYDistance = " + mYDistance);
        if (mFooterView == null || mYDistance >= 0 || mCurrentStatus == STATUS_LOADING
                || mCurrentStatus == STATUS_REFRESHING) {
            return;
        }

        loadMore();
    }

    /**
     * 下拉到底部时加载更多
     *
     * @return
     */
    protected boolean isBottom() {
        return false;
    }

    /**
     * 是否到了显示footer view的时刻，该方法在onScroll中调用。在这个类中实现了mScroll方法，
     * 在设置mContentView时会将this设置给mContentView,以此监听mContentView的滑动事件.
     * 因此如果需要支持上拉加载更多则mContentView必须支持setOnScrollListener方法
     * ,并且在初始化mContentView时调用该方法进行注册.
     *
     * @return
     */
    protected boolean isShowFooterView() {
        return false;
    }

}