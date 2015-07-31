package com.tongban.im.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.CircleFragment;
import com.tongban.im.fragment.DiscoverFragment;
import com.tongban.im.fragment.LeftMenuFragment;
import com.tongban.im.fragment.PostBarFragment;
import com.tongban.im.model.BaseEvent;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * 主界面
 */
public class MainActivity extends BaseToolBarActivity implements View.OnClickListener {

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;

    /**
     * 底部Tab
     */
    private RadioGroup rg_tab;
    /**
     * 当前页
     */
    private int currentPage = 0;
    /**
     * 存放Fragment的数组
     */
    private Fragment[] fragments;
    /**
     * 侧滑页
     */
    private LeftMenuFragment leftMenuFragment;

    private FragmentManager fm;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setTitle("发现");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_panel);

        fm = getSupportFragmentManager();

        leftMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_menu_container);
        if (leftMenuFragment == null) {
            leftMenuFragment = new LeftMenuFragment();
            fm.beginTransaction().add(R.id.id_menu_container, leftMenuFragment).commit();
        }

        fragments = new Fragment[3];
        /** 发现页 */
        fragments[0] = new DiscoverFragment();
        /** 话题/动态页 */
        fragments[1] = new PostBarFragment();
        /** 圈子页 */
        fragments[2] = new CircleFragment();
//        if (fragments[2] == null) {
//            ConversationListFragment listFragment = ConversationListFragment.getInstance();
//            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                    .appendPath("conversationlist")
//                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
//                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false") //群组
//                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false") //系统
//                    .build();
//            listFragment.setUri(uri);
//            fragments[1] = listFragment;
//        }


        fm.beginTransaction().add(R.id.fl_container, fragments[0]).commit();

        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_discover:
                        fm.beginTransaction().show(fragments[0]).
                                hide(fragments[currentPage]).commit();
                        currentPage = 0;
                        setTitle("发现");
                        break;
                    case R.id.rb_post_bar:
                        if (fragments[1].isAdded()) {
                            fm.beginTransaction().show(fragments[1]).
                                    hide(fragments[currentPage]).commit();
                        } else {
                            fm.beginTransaction().add(R.id.fl_container, fragments[1])
                                    .hide(fragments[currentPage]).commit();
                        }
                        currentPage = 1;
                        setTitle("话题");
                        break;
                    case R.id.rb_circle:
                        if (fragments[2].isAdded()) {
                            fm.beginTransaction().show(fragments[2]).
                                    hide(fragments[currentPage]).commit();
                        } else {
                            fm.beginTransaction().add(R.id.fl_container, fragments[2])
                                    .hide(fragments[currentPage]).commit();
                        }
                        currentPage = 2;
                        setTitle("圈子");
                        break;
                }
            }
        });
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_circle_fragment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_join_group) {
            Intent intent = new Intent(mContext, JoinGroupActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEventMainThread(Object o) {
        if (o instanceof BaseEvent.DrawerLayoutMenu) {
            BaseEvent.DrawerLayoutMenu item = (BaseEvent.DrawerLayoutMenu) o;
            LogUtil.d("onEventMainThread:" + item.getText());
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        // 按返回键时后台运行
        moveTaskToBack(true);
    }

}
