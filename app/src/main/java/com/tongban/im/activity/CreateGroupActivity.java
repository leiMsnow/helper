package com.tongban.im.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.CreateGroupIntroductionFragment;
import com.tongban.im.fragment.CreateGroupLabelFragment;
import com.tongban.im.fragment.CreateGroupLocationFragment;
import com.tongban.im.fragment.CreateGroupNameFragment;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends BaseToolBarActivity {
    private static final int FRAGMENT_CREATE_CIRCLE_NAME = 1;
    private static final int FRAGMENT_CREATE_CIRCLE_LABEL = 2;
    private static final int FRAGMENT_CREATE_CIRCLE_LOCATION = 3;
    private static final int FRAGMENT_CREATE_CIRCLE_INTRODUCTION = 4;
    private FrameLayout replacedFragment;
    private CreateGroupNameFragment mCreateGroupNameFragment;
    private CreateGroupLabelFragment mCreateGroupLabelFragment;
    private CreateGroupLocationFragment mCreateGroupLocationFragment;
    private CreateGroupIntroductionFragment mCreateGroupIntroductionFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private int currentFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {
        replacedFragment = (FrameLayout) findViewById(R.id.replaced_fragment);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        currentFragment = FRAGMENT_CREATE_CIRCLE_NAME;
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        mCreateGroupNameFragment = new CreateGroupNameFragment();
        transaction.replace(R.id.replaced_fragment, mCreateGroupNameFragment).commit();
        transaction.addToBackStack(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_chat_settings) {
            if (currentFragment == FRAGMENT_CREATE_CIRCLE_NAME) {
                setTitle(getResources().getString(R.string.create_circle_name));
                currentFragment = FRAGMENT_CREATE_CIRCLE_LABEL;
                mCreateGroupLabelFragment = new CreateGroupLabelFragment();
                transaction.replace(R.id.replaced_fragment, mCreateGroupLabelFragment).commit();
                transaction.addToBackStack(null);
            } else if (currentFragment == FRAGMENT_CREATE_CIRCLE_LABEL) {
                currentFragment = FRAGMENT_CREATE_CIRCLE_LOCATION;
                mCreateGroupLocationFragment = new CreateGroupLocationFragment();
                transaction.replace(R.id.replaced_fragment, mCreateGroupLocationFragment).commit();
                transaction.addToBackStack(null);
            } else if (currentFragment == FRAGMENT_CREATE_CIRCLE_LOCATION) {
                currentFragment = FRAGMENT_CREATE_CIRCLE_INTRODUCTION;
                mCreateGroupIntroductionFragment = new CreateGroupIntroductionFragment();
                transaction.replace(R.id.replaced_fragment, mCreateGroupIntroductionFragment).commit();
                transaction.addToBackStack(null);
            } else if (currentFragment == FRAGMENT_CREATE_CIRCLE_INTRODUCTION) {
                //创建圈子完成
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
