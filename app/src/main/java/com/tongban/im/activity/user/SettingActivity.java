package com.tongban.im.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;

import io.rong.imkit.RongIM;

public class SettingActivity extends BaseToolBarActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    private LinearLayout llClearCache;
    private TextView tvClearCacheNum, tvCurrentVersion;
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
        tvCurrentVersion = (TextView) findViewById(R.id.tv_current_version_img);
        chbMessageNotify = (CheckBox) findViewById(R.id.chb_notify);
        tvAboutMe = (TextView) findViewById(R.id.tv_about_me);
        btnLogout = (Button) findViewById(R.id.btn_login_or_register);
        btnLogout.setEnabled(true);
    }

    @Override
    protected void initData() {
        long usableSpace = Glide.getPhotoCacheDir(mContext).getUsableSpace();
        float usableSpaceMB = usableSpace / (1024 * 1024 * 1024);
        tvClearCacheNum.setText(String.valueOf(usableSpaceMB) + "MB");
        tvCurrentVersion.setText("V" + AppUtils.getVersionName(mContext));
    }

    @Override
    protected void initListener() {
        llClearCache.setOnClickListener(this);
        chbMessageNotify.setOnCheckedChangeListener(this);
        tvAboutMe.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        tvCurrentVersion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == llClearCache) {
            Glide.get(mContext).clearMemory();
            tvClearCacheNum.setText(0 + "MB");
        } else if (v == tvAboutMe) {
            startActivity(new Intent(mContext, AboutActivity.class));
        } else if (v == btnLogout) {
            if (RongIM.getInstance() != null)
                RongIM.getInstance().logout();
            SPUtils.clear(mContext);
            ActivityContainer.getInstance().finishActivity();
            TransferCenter.getInstance().startLogin(true,false);
        } else if (v == tvCurrentVersion) {
            BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
            dialog.setMessage("切换环境");
            dialog.setPositiveButton("正式环境",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            BaseApi.getInstance().setHostUrl(mContext, 0);
                        }
                    });
            dialog.setNegativeButton("测试环境",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            BaseApi.getInstance().setHostUrl(mContext, 1);
                        }
                    });
            dialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ToastUtil.getInstance(mContext).showToast("新消息通知");
            SPUtils.put(mContext, Consts.KEY_MESSAGE_NOTIFY, 1);
        } else {
            ToastUtil.getInstance(mContext).showToast("不要通知");
            SPUtils.put(mContext, Consts.KEY_MESSAGE_NOTIFY, 0);
        }
    }
}
