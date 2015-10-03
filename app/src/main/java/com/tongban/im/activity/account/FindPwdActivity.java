package com.tongban.im.activity.account;

import android.view.MenuItem;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.account.InputPhoneFragment;

/**
 * 找回密码界面
 *
 * @author fushudi
 */
public class FindPwdActivity extends BaseToolBarActivity {

    private InputPhoneFragment mInputPhoneFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_find_pwd;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        setTitle(R.string.find_pwd);
        mInputPhoneFragment = new InputPhoneFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, mInputPhoneFragment)
                .commit();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
