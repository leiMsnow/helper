package com.tongban.im.activity.discover;

import android.view.Menu;
import android.view.MenuItem;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.im.R;

/**
 * 商品详情页(图书)
 * @author Cheney
 * @date 8/20
 */
public class ProductBookActivity extends BaseApiActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_book;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
