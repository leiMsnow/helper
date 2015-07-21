package com.tongban.im.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.CreateCircleIntruductionFragment;
import com.tongban.im.fragment.CreateCircleLabelFragment;
import com.tongban.im.fragment.CreateCircleLocationFragment;
import com.tongban.im.fragment.CreateCircleNameFragment;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateCircleActivity extends BaseToolBarActivity {
    private FrameLayout replacedFragment;
    private CreateCircleNameFragment mCreateCircleNameFragment;
    private CreateCircleLabelFragment mCreateCircleLabelFragment;
    private CreateCircleLocationFragment mCreateCircleLocationFragment;
    private CreateCircleIntruductionFragment mCreateCircleIntruductionFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_circle;
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
        mCreateCircleNameFragment = new CreateCircleNameFragment();
        mCreateCircleLabelFragment = new CreateCircleLabelFragment();
        mCreateCircleLocationFragment = new CreateCircleLocationFragment();
        mCreateCircleIntruductionFragment = new CreateCircleIntruductionFragment();

        switchFragment(mCreateCircleNameFragment, mCreateCircleLabelFragment,
                mCreateCircleLocationFragment, mCreateCircleIntruductionFragment);

    }

    private void switchFragment(Fragment mCreateCircleNameFragment,
                                Fragment mCreateCircleLabelFragment, Fragment mCreateCircleLocationFragment,
                                Fragment mCreateCircleIntruductionFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!mCreateCircleLabelFragment.isAdded()) {
            transaction.hide(mCreateCircleNameFragment).add(R.id.replaced_fragment, mCreateCircleLabelFragment).commit();
        } else {
            transaction.hide(mCreateCircleNameFragment).show(mCreateCircleLabelFragment).commit();
        }

    }

//    private void switchFragment() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
////        if (mCreateCircleNameFragment != null) {
////            transaction.hide(mCreateCircleNameFragment);
////        }
////        if (mCreateCircleLabelFragment != null) {
////            transaction.hide(mCreateCircleLabelFragment);
////        }
////        if (mCreateCircleLocationFragment != null) {
////            transaction.hide(mCreateCircleLocationFragment);
////        }
////        if (mCreateCircleIntruductionFragment != null) {
////            transaction.hide(mCreateCircleIntruductionFragment);
////        }
//        //    mCreateCircleNameFragment = new CreateCircleNameFragment();
//        transaction.add(R.id.replaced_fragment, mCreateCircleNameFragment);
//        if (mCreateCircleLabelFragment == null) {
//            mCreateCircleLabelFragment = new CreateCircleLabelFragment();
//            transaction.add(R.id.replaced_fragment, mCreateCircleLabelFragment);
//        }
//        transaction.show(mCreateCircleLabelFragment);
//        transaction.commit();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_chat_settings) {

        }
        return super.onOptionsItemSelected(item);
    }
}
