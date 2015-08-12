package com.tongban.im.fragment;


import android.widget.ImageView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.widget.view.CircleImageDrawable;
import com.tongban.im.R;

/**
 * 发现页
 * author: chenenyu 15/7/13
 */
public class DiscoverFragment extends BaseApiFragment {

    private ImageView ivUserIcon;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView() {
        ivUserIcon = (ImageView) mView.findViewById(R.id.iv_user_icon);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }


}
