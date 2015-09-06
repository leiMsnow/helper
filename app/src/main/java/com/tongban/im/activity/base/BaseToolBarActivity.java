package com.tongban.im.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.activity.user.ChildInfoActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.model.GroupType;
import com.tongban.im.model.User;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhangleilei on 15/7/8.
 */
public abstract class BaseToolBarActivity extends BaseApiActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.in_toolbar);
        if (mToolbar == null) {
            return;
        }
        mToolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onComplete(Object obj) {
        super.onComplete(obj);
    }

    @Override
    public void onFailure(DisplayType displayType, Object errorObj) {
        super.onFailure(displayType, errorObj);
    }

    protected void setToolbarTheme(int type) {
        type = -1;
        switch (type) {
            case GroupType.CITY:
                setTheme(R.style.AppTheme_DeepPurple_Base);
                break;
            case GroupType.AGE:
                setTheme(R.style.AppTheme_Pink_Base);
                break;
            case GroupType.LIFE:
                setTheme(R.style.AppTheme_LightGreen_Base);
                break;
            case GroupType.CLASSMATE:
                setTheme(R.style.AppTheme_LightBlue_Base);
                break;
            case GroupType.ACTIVITY:
                setTheme(R.style.AppTheme_Yellow_Base);
                break;
            default:
                setTheme(R.style.AppTheme_White_Base);
                break;
        }
    }

    /**
     * 连接融云IM
     *
     * @param user       用户信息
     * @param isOpenMain 是否打开主界面
     */
    protected void connectIM(final User user, final boolean isOpenMain) {
        RongIM.connect(user.getIm_bind_token(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtil.d("onTokenIncorrect");
                startMain(isOpenMain, user.getChild_info() == null);
            }

            @Override
            public void onSuccess(String s) {
                LogUtil.d("连接RongIM成功，当前用户：" + s);
                startMain(isOpenMain, user.getChild_info() == null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtil.d("连接RongIM失败：" + errorCode.toString());
                startMain(isOpenMain, user.getChild_info() == null);
            }
        });
    }

    /**
     * 连接后跳转到主界面
     *
     * @param user
     */
    protected void connectIM(User user) {
        connectIM(user, true);
    }

    /**
     * 未登录连接
     */
    protected void connectIM() {
        RongIM.connect(SPUtils.get(mContext, Consts.IM_BIND_TOKEN, "").toString(),
                new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtil.d("onTokenIncorrect");
                startMain(true, false);
            }

            @Override
            public void onSuccess(String s) {
                LogUtil.d("连接RongIM成功，当前用户：" + s);
                startMain(true, false);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtil.d("连接RongIM失败：" + errorCode.toString());
                startMain(true, false);
            }
        });
    }


    private void startMain(boolean isOpenMain, boolean isSetChildInfo) {
        if (isSetChildInfo) {
            startActivity(new Intent(this, ChildInfoActivity.class));
        } else if (isOpenMain) {
            RongCloudEvent.getInstance().setOtherListener();
            startActivity(new Intent(mContext, MainActivity.class));
        }
        finish();
    }

}
