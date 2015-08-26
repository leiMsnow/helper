package com.tongban.im.activity.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.MultipleProductFragment;
import com.tongban.im.fragment.user.MyTopicFragment;
import com.tongban.im.fragment.user.SingleProductFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏界面
 *
 * @author fushudi
 */
public class MyCollectActivity extends BaseToolBarActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener {

    private RelativeLayout rlMultipleProduct, rlSingleProduct, rlTopic;
    private ViewPager vpResult;
    private View mIndicator;
    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mTabs = new ArrayList<>();
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();
    private ChangeColorView ccvMultiple;
    private ChangeColorView ccvSingle;
    private ChangeColorView ccvTopic;
    private TextView tvMultiProductNum, tvSingleProductNum, tvCollectTopicNum;

    private int mIndicatorWidth;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_collect);
        rlMultipleProduct = (RelativeLayout) findViewById(R.id.rl_multiple_product);
        rlSingleProduct = (RelativeLayout) findViewById(R.id.rl_single_product);
        rlTopic = (RelativeLayout) findViewById(R.id.rl_topic);

        ccvMultiple = (ChangeColorView) findViewById(R.id.ccv_multiple_product);
        ccvSingle = (ChangeColorView) findViewById(R.id.ccv_single_product);
        ccvTopic = (ChangeColorView) findViewById(R.id.ccv_topic);
        mIndicator = findViewById(R.id.v_indicator);
        vpResult = (ViewPager) findViewById(R.id.vp_result);
        ccvMultiple.setIconAlpha(1.0f);
        initIndicator(3);

        tvMultiProductNum = (TextView) findViewById(R.id.tv_multiple_product_num);
        tvSingleProductNum = (TextView) findViewById(R.id.tv_single_product_num);
        tvCollectTopicNum = (TextView) findViewById(R.id.tv_collect_topic_num);
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
        MyTopicFragment topicFragment = new MyTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Consts.KEY_MY_TOPIC_LIST, Topic.MY_COLLECT_TOPIC_LIST);
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
        rlMultipleProduct.setOnClickListener(this);
        rlSingleProduct.setOnClickListener(this);
        rlTopic.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v == rlMultipleProduct) {
            resetTabs(0);
        } else if (v == rlSingleProduct) {
            resetTabs(1);
        } else if (v == rlTopic) {
            resetTabs(2);
        }
    }

    private void resetTabs(int index) {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0.0f);
        }
        mTabIndicator.get(index).setIconAlpha(1.0f);
        vpResult.setCurrentItem(index, false);
    }

    /**
     * 我收藏的话题Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicListEvent obj) {
        tvCollectTopicNum.setText(String.valueOf(obj.getTopicList().size()));
    }

    /**
     * 我的收藏单品Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CollectSingleProductEvent obj) {
        tvSingleProductNum.setText(String.valueOf(obj.getSingleProductList().size()));
    }
    /**
     * 我的收藏专题Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CollectMultiProductEvent obj) {
        tvMultiProductNum.setText(String.valueOf(obj.getMultiProductList().size()));
    }
}
