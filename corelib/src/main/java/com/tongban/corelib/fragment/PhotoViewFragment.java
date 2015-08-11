package com.tongban.corelib.fragment;


import android.support.v4.view.ViewPager;

import com.tongban.corelib.R;
import com.tongban.corelib.adapter.PhotoViewPagerAdapter;
import com.tongban.corelib.base.fragment.BaseUIFragment;
import com.tongban.corelib.widget.view.HackyViewPager;

import java.util.ArrayList;


public class PhotoViewFragment extends BaseUIFragment {

    public static final String KEY_URL = "KEY_URL";


    private ViewPager mViewPager;

    private PhotoViewPagerAdapter adapter;

    private ArrayList<String> resList;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_photo_view;
    }

    @Override
    protected void initView() {
        mViewPager = (HackyViewPager) mView.findViewById(R.id.view_pager);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            resList = getArguments().getStringArrayList(KEY_URL);
            adapter = new PhotoViewPagerAdapter(mContext, resList);
            mViewPager.setAdapter(adapter);
        }
    }

}
