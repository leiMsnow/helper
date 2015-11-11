package com.tongban.im.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.widget.view.transformer.DepthPageTransformer;
import com.tongban.corelib.widget.view.transformer.ScalePageTransformer;
import com.tongban.corelib.widget.view.transformer.ZoomInPageTransformer;
import com.tongban.corelib.widget.view.transformer.ZoomOutPageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.fragment.ServiceHallFragment;
import com.voice.tongban.activity.IntelligentMainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnPageChange;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 主界面
 */
public class MainActivity extends AppBaseActivity {

    @Bind(R.id.dl_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.fab_voice)
    FloatingActionButton fabVoice;
    @Bind(R.id.fl_container)
    ViewPager mViewPager;

    ActionBarDrawerToggle mActionBarDrawerToggle;
    List<Fragment> mTabs = new ArrayList<>();

    MenuItem mHome, mMessage;

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
        mHome = menu.findItem(R.id.menu_main);
        mMessage = menu.findItem(R.id.menu_chat);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main) {
            setIndicator(0);
        } else if (item.getItemId() == R.id.menu_chat) {
            setIndicator(1);
        }
        return true;
    }

    @Override
    protected void initData() {
        //推荐界面
        Fragment recommendFragment = ServiceHallFragment.getInstance();
        //聊天界面
        ConversationListFragment chatFragment = ConversationListFragment.getInstance();

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

        mTabs.add(recommendFragment);
        mTabs.add(chatFragment);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mTabs.size());
        mViewPager.setPageTransformer(true, new ZoomInPageTransformer());

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


    @OnPageChange(value = R.id.fl_container,callback = OnPageChange.Callback.PAGE_SELECTED)
    public void onPageSelected(int position) {
        setIndicator(position);
    }


    // 设置指示器位置
    private void setIndicator(int position) {

        mViewPager.setCurrentItem(position, true);

        if (position == 0) {
            mHome.setIcon(R.mipmap.ic_menu_home_pressed);
            mMessage.setIcon(R.mipmap.ic_menu_message_normal);
        } else {
            mHome.setIcon(R.mipmap.ic_menu_home_normal);
            mMessage.setIcon(R.mipmap.ic_menu_message_pressed);
        }

    }

    @Override
    public void onBackPressed() {
        // 按返回键时后台运行
        moveTaskToBack(true);
    }

}
