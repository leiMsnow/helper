package com.tongban.im.fragment.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.im.R;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseToolBarFragment extends BaseApiFragment implements IApiCallback,
        RequestApiListener {

    protected View mToolbar;

    /**
     * 设置用户头像信息
     *
     * @param uri  网络地址
     * @param view imageView控件
     */
    public void setUserPortrait(String uri, ImageView view) {
        Glide.with(BaseToolBarFragment.this).load(uri).error(Consts.getUserDefaultPortrait()).into(view);
    }


    protected int getToolbarHeight() {
        int height = 0;
        if (mToolbar != null && mToolbar.getVisibility() != View.GONE) {
            height = DensityUtils.dp2px(mContext, 56);
        }
        return height;
    }

    @Override
    public void setEmptyView(ApiErrorResult result) {
        int resId = 0;
        // 服务器不通
        if (result.getErrorCode() == BaseApi.API_URL_ERROR) {
            resId = R.mipmap.bg_empty_url_error;
        }
        // 专题相关
        if (result.getApiName().equals(ProductApi.FETCH_HOME_INFO)) {
            resId = R.mipmap.bg_empty_discover;
        }
        // 话题相关
        else if (result.getApiName().equals(TopicApi.RECOMMEND_TOPIC_LIST)) {
            resId = R.mipmap.bg_empty_topic;
        }
        // 圈子相关
        else if (result.getApiName().equals(GroupApi.RECOMMEND_GROUP_LIST)) {
            resId = R.mipmap.bg_empty_group;
        }
        // 搜索相关
        else if (result.getApiName().equals(ProductApi.SEARCH_THEME) ||
                result.getApiName().equals(ProductApi.SEARCH_PRODUCT)) {
            resId = R.mipmap.bg_empty_search;
        }
        // 无网络
        if (result.getErrorCode() == BaseApi.API_NO_NETWORK) {
            resId = R.mipmap.bg_empty_no_network;
        }

        ImageView ivEmpty = (ImageView) mEmptyView.findViewById(com.tongban.corelib.R.id.iv_empty);
        if (resId == 0) {
            ivEmpty.setVisibility(View.GONE);
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivEmpty.getLayoutParams();
        lp.topMargin = getToolbarHeight();
        ivEmpty.setLayoutParams(lp);

        ivEmpty.setVisibility(View.VISIBLE);
        ivEmpty.setImageResource(resId);
        ivEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestApiListener != null) {
                    requestApiListener.onRequest();
                }
            }
        });
    }

    @Override
    public void onRequest() {

    }
}
