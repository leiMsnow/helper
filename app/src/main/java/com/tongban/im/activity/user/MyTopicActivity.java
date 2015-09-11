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
import com.tongban.corelib.widget.view.transformer.DepthPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.MyCommentTopicFragment;
import com.tongban.im.fragment.user.MyTopicFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * 与我有关的话题（我发起的、我回复的话题）界面
 *
 * @author fushudi
 */
public class MyTopicActivity extends CommonImageResultActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager vpResult;
    private View mIndicator;
    private RelativeLayout rlMySendTopic, rlReplyTopic;
    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mTabs = new ArrayList<>();
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();
    private ChangeColorView ccvMySendTopic;
    private ChangeColorView ccvMyReceiveTopic;
    private TextView tvMySendTopicNum, tvMyCommentTopicNum;

    private int mIndicatorWidth;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_topic;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_topic);
        rlMySendTopic = (RelativeLayout) findViewById(R.id.rl_my_send_topic);
        rlReplyTopic = (RelativeLayout) findViewById(R.id.rl_reply_topic);
        ccvMySendTopic = (ChangeColorView) findViewById(R.id.ccv_my_send_topic);
        ccvMyReceiveTopic = (ChangeColorView) findViewById(R.id.ccv_my_receive_topic);
        mIndicator = findViewById(R.id.v_indicator);
        vpResult = (ViewPager) findViewById(R.id.vp_result);
        ccvMySendTopic.setIconAlpha(1.0f);
        initIndicator(2);
        tvMySendTopicNum = (TextView) findViewById(R.id.tv_my_send_topic_num);
        tvMyCommentTopicNum = (TextView) findViewById(R.id.tv_my_receive_topic_num);
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
        mTabIndicator.add(ccvMySendTopic);
        mTabIndicator.add(ccvMyReceiveTopic);
        //我发起的话题
        MyTopicFragment topicFragment = new MyTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Consts.KEY_MY_TOPIC_LIST, Topic.MY_SEND_TOPIC_LIST);
        topicFragment.setArguments(bundle);
        mTabs.add(topicFragment);
        //回复我的话题
        MyCommentTopicFragment myCommentTopicFragment=new MyCommentTopicFragment();
        mTabs.add(myCommentTopicFragment);
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
        vpResult.setPageTransformer(true, new DepthPageTransformer());
    }

    @Override
    protected void initListener() {
        rlMySendTopic.setOnClickListener(this);
        rlReplyTopic.setOnClickListener(this);
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
        if (v == rlMySendTopic) {
            vpResult.setCurrentItem(0);
        } else if (v == rlReplyTopic) {
            vpResult.setCurrentItem(1);
        }
    }

    /**
     * 我发起的话题Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicListEvent obj) {
        tvMySendTopicNum.setText(String.valueOf(obj.topicList.size()));
    }

    /**
     * 回复我的话题Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CommentTopicListEvent obj) {
        tvMyCommentTopicNum.setText(String.valueOf(obj.commentTopicList.size()));
    }
}
