package com.tongban.im.activity.user;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.InputChildInfoFragment;
import com.tongban.im.fragment.user.SetChildPortraitFragment;
import com.tongban.im.model.BaseEvent;

public class ChildInfoActivity extends CameraResultActivity {

    private InputChildInfoFragment mInputChildInfoFragment;
    private SetChildPortraitFragment mSetChildPortraitFragment;

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
        mSetChildPortraitFragment = new SetChildPortraitFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Consts.KEY_CHILD_NAME, obj.childName);
        bundle.putString(Consts.KEY_CHILD_BIRTHDAY, obj.childBirthday);
        bundle.putString(Consts.KEY_CHILD_SEX, obj.childSex);
        mSetChildPortraitFragment.setArguments(bundle);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fl_replaced, mSetChildPortraitFragment);
        transaction.commit();
    }

}
