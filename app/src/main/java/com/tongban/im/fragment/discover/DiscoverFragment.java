package com.tongban.im.fragment.discover;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.discover.SearchDiscoverActivity;
import com.tongban.im.activity.user.PersonalCenterActivity;

/**
 * 发现页
 * author: chenenyu 15/7/13
 */
public class DiscoverFragment extends BaseApiFragment implements View.OnClickListener {

    private ImageView ivUserIcon;
    private ImageView ivSearchAll;
    private ListView mListView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initView() {
        ivUserIcon = (ImageView) mView.findViewById(R.id.iv_user_portrait);
        ivSearchAll = (ImageView) mView.findViewById(R.id.iv_search_all);
        mListView = (ListView) mView.findViewById(R.id.lv_discover);
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
            mContext.startActivity(new Intent(mContext, PersonalCenterActivity.class));
        } else if (v == ivSearchAll) {
            mContext.startActivity(new Intent(mContext, SearchDiscoverActivity.class));
        }
    }
}
