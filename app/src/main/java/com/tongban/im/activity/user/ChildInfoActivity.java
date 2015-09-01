package com.tongban.im.activity.user;


import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.user.InputChildInfoFragment;
import com.tongban.im.fragment.user.SetChildPortraitFragment;

public class ChildInfoActivity extends BaseToolBarActivity implements View.OnClickListener {
    private static final int FRAGMENT_INPUT_CHILD_INFO = 1;
    private static final int FRAGMENT_SET_PORTRAIT = 2;
    private InputChildInfoFragment mInputChildInfoFragment;
    private SetChildPortraitFragment mSetChildPortraitFragment;
    private FrameLayout flReplacedFragment;
    private Button btnIsOk;

    private int currentFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_child_info;
    }

    @Override
    protected void initView() {
        flReplacedFragment = (FrameLayout) findViewById(R.id.fl_replaced);
        btnIsOk = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        //填写宝宝信息界面
        currentFragment = FRAGMENT_INPUT_CHILD_INFO;
        mInputChildInfoFragment = new InputChildInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_replaced, mInputChildInfoFragment);
        transaction.commit();
    }

    @Override
    protected void initListener() {
        btnIsOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Fragment后退
        if (v.getId() == android.R.id.home) {
            currentFragment -= 1;
            if (currentFragment <= 1) {
                currentFragment = 1;
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
        //点击OK按钮
        else if (v == btnIsOk) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //设置宝宝头像界面
            if (currentFragment == FRAGMENT_INPUT_CHILD_INFO) {
                currentFragment = FRAGMENT_SET_PORTRAIT;
                mSetChildPortraitFragment = new SetChildPortraitFragment();
                transaction.replace(R.id.fl_replaced, mSetChildPortraitFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            //设置成功
            else if (currentFragment == FRAGMENT_SET_PORTRAIT) {
                ToastUtil.getInstance(mContext).showToast("设置宝宝信息成功");
            }
        }
    }
}
