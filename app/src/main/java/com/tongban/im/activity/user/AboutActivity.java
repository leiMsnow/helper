package com.tongban.im.activity.user;

import android.view.View;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.im.App;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

public class AboutActivity extends BaseToolBarActivity {


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        setTitle("关于我们");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    public void onClick(View v) {
        App.getInstance().saveCrash(true);
        ActivityContainer.getInstance().finishActivity();
    }
}
