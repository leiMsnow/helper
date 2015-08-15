package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.BadgeView;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

/**
 * 用户中心
 */
public class UserCenterActivity extends BaseToolBarActivity implements View.OnClickListener {

    private PullToZoomScrollViewEx lvUserCenter;
    private BadgeView fansBadgeView;
    private LinearLayout llUserInfo;
    private RelativeLayout rlFans, rlFollow, rlTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {

        return R.layout.activity_user_center;
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

        llUserInfo = (LinearLayout) headView.findViewById(R.id.ll_user_info);
        rlFans = (RelativeLayout) headView.findViewById(R.id.rl_fans);
        rlFollow = (RelativeLayout) headView.findViewById(R.id.rl_follow);
        rlTopic = (RelativeLayout) headView.findViewById(R.id.rl_topic);

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
        llUserInfo.setOnClickListener(this);
        rlFans.setOnClickListener(this);
        rlFollow.setOnClickListener(this);
        rlTopic.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == llUserInfo) {
            startActivity(new Intent(this, PersonalDataActivity.class));
        } else if (v == rlFans) {
            Intent intent = new Intent(this, MyInfoActivity.class);
            intent.putExtra("Tag", "Fans");
            startActivity(intent);
        } else if (v == rlFollow) {
            Intent intent = new Intent(this, MyInfoActivity.class);
            intent.putExtra("Tag", "Follow");
            startActivity(intent);
        } else if (v == rlTopic) {

        }
    }
}
