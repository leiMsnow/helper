package com.tongban.im.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingActivity extends BaseToolBarActivity implements
        CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.tv_clear_cache_num)
    TextView tvClearCacheNum;
    @Bind(R.id.ll_clear_cache)
    LinearLayout llClearCache;
    @Bind(R.id.chb_notify)
    CheckBox chbMessageNotify;
    @Bind(R.id.tv_current_version)
    TextView tvCurrentVersion;
    @Bind(R.id.tv_about_me)
    TextView tvAboutMe;
    @Bind(R.id.btn_login_or_register)
    Button btnLogout;

    @Override

    protected int getLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

        setTitle(R.string.settings);
        btnLogout.setEnabled(true);

        long usableSpace = Glide.getPhotoCacheDir(mContext).getUsableSpace();
        float usableSpaceMB = usableSpace / (1024 * 1024 * 1024);
        tvClearCacheNum.setText(String.valueOf(usableSpaceMB) + "MB");
        tvCurrentVersion.setText("V" + AppUtils.getVersionName(mContext));
    }


    @OnClick({R.id.ll_clear_cache, R.id.tv_about_me, R.id.btn_login_or_register
            , R.id.tv_current_version})
    public void onClick(View v) {
        // 清除缓存
        if (v == llClearCache) {
            Glide.get(mContext).clearMemory();
            tvClearCacheNum.setText(0 + "MB");
        }
        // 关于我们
        else if (v == tvAboutMe) {
            startActivity(new Intent(mContext, AboutActivity.class));
        }
        // 注销
        else if (v == btnLogout) {
            logout();
        }
        // 当前版本
        else if (v == tvCurrentVersion) {
            BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
            dialog.setMessage("切换环境");
            dialog.setPositiveButton("正式环境",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            BaseApi.getInstance().setHostUrl(mContext, BaseApi.DEFAULT_HOST);
                            logout();
                        }
                    });
            dialog.setNegativeButton("测试环境",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            BaseApi.getInstance().setHostUrl(mContext, BaseApi.TEST_HOST);
                            logout();
                        }
                    });
            dialog.show();
        }
    }

    @OnCheckedChanged(R.id.chb_notify)
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
