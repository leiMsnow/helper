package com.tongban.im.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.tongban.corelib.fragment.PhotoViewFragment;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

import java.util.ArrayList;

/**
 * 图片浏览
 *
 * @author zhangleilei
 * @createTime 2015/8/11
 */
public class PhotoViewPagerActivity extends BaseToolBarActivity {

    private ArrayList<String> resList;

    private PhotoViewFragment photoViewFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            resList = getIntent().getStringArrayListExtra(PhotoViewFragment.KEY_URL);

            photoViewFragment = new PhotoViewFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PhotoViewFragment.KEY_URL, resList);
            photoViewFragment.setArguments(bundle);
            ft.replace(R.id.fl_content, photoViewFragment);
            ft.commit();
        }
    }

    @Override
    protected void initListener() {

    }


}