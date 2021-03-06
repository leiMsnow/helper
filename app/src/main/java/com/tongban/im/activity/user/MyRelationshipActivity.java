package com.tongban.im.activity.user;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tb.api.utils.ApiConstants;
import com.tongban.corelib.utils.Constants;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.FansFragment;
import com.tongban.im.fragment.user.FocusFragment;

/**
 * 粉丝、关注界面
 *
 * @author fushudi
 */
public class MyRelationshipActivity extends AppBaseActivity {

    private Fragment mFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initData() {
        if (getIntent().getData() != null) {

            Uri uri = getIntent().getData();
            String tag = uri.getQueryParameter(ApiConstants.KEY_TAG);
            String userId = uri.getQueryParameter(Constants.USER_ID);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.USER_ID, userId);

            if (tag.equals(Consts.TAG_FANS)) {
                setTitle("粉丝");
                mFragment = new FansFragment();
            } else if (tag.equals(Consts.TAG_FOLLOW)) {
                setTitle("关注");
                mFragment = new FocusFragment();

            }

            mFragment.setArguments(bundle);
            transaction.replace(R.id.fl_container, mFragment);
            transaction.commit();
        }
    }
}
