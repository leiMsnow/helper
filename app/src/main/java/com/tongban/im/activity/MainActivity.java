package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.corelib.widget.view.transformer.ZoomOutPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.discover.DiscoverFragment;
import com.tongban.im.fragment.group.GroupFragment;
import com.tongban.im.fragment.topic.TopicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseToolBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();
    private ChangeColorView ccvDiscover, ccvCircle, ccvTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.getInstance().finishActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ActivityContainer.getInstance().finishActivity(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_content);

        ccvDiscover = (ChangeColorView) findViewById(R.id.ccv_discover);
        ccvCircle = (ChangeColorView) findViewById(R.id.ccv_circle);
        ccvTopic = (ChangeColorView) findViewById(R.id.ccv_topic);

        ccvDiscover.setIconAlpha(1.0f);
    }

    @Override
    protected void initData() {
        mTabIndicator.add(ccvDiscover);
        mTabIndicator.add(ccvTopic);
        mTabIndicator.add(ccvCircle);
        /** 发现 */
        mTabs.add(new DiscoverFragment());
        /** 话题 */
        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.KEY_IS_MAIN, true);
        topicFragment.setArguments(bundle);
        mTabs.add(topicFragment);
        /** 圈子 */
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
        ccvDiscover.setOnClickListener(this);
        ccvTopic.setOnClickListener(this);
        ccvCircle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ccvDiscover) {
            resetTabs(0);
        } else if (v == ccvTopic) {
            resetTabs(1);
        } else if (v == ccvCircle) {
            resetTabs(2);
        }
    }

    private void resetTabs(int index) {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0.0f);
        }
        mTabIndicator.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);
        showEmptyText("",false);
    }

    @Override
    public void onBackPressed() {
        // 按返回键时后台运行
        moveTaskToBack(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            mTabIndicator.get(position).setIconAlpha(1 - positionOffset);
            mTabIndicator.get(position + 1).setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        resetTabs(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
