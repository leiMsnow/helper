package com.tongban.im.fragment;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;

/**
 * 选择圈子位置界面
 *
 * @author fushudi
 */@Deprecated
public class CreateGroupLocationFragment extends BaseApiFragment implements View.OnClickListener {
    private ImageView ivSearch;
    private TextView tvCancel;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_create_group_location;
    }

    @Override
    protected void initView() {
        ivSearch = (ImageView) getActivity().findViewById(R.id.iv_search);
        tvCancel = (TextView) getActivity().findViewById(R.id.tv_cancle);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:

                break;
            case R.id.tv_cancle:

                break;
        }
    }
}
