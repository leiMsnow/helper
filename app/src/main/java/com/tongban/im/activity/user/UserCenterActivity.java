package com.tongban.im.activity.user;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

/**
 * 用户中心（他人的）
 *
 * @author fushudi
 */
public class UserCenterActivity extends BaseToolBarActivity {
    private PullToZoomScrollViewEx lvUserCenter;

    private TextView tvTags;
    private TextView tvMyGroup, tvMyCollect;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {
        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        View headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);
        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);

        tvTags = (TextView) findViewById(R.id.tv_declaration);
        tvTags.setVisibility(View.GONE);
        tvMyGroup = (TextView) contentView.findViewById(R.id.tv_my_topic);
        tvMyCollect = (TextView) contentView.findViewById(R.id.tv_my_collect);
        tvMyGroup.setVisibility(View.GONE);
        tvMyCollect.setVisibility(View.GONE);

        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
