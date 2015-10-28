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

import com.tb.api.model.BaseEvent;
import com.tb.api.model.topic.Topic;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.MyTopicFragment;
import com.tongban.im.fragment.user.ProductListFragment;
import com.tongban.im.fragment.user.ThemeListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * 我的收藏界面
 *
 * @author fushudi
 */
public class MyCollectActivity extends AppBaseActivity {

    @Bind(R.id.ccv_theme)
    ChangeColorView ccvMultiple;
    @Bind(R.id.tv_multiple_product_num)
    TextView tvMultiProductNum;
    @Bind(R.id.rl_multiple_product)
    RelativeLayout rlMultipleProduct;
    @Bind(R.id.ccv_product)
    ChangeColorView ccvSingle;
    @Bind(R.id.tv_single_product_num)
    TextView tvSingleProductNum;
    @Bind(R.id.rl_single_product)
    RelativeLayout rlSingleProduct;
    @Bind(R.id.ccv_topic)
    ChangeColorView ccvTopic;
    @Bind(R.id.tv_collect_topic_num)
    TextView tvCollectTopicNum;
    @Bind(R.id.rl_topic)
    RelativeLayout rlTopic;
    @Bind(R.id.v_indicator)
    View mIndicator;
    @Bind(R.id.vp_result)
    ViewPager vpResult;

    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mTabs = new ArrayList<>();
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();

    private int mIndicatorWidth;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_collect;
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

        setTitle(R.string.my_collect);

        ccvMultiple.setIconAlpha(1.0f);
        initIndicator(3);

        mTabIndicator.add(ccvMultiple);
        mTabIndicator.add(ccvSingle);
        mTabIndicator.add(ccvTopic);
        //专题结果
        mTabs.add(ThemeListFragment.newInstance(1));
        //单品结果
        mTabs.add(ProductListFragment.newInstance(1));
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
        vpResult.setOffscreenPageLimit(3);
    }

    @OnPageChange(value = R.id.vp_result,callback = OnPageChange.Callback.PAGE_SCROLLED)
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            mTabIndicator.get(position).setIconAlpha(1 - positionOffset);
            mTabIndicator.get(position + 1).setIconAlpha(positionOffset);
        }

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
        lp.leftMargin = (int) (mIndicatorWidth * (position + positionOffset));
        mIndicator.setLayoutParams(lp);
    }

    @OnClick({R.id.rl_multiple_product,R.id.rl_single_product,R.id.rl_topic})
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
        tvCollectTopicNum.setText(String.valueOf(obj.topicList.size()));
    }

    /**
     * 我的收藏单品Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FetchCollectedProductEvent obj) {
        tvSingleProductNum.setText(String.valueOf(obj.productBookList.size()));
    }

    /**
     * 我的收藏专题Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FetchCollectedThemeEvent obj) {
        tvMultiProductNum.setText(String.valueOf(obj.mThemeList.size()));
    }
}
