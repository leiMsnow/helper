package com.tongban.im.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.fragment.ServiceHallFragment;
import com.voice.tongban.activity.IntelligentMainActivity;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 主界面
 */
public class MainActivity extends AppBaseActivity {

    @Bind(R.id.dl_layout)
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;

    ConversationListFragment chatFragment;
    ServiceHallFragment recommendFragment;
    @Bind(R.id.fab_voice)
    FloatingActionButton fabVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityContainer.getInstance().finishActivity(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main) {
            getSupportFragmentManager().beginTransaction()
                    .show(recommendFragment)
                    .hide(chatFragment)
                    .commit();
        } else if (item.getItemId() == R.id.menu_chat) {
            getSupportFragmentManager().beginTransaction()
                    .show(chatFragment)
                    .hide(recommendFragment)
                    .commit();
        }
        return true;
    }

    @Override
    protected void initData() {
        //推荐界面
        recommendFragment = ServiceHallFragment.getInstance();
        //聊天界面
        chatFragment = ConversationListFragment.getInstance();
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                        //私聊
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                        //群组
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                        //系统
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        chatFragment.setUri(uri);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, recommendFragment)
                .add(R.id.fl_container, chatFragment)
                .hide(chatFragment)
                .commit();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }


    @OnClick(R.id.fab_voice)
    public void onClick(View v) {
        if (v == fabVoice) {
            startActivity(new Intent(mContext, IntelligentMainActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        // 按返回键时后台运行
        moveTaskToBack(true);
    }

}
