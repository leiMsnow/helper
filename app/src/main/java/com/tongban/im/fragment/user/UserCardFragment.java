package com.tongban.im.fragment.user;


import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tb.api.UserCenterApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.user.User;
import com.tongban.corelib.utils.AnimatorUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;
import com.tongban.corelib.widget.view.ptz.PullToZoomBase;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.corelib.widget.view.transformer.ScalePageTransformer;
import com.tongban.im.R;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.common.ModelToTable;
import com.tongban.im.db.helper.UserDaoHelper;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * 个人中心（他人）界面
 *
 * @author fushudi
 */
public class UserCardFragment extends AppBaseFragment implements View.OnClickListener {

    @Bind(R.id.sv_user_center)
    PullToZoomScrollViewEx lvUserCenter;

    @Bind(R.id.tv_title)
    TextView tvUserName;
    //headerView
    CircleImageView ivUserPortrait;
    ViewPager vpChildInfo;
    CirclePageIndicator indicator;
    RelativeLayout rlActionParent;
    protected TextView tvFollowCount;
    LinearLayout rlFollowNum;
    TextView tvFansCount;
    LinearLayout rlFansNum;
    TextView tvGroupCount;
    LinearLayout rlGroupNum;
    LinearLayout vHeaderBottom;
    protected Button ivFocus;
    protected Button ivCancelFocus;
    protected Button ivPrivateChat;

    //zoomView
    ImageView ivZoomTop;

    private UserInfoAdapter mAdapter;

    protected View headView, zoomView, contentView;

    protected User mUserInfo = new User();

    private float alphaValue = 1.0f;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_user_card;
    }

    @Override
    protected void initData() {

        headView = LayoutInflater.from(mContext).inflate(R.layout.ptz_head_view_user_center, null, false);
        zoomView = LayoutInflater.from(mContext).inflate(R.layout.ptz_zoom_view, null, false);
//        contentView = LayoutInflater.from(mContext).inflate(R.layout.ptz_content_view, null, false);

        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
//        lvUserCenter.setScrollContentView(contentView);

        //headView
        ivUserPortrait = (CircleImageView) headView.findViewById(R.id.iv_user_portrait);
        vpChildInfo = (ViewPager) headView.findViewById(R.id.vp_container);
        rlActionParent = (RelativeLayout) getActivity().findViewById(R.id.rl_action_parent);
        indicator = (CirclePageIndicator) headView.findViewById(R.id.lpi_indicator);
        vHeaderBottom = (LinearLayout) headView.findViewById(R.id.ll_relationship);
        tvFansCount = (TextView) headView.findViewById(R.id.tv_fans_num);
        tvFollowCount = (TextView) headView.findViewById(R.id.tv_follow_num);
        tvGroupCount = (TextView) headView.findViewById(R.id.tv_group_num);
        rlFansNum = (LinearLayout) headView.findViewById(R.id.rl_fans_num);
        rlFollowNum = (LinearLayout) headView.findViewById(R.id.rl_follow_num);
        rlGroupNum = (LinearLayout) headView.findViewById(R.id.rl_group_num);
        ivFocus = (Button) headView.findViewById(R.id.iv_focus);
        ivPrivateChat = (Button) headView.findViewById(R.id.iv_private_chat);
        ivCancelFocus = (Button) headView.findViewById(R.id.iv_cancel_focus);
        // contentView
//        tvMyTopic = (TextView) contentView.findViewById(R.id.tv_my_topic);
//        tvMyCollect = (TextView) contentView.findViewById(R.id.tv_my_collect);
//        tvSettings = (TextView) contentView.findViewById(R.id.tv_settings);
        // zoomView
        ivZoomTop = (ImageView) zoomView.findViewById(R.id.iv_zoom_top);

        rlFansNum.setEnabled(false);
        rlFollowNum.setEnabled(false);
        rlGroupNum.setEnabled(false);

        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);

        lvUserCenter.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                float scrollValue = -newScrollValue;
                float headerBottomHeight = vHeaderBottom.getHeight() * 2;
                float startValue = alphaValue;
                if (scrollValue < headerBottomHeight) {
                    alphaValue = (headerBottomHeight - scrollValue) / headerBottomHeight;

                    AnimatorUtils.animatorToAlpha(ivZoomTop, startValue, alphaValue, 10);
                    AnimatorUtils.animatorToAlpha(ivUserPortrait, startValue, alphaValue, 10);
                    AnimatorUtils.animatorToAlpha(vpChildInfo, startValue, alphaValue, 10);
                    AnimatorUtils.animatorToAlpha(rlActionParent, startValue, alphaValue, 10);
                }
            }

            @Override
            public void onPullZoomEnd() {
                AnimatorUtils.animatorToAlpha(ivZoomTop, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(ivUserPortrait, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(vpChildInfo, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(rlActionParent, alphaValue, 1.0f, 300);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alphaValue = 1.0f;
                    }
                }, 300);
            }
        });

        ivUserPortrait.setOnClickListener(this);

        if (getActivity().getIntent() != null) {
            Uri uri = getActivity().getIntent().getData();
            String visitorId = uri.getQueryParameter("visitorId");
            UserCenterApi.getInstance().fetchUserCenterInfo(visitorId, this);
        }


        ivFocus.setOnClickListener(this);
        ivCancelFocus.setOnClickListener(this);
        ivPrivateChat.setOnClickListener(this);

        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlGroupNum.setOnClickListener(this);

    }

    protected void setDataInfo(User user) {
        // 将用户信息保存到本地数据库
        UserDaoHelper.get(mContext).addData(ModelToTable.userToTable(user));
        mUserInfo = user;

        rlFansNum.setEnabled(true);
        rlFollowNum.setEnabled(true);
        rlGroupNum.setEnabled(true);

        if (!TextUtils.isEmpty(mUserInfo.getNick_name())) {
            tvUserName.setText(mUserInfo.getNick_name());
        }

        if (mUserInfo.getPortraitUrl() != null) {
            setUserPortrait(mUserInfo.getPortraitUrl().getMin(), ivUserPortrait);
        } else {
            int resId = (Integer) SPUtils.
                    get(mContext, SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0);
            ivUserPortrait.setImageResource(resId);
        }
        if (mUserInfo.getChildInfo() != null &&
                mUserInfo.getChildInfo().size() > 0) {
            mAdapter = new UserInfoAdapter(mContext, mUserInfo.getChildInfo(), mUserInfo.getUser_id());
            vpChildInfo.setAdapter(mAdapter);
            indicator.setViewPager(vpChildInfo);
            vpChildInfo.setPageTransformer(true, new ScalePageTransformer());
        }
        tvFansCount.setText(String.valueOf(mUserInfo.getFans_amount()));
        tvFollowCount.setText(String.valueOf(mUserInfo.getFocused_amount()));
        tvGroupCount.setText(mUserInfo.getGroupAmount());
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 用户中心（他人）Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.UserCenterEvent obj) {
        setDataInfo(obj.user);
        ivFocus.setVisibility(View.VISIBLE);
        if (mUserInfo.is_focused()) {
            ivPrivateChat.setVisibility(View.VISIBLE);
            ivCancelFocus.setVisibility(View.VISIBLE);
            ivFocus.setVisibility(View.GONE);
        } else {
            ivPrivateChat.setVisibility(View.GONE);
            ivCancelFocus.setVisibility(View.GONE);
            ivFocus.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View v) {
        //关注
        if (v == ivFocus) {
            UserCenterApi.getInstance().focusUser(true, mUserInfo.getUser_id(), this);
        }
        //取消关注
        else if (v == ivCancelFocus) {
            UserCenterApi.getInstance().focusUser(false, mUserInfo.getUser_id(), this);
        }
        //私聊
        else if (v == ivPrivateChat) {
            RongIM.getInstance().startPrivateChat(mContext, mUserInfo.getUser_id(),
                    mUserInfo.getNick_name());
        }
    }

    /**
     * 关注/取消关注事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {

        if (obj.isFocus) {
            ivPrivateChat.setVisibility(View.VISIBLE);
            ivCancelFocus.setVisibility(View.VISIBLE);
            ivFocus.setVisibility(View.GONE);
        } else {
            ivPrivateChat.setVisibility(View.GONE);
            ivCancelFocus.setVisibility(View.GONE);
            ivFocus.setVisibility(View.VISIBLE);
        }
    }
}
