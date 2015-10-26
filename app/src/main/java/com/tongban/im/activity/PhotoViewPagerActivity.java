package com.tongban.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;

import java.util.ArrayList;

/**
 * 图片浏览
 *
 * @author zhangleilei
 * @createTime 2015/8/11
 */
public class PhotoViewPagerActivity extends AppBaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            PhotoViewFragment photoViewFragment = new PhotoViewFragment();
            Bundle bundle = getIntent().getExtras();
            photoViewFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_content, photoViewFragment)
                    .commit();
        }
    }

    /**
     * 打开图片查看器
     *
     * @param mContext
     * @param urls
     * @param currentIndex
     */
    public static void startPhotoView(Context mContext, ArrayList<String> urls, int currentIndex) {
        Intent intent = new Intent(mContext, PhotoViewPagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PhotoViewFragment.KEY_URL, urls);
        bundle.putInt(PhotoViewFragment.KEY_CURRENT_INDEX, currentIndex);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


}