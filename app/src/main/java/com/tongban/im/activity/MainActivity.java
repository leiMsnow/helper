package com.tongban.im.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.widget.view.transformer.ZoomOutPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.discover.DiscoverFragment;
import com.tongban.im.fragment.group.GroupFragment;
import com.tongban.im.fragment.topic.TopicFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 主界面
 */
public class MainActivity extends BaseToolBarActivity
        implements ViewPager.OnPageChangeListener {

    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.tv_discover)
    CheckBox tvDiscover;
    @Bind(R.id.tv_topic)
    CheckBox tvTopic;
    @Bind(R.id.tv_group)
    CheckBox tvGroup;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mTabs = new ArrayList<>();
    private List<CheckBox> mTabIndicator = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityContainer.getInstance().finishActivity(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
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
        vpContent.setAdapter(mAdapter);
        vpContent.setOffscreenPageLimit(3);
        vpContent.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    protected void initListener() {
        vpContent.addOnPageChangeListener(this);
    }

    @OnClick({R.id.tv_discover, R.id.tv_topic, R.id.tv_group})
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
        vpContent.setCurrentItem(index, false);
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
