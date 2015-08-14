package com.tongban.im.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.SearchDiscoverActivity;
import com.tongban.im.activity.UserCenterActivity;

/**
 * 发现页
 * author: chenenyu 15/7/13
 */
public class DiscoverFragment extends BaseApiFragment implements View.OnClickListener {

    private ImageView ivUserIcon;
    private ImageView ivSearchAll;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView() {
        ivUserIcon = (ImageView) mView.findViewById(R.id.iv_user_portrait);
        ivSearchAll = (ImageView) mView.findViewById(R.id.iv_search_all);
    }

    @Override
    protected void initListener() {
        ivUserIcon.setOnClickListener(this);
        ivSearchAll.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        if (v == ivUserIcon) {
            mContext.startActivity(new Intent(mContext, UserCenterActivity.class));
        } else if (v == ivSearchAll) {
            mContext.startActivity(new Intent(mContext, SearchDiscoverActivity.class));
        }
    }
}
