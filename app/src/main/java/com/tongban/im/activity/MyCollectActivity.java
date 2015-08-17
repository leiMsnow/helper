package com.tongban.im.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.MultipleProductFragment;
import com.tongban.im.fragment.SingleProductFragment;
import com.tongban.im.fragment.TopicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏界面
 *
 * @author fushudi
 */
public class MyCollectActivity extends BaseToolBarActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vpResult;
    private View mIndicator;
    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mTabs = new ArrayList<>();
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();
    private ChangeColorView ccvMultiple;
    private ChangeColorView ccvSingle;
    private ChangeColorView ccvTopic;

    private int mIndicatorWidth;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initView() {
        ccvMultiple = (ChangeColorView) findViewById(R.id.ccv_multiple_product);
        ccvSingle = (ChangeColorView) findViewById(R.id.ccv_single_product);
        ccvTopic = (ChangeColorView) findViewById(R.id.ccv_topic);
        mIndicator = findViewById(R.id.v_indicator);
        vpResult = (ViewPager) findViewById(R.id.vp_result);
        ccvMultiple.setIconAlpha(1.0f);
        initIndicator(3);
    }

    /**
     * 设置指示器宽度
     *
     * @param count 分割数量
     */
    private void initIndicator(int count) {
        mIndicatorWidth = ScreenUtils.getScreenWidth(mContext) / count;
        ViewGroup.LayoutParams lp = mIndicator.getLayoutParams();
        lp.width = mIndicatorWidth;
        mIndicator.setLayoutParams(lp);
    }

    @Override
    protected void initData() {
        mTabIndicator.add(ccvMultiple);
        mTabIndicator.add(ccvSingle);
        mTabIndicator.add(ccvTopic);
        //专题结果
        mTabs.add(new MultipleProductFragment());
        //单品结果
        mTabs.add(new SingleProductFragment());
        //话题结果
        TopicFragment topicFragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Consts.KEY_TOPIC_TOOLBAR_DISPLAY,false);
        topicFragment.setArguments(bundle);
        mTabs.add(topicFragment);

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
        vpResult.setAdapter(mAdapter);
        vpResult.addOnPageChangeListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            mTabIndicator.get(position).setIconAlpha(1 - positionOffset);
            mTabIndicator.get(position + 1).setIconAlpha(positionOffset);
        }

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
        lp.leftMargin = (int) (mIndicatorWidth * (position + positionOffset));
        mIndicator.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
