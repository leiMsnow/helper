package com.tongban.im.activity;

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
        setTitle(getResources().getString(R.string.create_group_name));
        currentFragment = FRAGMENT_CREATE_CIRCLE_NAME;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mCreateGroupNameFragment = new CreateGroupNameFragment();
        transaction.replace(R.id.replaced_fragment, mCreateGroupNameFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        //Fragment后退
        if (itemId == android.R.id.home) {
            currentFragment -= 1;
            if (currentFragment <= 1) {
                currentFragment = 1;
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else if (itemId == R.id.next_step) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //选择圈子标签Fragment
            if (currentFragment == FRAGMENT_CREATE_CIRCLE_NAME) {
                setTitle(getResources().getString(R.string.create_group_label));
                currentFragment = FRAGMENT_CREATE_CIRCLE_LABEL;
                mCreateGroupLabelFragment = new CreateGroupLabelFragment();
                transaction.replace(R.id.replaced_fragment, mCreateGroupLabelFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            //选择圈子位置Fragment
            else if (currentFragment == FRAGMENT_CREATE_CIRCLE_LABEL) {
                setTitle(getResources().getString(R.string.create_group_location));
                currentFragment = FRAGMENT_CREATE_CIRCLE_LOCATION;
                mCreateGroupLocationFragment = new CreateGroupLocationFragment();
                transaction.replace(R.id.replaced_fragment, mCreateGroupLocationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            //填写圈子介绍Fragment
            else if (currentFragment == FRAGMENT_CREATE_CIRCLE_LOCATION) {
                setTitle(getResources().getString(R.string.create_group_introduction));
                currentFragment = FRAGMENT_CREATE_CIRCLE_INTRODUCTION;
                mCreateGroupIntroductionFragment = new CreateGroupIntroductionFragment();
                transaction.replace(R.id.replaced_fragment, mCreateGroupIntroductionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
            //创建圈子完成
            else if (currentFragment == FRAGMENT_CREATE_CIRCLE_INTRODUCTION) {


            }

        }
        if (currentFragment == 1) {
            setTitle(getResources().getString(R.string.create_group_name));
        } else if (currentFragment == 2) {
            setTitle(getResources().getString(R.string.create_group_label));
        } else if (currentFragment == 3) {
            setTitle(getResources().getString(R.string.create_group_location));
        } else if (currentFragment == 4) {
            setTitle(getResources().getString(R.string.create_group_introduction));
        }
        return true;
    }
}
