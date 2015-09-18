package com.tongban.im.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.transformer.ZoomOutPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.discover.DiscoverFragment;
import com.tongban.im.fragment.group.GroupFragment;
import com.tongban.im.fragment.topic.TopicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseToolBarActivity implements View.OnClickListener
        , ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<CheckBox> mTabIndicator = new ArrayList<>();
    private CheckBox tvDiscover, tvTopic, tvGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.getInstance().finishActivity(this);
        fetchPersonalCenter();
    }

    private void fetchPersonalCenter() {
        if (!"".equals(SPUtils.get(mContext, Consts.USER_ID, ""))) {
            UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_content);

        tvDiscover = (CheckBox) findViewById(R.id.tv_discover);
        tvTopic = (CheckBox) findViewById(R.id.tv_topic);
        tvGroup = (CheckBox) findViewById(R.id.tv_group);
    }

    @Override
    protected void initData() {
        mTabIndicator.add(tvDiscover);
        mTabIndicator.add(tvTopic);
        mTabIndicator.add(tvGroup);
        // 发现
        mTabs.add(new DiscoverFragment());
        // 话题
        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.KEY_IS_MAIN, true);
        topicFragment.setArguments(bundle);
        mTabs.add(topicFragment);
        // 圈子
        mTabs.add(new GroupFragment());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    protected void initListener() {
        mViewPager.addOnPageChangeListener(this);
        tvDiscover.setOnClickListener(this);
        tvTopic.setOnClickListener(this);
        tvGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvDiscover) {
            resetTabs(0);
        } else if (v == tvTopic) {
            resetTabs(1);
        } else if (v == tvGroup) {
            resetTabs(2);
        }
    }

    private void resetTabs(int index) {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setChecked(false);
        }
        mTabIndicator.get(index).setChecked(true);
        mViewPager.setCurrentItem(index, false);
    }

    @Override
    public void onBackPressed() {
        // 按返回键时后台运行
        moveTaskToBack(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetTabs(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
