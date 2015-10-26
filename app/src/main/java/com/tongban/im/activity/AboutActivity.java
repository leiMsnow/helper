package com.tongban.im.activity;

import android.view.View;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.im.App;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;

public class AboutActivity extends AppBaseActivity {


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        setTitle("关于我们");

    }

    public void onClick(View v) {
        App.getInstance().saveCrash(true);
        ActivityContainer.getInstance().finishActivity();
    }
}
