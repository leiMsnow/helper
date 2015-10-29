package com.tongban.corelib.base.fragment;

import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.SPUtils;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseToolbarFragment extends BaseApiFragment implements IApiCallback,
        RequestApiListener {

    protected String getUserId() {
        return SPUtils.get(mContext, Constants.USER_ID, "").toString();
    }

}
