package com.tongban.im.fragment.user;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tb.api.model.user.User;
import com.tb.api.utils.TransferCenter;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.user.MyInfoActivity;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import butterknife.Bind;

public class UserBaseFragment extends BaseToolBarFragment implements View.OnClickListener {
    @Bind(R.id.sv_user_center)
    NestedScrollView lvUserCenter;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    //headerView
    @Bind(R.id.iv_user_portrait)
    FloatingActionButton ivUserPortrait;

//    @Bind(R.id.rl_action_parent)
//    RelativeLayout rlActionParent;
    @Bind(R.id.tv_follow_num)
    protected TextView tvFollowCount;
    @Bind(R.id.rl_follow_num)
    LinearLayout rlFollowNum;
    @Bind(R.id.tv_fans_num)
    TextView tvFansCount;
    @Bind(R.id.rl_fans_num)
    LinearLayout rlFansNum;
    @Bind(R.id.tv_group_num)
    TextView tvGroupCount;
    @Bind(R.id.rl_group_num)
    LinearLayout rlGroupNum;
    @Bind(R.id.ll_relationship)
    LinearLayout vHeaderBottom;
    // contentView
    @Bind(R.id.tv_my_topic)
    protected TextView tvMyTopic;
    @Bind(R.id.tv_my_collect)
    protected TextView tvMyCollect;
    @Bind(R.id.tv_settings)
    protected TextView tvSettings;



    protected View headView, zoomView, contentView;

    protected User mUserInfo = new User();

    private float alphaValue = 1.0f;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_personal_center2;
    }

    @Override
    protected void initData() {
        rlFansNum.setEnabled(false);
        rlFollowNum.setEnabled(false);
        rlGroupNum.setEnabled(false);

        ivUserPortrait.setOnClickListener(this);

        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlGroupNum.setOnClickListener(this);

    }

    public void onClick(View v) {
        //跳转到粉丝界面
        if (v == rlFansNum) {
            TransferCenter.getInstance().startRelationship(Consts.TAG_FANS,
                    mUserInfo.getUser_id());
        }
        //跳转到关注列表界面
        else if (v == rlFollowNum) {
            TransferCenter.getInstance().startRelationship(Consts.TAG_FOLLOW,
                    mUserInfo.getUser_id());
        }
        //我的圈子
        else if (v == rlGroupNum) {
            TransferCenter.getInstance().startMyGroupList(mUserInfo.getUser_id());
        }
        //跳转到个人资料页
        else if (v == ivUserPortrait) {
            if (SPUtils.get(mContext, Consts.USER_ID, "").equals(mUserInfo.getUser_id()))
                mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
        }

    }

    protected void setDataInfo(User user) {
        mUserInfo = user;

        rlFansNum.setEnabled(true);
        rlFollowNum.setEnabled(true);
        rlGroupNum.setEnabled(true);

        if (!TextUtils.isEmpty(mUserInfo.getNick_name())) {
//            tvUserName.setText(mUserInfo.getNick_name());
            mCollapsingToolbarLayout.setTitle(mUserInfo.getNick_name());
        }

        if (mUserInfo.getPortraitUrl() != null) {
            setUserPortrait(mUserInfo.getPortraitUrl().getMin(), ivUserPortrait);
        } else {
            int resId = (Integer) SPUtils.
                    get(mContext, SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0);
//            ivUserPortrait.setImageResource(resId);
            ivUserPortrait.setBackgroundResource(resId);
        }
        tvFansCount.setText(String.valueOf(mUserInfo.getFans_amount()));
        tvFollowCount.setText(String.valueOf(mUserInfo.getFocused_amount()));
        tvGroupCount.setText(mUserInfo.getGroupAmount());
    }
}
