package com.tongban.im.activity.user;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.InputChildInfoFragment;
import com.tongban.im.fragment.user.SetChildPortraitFragment;
import com.tongban.im.model.BaseEvent;

/**
 * 设置宝宝信息界面
 *
 * @author fushudi
 */
public class ChildInfoActivity extends CameraResultActivity {

    private InputChildInfoFragment mInputChildInfoFragment;
//    private SetChildPortraitFragment mSetChildPortraitFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_child_info;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        //填写宝宝信息界面
        mInputChildInfoFragment = new InputChildInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_replaced, mInputChildInfoFragment);
        transaction.commit();
    }

    @Override
    protected void initListener() {

    }

    /**
     * 宝宝信息Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.ChildCreateEvent obj) {
        SPUtils.put(mContext,Consts.KEY_CHILD_BIRTHDAY,obj.childBirthday);
        SPUtils.put(mContext,Consts.KEY_CHILD_SEX,obj.childSex);
        SPUtils.put(mContext, Consts.FIRST_SET_CHILD_INFO, false);
        finish();
        connectIM();
    }

}
