package com.tongban.im.activity.base;

import android.content.Intent;

import com.tb.api.UserCenterApi;
import com.tb.api.model.user.AddChildInfo;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.common.Consts;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhangleilei on 15/7/8.
 */
public abstract class AccountBaseActivity extends CommonImageResultActivity {

    /**
     * 连接融云IM
     *
     */
    protected void connectIM() {
        RongIM.connect(SPUtils.get(mContext, Constants.IM_BIND_TOKEN, "ogq34eaythOy6oZ2R5IK3z6P6IK0BQfgiphpw86bO2beDmcaKTkdGFaOuKAL+3arhw6UOS1FNioU6m4Ke23qudWgFJe9Ja9lITBCLqqRFxE3J6Ix0gmg4Q==").toString(),
                new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        LogUtil.d("onTokenIncorrect");
                        startMain();
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtil.d("连接RongIM成功，当前用户：" + s);
                        startMain();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        LogUtil.d("连接RongIM失败：" + errorCode.toString());
                        startMain();
                    }
                });
    }

    private void startMain() {

        RongCloudEvent.getInstance().setOtherListener();
        startActivity(new Intent(mContext, MainActivity.class));

    }

}
