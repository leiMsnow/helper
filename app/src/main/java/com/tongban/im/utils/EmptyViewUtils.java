package com.tongban.im.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.im.R;
import com.tongban.im.api.CommonApi;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.base.BaseApi;

/**
 * Created by zhangleilei on 9/19/15.
 */
public class EmptyViewUtils {


    private static EmptyViewUtils instance;

    private EmptyViewUtils() {

    }

    public static EmptyViewUtils getInstance() {
        if (instance == null) {
            synchronized (EmptyViewUtils.class) {
                if (instance == null) {
                    instance = new EmptyViewUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取空数据的背景图id
     *
     * @param result
     * @return
     */
    private int getEmptyViewRes(ApiErrorResult result) {
        int resId = 0;

        // 无网络
        if (result.getErrorCode() == BaseApi.API_NO_NETWORK) {
            resId = R.mipmap.bg_empty_no_network;
        }
        // 服务器不通
        else if (result.getErrorCode() == BaseApi.API_URL_ERROR) {
            resId = R.mipmap.bg_empty_url_error;
        }
        // 专题相关
        else if (result.getApiName().equals(ProductApi.FETCH_HOME_INFO)) {
            resId = R.mipmap.bg_empty_discover;
        }
        // 话题相关
        else if (result.getApiName().equals(TopicApi.RECOMMEND_TOPIC_LIST) ||
                result.getApiName().equals(TopicApi.TOPIC_INFO) ||
                result.getApiName().equals(TopicApi.TOPIC_COMMENT_LIST) ||
                result.getApiName().equals(TopicApi.OFFICIAL_TOPIC_INFO)) {
            resId = R.mipmap.bg_empty_topic;
        }
        // 圈子相关
        else if (result.getApiName().equals(GroupApi.RECOMMEND_GROUP_LIST) ||
                result.getApiName().equals(GroupApi.GROUP_INFO)) {
            resId = R.mipmap.bg_empty_group;
        }
        // 搜索相关
        else if (result.getApiName().equals(ProductApi.SEARCH_THEME) ||
                result.getApiName().equals(ProductApi.SEARCH_PRODUCT) ||
                result.getApiName().equals(CommonApi.FETCH_DISCOVER_TAG)) {
            resId = R.mipmap.bg_empty_search;
        }
        return resId;
    }

    /**
     * 创建空数据布局
     */
    public View createEmptyView(Context mContext) {
        View mEmptyParentView = LayoutInflater.from(mContext)
                .inflate(R.layout.view_empty, null);
        ObjectAnimator objectAnimator = ObjectAnimator.
                ofFloat(mEmptyParentView, "alpha", 0.0f, 1.0f)
                .setDuration(500);
        objectAnimator.start();
        return mEmptyParentView;
    }

    /**
     * 设置空数据的alpha
     *
     * @param mEmptyParentView
     */
    public void setAlphaEmptyView(View mEmptyParentView) {

        if (mEmptyParentView != null) {
            mEmptyParentView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 显示空数据布局
     *
     * @param result
     * @param mEmptyParentView
     * @param height
     * @param requestApiListener
     */
    public void showEmptyView(ApiErrorResult result, View mEmptyParentView, int height
            , final RequestApiListener requestApiListener) {
        if (mEmptyParentView != null) {
            int resId = getEmptyViewRes(result);
            if (resId == 0) {
                hideEmptyView(mEmptyParentView);
                return;
            }
            // 设置文本
            TextView tvMsg = (TextView) mEmptyParentView.findViewById(R.id.tv_empty);
            tvMsg.setText(result.getErrorMessage());
            // 设置图片
            ImageView ivEmpty = (ImageView) mEmptyParentView.findViewById(R.id.iv_empty);
            ivEmpty.setImageResource(resId);
            // 设置监听
            View mEmptyView = mEmptyParentView.findViewById(R.id.rl_empty_view);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mEmptyView.getLayoutParams();
            lp.topMargin = height;
            mEmptyView.setLayoutParams(lp);

            mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestApiListener != null) {
                        requestApiListener.onRequest();
                    }
                }
            });
        }
    }

    /**
     * 隐藏空数据布局
     */
    public void hideEmptyView(final View mEmptyParentView) {
        if (mEmptyParentView != null) {
            mEmptyParentView.setVisibility(View.GONE);
        }
    }


}
