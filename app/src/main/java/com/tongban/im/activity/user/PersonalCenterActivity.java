package com.tongban.im.activity.user;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.UserBaseActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends UserBaseActivity implements View.OnClickListener {

    private TextView tvMyTopic, tvMyCollect, tvFeedBack, tvSettings;

    @Override
    protected void initView() {
        super.initView();
        //contentView
        tvMyTopic = (TextView) contentView.findViewById(R.id.tv_my_topic);
        tvMyCollect = (TextView) contentView.findViewById(R.id.tv_my_collect);
        tvFeedBack = (TextView) contentView.findViewById(R.id.tv_feedback);
        tvSettings = (TextView) contentView.findViewById(R.id.tv_settings);

        tvMyTopic.setVisibility(View.VISIBLE);
        tvMyCollect.setVisibility(View.VISIBLE);
        tvFeedBack.setVisibility(View.VISIBLE);
        tvSettings.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tvSetChildInfo.setOnClickListener(this);
        tvMyCollect.setOnClickListener(this);
        tvMyTopic.setOnClickListener(this);
        tvFeedBack.setOnClickListener(this);
        tvSettings.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        //跳转到话题界面
        if (v == tvMyTopic) {
            startActivity(new Intent(this, MyTopicActivity.class));
        }
        //跳转到我的收藏界面
        else if (v == tvMyCollect) {
            startActivity(new Intent(this, MyCollectActivity.class));
        }
        //跳转到意见反馈
        else if (v == tvFeedBack) {
            startActivity(new Intent(mContext, FeedbackActivity.class));
        }
        //设置 // TODO: 9/1/15 暂时写为注销登录
        //跳转到设置界面
        else if (v == tvSettings) {
//            startActivity(new Intent(mContext, SettingActivity.class));

//            //只清除默认文件中的SP，VISIT不会被清除
            SPUtils.clear(mContext);
            TransferCenter.getInstance().startLogin(true);
        }
        //跳转到设置宝宝信息界面
        else if (v == tvSetChildInfo) {
            startActivity(new Intent(this, ChildInfoActivity.class));
        }
    }


    /**
     * 返回个人中心数据Event
     */
    public void onEventMainThread(BaseEvent.PersonalCenterEvent obj) {
        setDataInfo(obj.user);
    }

    /**
     * 关注Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {

        if (obj.isFocus) {
            tvFollowCount.setText(String.valueOf(
                    Integer.parseInt(tvFollowCount.getText().toString()) + 1));
        } else {
            tvFollowCount.setText(String.valueOf(
                    Integer.parseInt(tvFollowCount.getText().toString()) - 1));
        }
    }

}
