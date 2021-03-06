package com.tongban.corelib.fragment;


import android.support.v4.view.ViewPager;

import com.tongban.corelib.R;
import com.tongban.corelib.adapter.PhotoViewPagerAdapter;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.base.fragment.BaseTemplateFragment;
import com.tongban.corelib.widget.view.HackyViewPager;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;

import java.util.ArrayList;


public class PhotoViewFragment extends BaseApiFragment {

    public static final String KEY_URL = "KEY_URL";
    public static final String KEY_CURRENT_INDEX = "KEY_CURRENT_INDEX";


    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;

    private PhotoViewPagerAdapter adapter;

    private ArrayList<String> resList;
    private int currentIndex;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_photo_view;
    }

    protected void initView() {
        mViewPager = (HackyViewPager) mView.findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) mView.findViewById(R.id.lpi_indicator);
    }


    @Override
    protected void initData() {
        initView();
        if (getArguments() != null) {
            resList = getArguments().getStringArrayList(KEY_URL);
            currentIndex = getArguments().getInt(KEY_CURRENT_INDEX, 0);
            adapter = new PhotoViewPagerAdapter(mContext, resList);
            mViewPager.setAdapter(adapter);

            mIndicator.setViewPager(mViewPager);
            mViewPager.setCurrentItem(currentIndex);
        }
    }
}
