package com.tongban.corelib.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tongban.corelib.R;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PhotoViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mResList;

    public PhotoViewPagerAdapter(Context context, List<String> resList) {
        mContext = context;
        mResList = resList;
    }


    @Override
    public int getCount() {
        return mResList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(container.getContext());
        Glide.with(mContext).load(mResList.get(position)).into(photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}