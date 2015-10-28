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
import com.tongban.corelib.widget.view.transformer.ZoomInPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.MyCommentTopicFragment;
import com.tongban.im.fragment.user.MyTopicFragment;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * 与我有关的话题（我发起的、我回复的话题）界面
 *
 * @author fushudi
 */
public class MyTopicActivity extends CommonImageResultActivity {

    @Bind(R.id.ccv_my_send_topic)
    ChangeColorView ccvMySendTopic;
    @Bind(R.id.tv_my_send_topic_num)
    TextView tvMySendTopicNum;
    @Bind(R.id.rl_my_send_topic)
    RelativeLayout rlMySendTopic;
    @Bind(R.id.ccv_my_receive_topic)
    ChangeColorView ccvMyReceiveTopic;
    @Bind(R.id.tv_my_receive_topic_num)
    TextView tvMyCommentTopicNum;
    @Bind(R.id.rl_reply_topic)
    RelativeLayout rlReplyTopic;
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
        return R.layout.activity_my_topic;
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
        setTitle(R.string.my_topic);
        ccvMySendTopic.setIconAlpha(1.0f);
        initIndicator(2);
        mTabIndicator.add(ccvMySendTopic);
        mTabIndicator.add(ccvMyReceiveTopic);
        //我发起的
        MyTopicFragment topicFragment = new MyTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Consts.KEY_MY_TOPIC_LIST, Topic.MY_SEND_TOPIC_LIST);
        topicFragment.setArguments(bundle);
        mTabs.add(topicFragment);
        //回复我的
        MyCommentTopicFragment myCommentTopicFragment = new MyCommentTopicFragment();
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
        vpResult.setPageTransformer(true, new ZoomInPageTransformer());
    }

    @OnPageChange(value = R.id.vp_result, callback = OnPageChange.Callback.PAGE_SCROLLED)
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            mTabIndicator.get(position).setIconAlpha(1 - positionOffset);
            mTabIndicator.get(position + 1).setIconAlpha(positionOffset);
        }

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
        lp.leftMargin = (int) (mIndicatorWidth * (position + positionOffset));
        mIndicator.setLayoutParams(lp);
    }

    @OnClick({R.id.rl_my_send_topic, R.id.rl_reply_topic})
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
        int count = Integer.parseInt(tvMySendTopicNum.getText().toString()) +
                obj.topicList.size();
        tvMySendTopicNum.setText(String.valueOf(count));
    }

    /**
     * 回复我的话题Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CommentTopicListEvent obj) {
        int count = Integer.parseInt(tvMyCommentTopicNum.getText().toString()) +
                obj.commentTopicList.size();
        tvMyCommentTopicNum.setText(String.valueOf(count));
    }
}
