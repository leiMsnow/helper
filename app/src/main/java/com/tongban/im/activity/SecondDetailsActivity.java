package com.tongban.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rey.material.widget.Button;
import com.tb.api.model.TalentInfo;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.fragment.ServiceDetailsFragment;
import com.tongban.im.fragment.TalentDetailsFragment;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class SecondDetailsActivity extends AppBaseActivity {

    //服务详情
    public static final int SERVICE_DETAILS = 0;
    //达人详情
    public static final int TALENT_DETAILS = 1;


    @Bind(R.id.btn_chat)
    Button btnChat;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.iv_user_portrait)
    CircleImageView ivUserPortrait;
    @Bind(R.id.iv_bg_url)
    ImageView ivBgUrl;
    @Bind(R.id.fl_portrait)
    FrameLayout flPortrait;
    @Bind(R.id.sv_user_center)
    NestedScrollView svUserCenter;


    private String mTagId;

    private Fragment mFragment;


    public void setBottomOnClickListener(BottomOnClickListener bottomOnClickListener) {
        this.bottomOnClickListener = bottomOnClickListener;
    }

    BottomOnClickListener bottomOnClickListener;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_second_details;
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            int mFragmentTag = getIntent().getIntExtra("fragment_tag", -1);
            mTagId = getIntent().getStringExtra("tag_id");

            switch (mFragmentTag) {
                case SERVICE_DETAILS:
                    mFragment = ServiceDetailsFragment.getInstance(mTagId);
                    btnChat.setVisibility(View.VISIBLE);
                    break;
                case TALENT_DETAILS:
                    mFragment = TalentDetailsFragment.getInstance(mTagId);
                    break;
            }

            if (mFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_container, mFragment)
                        .show(mFragment)
                        .commit();
            }
        }
        svUserCenter.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY
                    , int oldScrollX, int oldScrollY) {
                if (scrollY > 0) {
                    flPortrait.setVisibility(View.GONE);
                } else {
                    flPortrait.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 启动二级页
     *
     * @param tag   打开的界面标记 服务/用户详情
     * @param tagId 服务ID或者userID
     */
    public static void startDetailsActivity(Activity activity
            , int tag
            , String tagId) {

        Intent intent = new Intent(activity, SecondDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("fragment_tag", tag);
        bundle.putString("tag_id", tagId);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }

    @OnClick(R.id.btn_chat)
    public void onClick(View view) {
        if (bottomOnClickListener != null) {
            bottomOnClickListener.bottomOnClick(mTagId);
        }
    }

    public interface BottomOnClickListener {

        void bottomOnClick(String tagId);
    }

    public void onEventMainThread(TalentInfo talentInfo) {
        if (talentInfo.getUser() != null
                && talentInfo.getUser().getPortraitUrl() != null) {
            setUserPortrait(talentInfo.getUser().getPortraitUrl().getMin(), ivUserPortrait);
            collapsingToolbarLayout.setTitle(talentInfo.getUser().getNick_name());
        }
        setImagePortrait(talentInfo.getBg_url(), ivBgUrl);
    }
}
