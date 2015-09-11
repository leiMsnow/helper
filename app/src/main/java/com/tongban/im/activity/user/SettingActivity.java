package com.tongban.im.activity.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.TransferCenter;

import io.rong.imkit.RongIM;

public class SettingActivity extends BaseToolBarActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private LinearLayout llClearCache;
    private TextView tvClearCacheNum;
    private CheckBox chbMessageNotify;
    private TextView tvAboutMe;
    private Button btnLogout;


    @Override

    protected int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        setTitle(R.string.settings);
        llClearCache = (LinearLayout) findViewById(R.id.ll_clear_cache);
        tvClearCacheNum = (TextView) findViewById(R.id.tv_clear_cache_num);
        chbMessageNotify = (CheckBox) findViewById(R.id.chb_notify);
        tvAboutMe = (TextView) findViewById(R.id.tv_about_me);
        btnLogout = (Button) findViewById(R.id.btn_login_or_register);
        btnLogout.setEnabled(true);
    }

    @Override
    protected void initData() {
        long usableSpace = Glide.getPhotoCacheDir(mContext).getUsableSpace();
        tvClearCacheNum.setText(String.valueOf(usableSpace));
    }

    @Override
    protected void initListener() {
        llClearCache.setOnClickListener(this);
        chbMessageNotify.setOnCheckedChangeListener(this);
        tvAboutMe.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == llClearCache) {
            ToastUtil.getInstance(mContext).showToast("清除缓存");
        } else if (v == tvAboutMe) {
            startActivity(new Intent(mContext, AboutMeActivity.class));
        } else if (v == btnLogout) {
            if (RongIM.getInstance() != null)
                RongIM.getInstance().logout();
            SPUtils.clear(mContext);
            TransferCenter.getInstance().startLogin(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ToastUtil.getInstance(mContext).showToast("新消息通知");
        } else {
            ToastUtil.getInstance(mContext).showToast("不要通知");
        }
    }
}
