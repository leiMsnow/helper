package com.tongban.im.activity;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

public class PersonalDataActivity extends BaseToolBarActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView() {
        setTitle("个人资料");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.fans) {
            startActivity(new Intent(this,MyInfoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
