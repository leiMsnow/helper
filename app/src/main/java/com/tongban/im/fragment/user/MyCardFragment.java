package com.tongban.im.fragment.user;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.im.R;
import com.tongban.im.activity.SettingActivity;
import com.tongban.im.activity.user.MyCollectActivity;
import com.tongban.im.activity.user.MyTopicActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 个人中心界面
 *
 * @author fushudi
 */
public class MyCardFragment extends UserBaseFragment {
    @Override
    protected void initData() {
        super.initData();
        tvMyTopic.setVisibility(View.VISIBLE);
        tvMyCollect.setVisibility(View.VISIBLE);
        tvSettings.setVisibility(View.VISIBLE);

        tvMyTopic.setOnClickListener(this);
        tvMyCollect.setOnClickListener(this);
        tvSettings.setOnClickListener(this);

        UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
    }

    @Override
    public void onClick(View v) {
        //跳转到话题界面
        if (v == tvMyTopic) {
            startActivity(new Intent(mContext, MyTopicActivity.class));
        }
        //跳转到我的收藏界面
        else if (v == tvMyCollect) {
            startActivity(new Intent(mContext, MyCollectActivity.class));
        }
        //跳转到设置界面
        else if (v == tvSettings) {
            startActivity(new Intent(mContext, SettingActivity.class));
        }else{
            super.onClick(v);
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
