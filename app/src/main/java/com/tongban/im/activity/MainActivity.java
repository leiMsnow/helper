package com.tongban.im.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.CircleFragment;
import com.tongban.im.fragment.DiscoverFragment;
import com.tongban.im.fragment.TopicFragment;
import com.tongban.im.model.BaseEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主界面
 */
public class MainActivity extends BaseToolBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    // 圈子页顶部的tab
//    private RadioGroup rgCircle;
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();
    private ChangeColorView ccvDiscover, ccvCircle, ccvTopic;

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
        // 圈子页的顶部tab
//        rgCircle = (RadioGroup) findViewById(R.id.rg_circle);

        ccvDiscover.setIconAlpha(1.0f);
    }

    @Override
    protected void initData() {

        mTabIndicator.add(ccvDiscover);
        mTabIndicator.add(ccvTopic);
        mTabIndicator.add(ccvCircle);
        /** 发现 */
        Fragment fragment = new DiscoverFragment();
        mTabs.add(fragment);
        /** 话题 */
        fragment = new TopicFragment();
        mTabs.add(fragment);
        /** 圈子 */
        fragment = new CircleFragment();
        mTabs.add(fragment);

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
    }

    @Override
    protected void initListener() {
        mViewPager.addOnPageChangeListener(this);
        ccvDiscover.setOnClickListener(this);
        ccvTopic.setOnClickListener(this);
        ccvCircle.setOnClickListener(this);

//        rgCircle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_chat:
//                        EventBus.getDefault().post(BaseEvent.SwitchCircleTabEvent.CHAT);
//                        break;
//                    case R.id.rb_recommend:
//                        EventBus.getDefault().post(BaseEvent.SwitchCircleTabEvent.RECOMMEND);
//                        break;
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
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
//        if (index == 2) {
//            rgCircle.setVisibility(View.VISIBLE);
//        } else {
//            rgCircle.setVisibility(View.GONE);
//        }

        mTabIndicator.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);

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
