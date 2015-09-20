package com.tongban.im.activity.base;

import android.content.Intent;

import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.user.AddChildInfo;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhangleilei on 15/7/8.
 */
public abstract class AccountBaseActivity extends BaseToolBarActivity {

    /**
     * 连接融云IM
     *
     * @param isOpenMain 是否打开主界面
     * @param isChild    是否需要上传宝宝信息
     */
    protected void connectIM(final boolean isOpenMain, final boolean isChild) {
        RongIM.connect(SPUtils.get(mContext, Consts.IM_BIND_TOKEN, "").toString(),
                new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        LogUtil.d("onTokenIncorrect");
                        startMain(isOpenMain, isChild);
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtil.d("连接RongIM成功，当前用户：" + s);
                        startMain(isOpenMain, isChild);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        LogUtil.d("连接RongIM失败：" + errorCode.toString());
                        startMain(isOpenMain, isChild);
                    }
                });
    }

    protected void connectIM(boolean isChild) {
        connectIM(true, isChild);
    }

    /**
     * 未登录连接
     */
    protected void connectIM() {
        connectIM(true, false);
    }

    private void startMain(boolean isOpenMain, boolean isSetChildInfo) {
        if (isSetChildInfo) {
            //添加宝宝信息
            int childSex = (int) SPUtils.get(mContext, SPUtils.VISIT_FILE,
                    Consts.CHILD_SEX, 1);
            String childBirthday = SPUtils.get(mContext, SPUtils.VISIT_FILE,
                    Consts.CHILD_BIRTHDAY, "").toString();
            AddChildInfo childInfo = new AddChildInfo();
            childInfo.setBirthday(childBirthday);
            childInfo.setSex(childSex);
            List<AddChildInfo> children = new ArrayList<>();
            children.add(childInfo);
            UserCenterApi.getInstance().setChildInfo(SPUtils.get(mContext, Consts.USER_ID, "")
                    .toString(), children, null);
        }
        if (isOpenMain) {
            RongCloudEvent.getInstance().setOtherListener();
            startActivity(new Intent(mContext, MainActivity.class));
        }
        finish();
    }

}
