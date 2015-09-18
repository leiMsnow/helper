package com.tongban.im.fragment.base;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.common.Consts;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseToolBarFragment extends BaseApiFragment implements IApiCallback {

    /**
     * 设置用户头像信息
     *
     * @param uri  网络地址
     * @param view imageView控件
     */
    public void setUserPortrait(String uri, ImageView view) {
        Glide.with(BaseToolBarFragment.this).load(uri).error(Consts.getUserDefaultPortrait()).into(view);
    }
}
