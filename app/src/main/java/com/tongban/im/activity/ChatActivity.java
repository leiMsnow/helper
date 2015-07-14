package com.tongban.im.activity;

import android.os.Bundle;
import android.view.Menu;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.im.R;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseApiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

}
