package com.tongban.im.activity.group;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.fragment.group.RecommendGroupFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * 圈子-adapter
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class SearchGroupActivity extends BaseToolBarActivity implements
        SearchView.OnQueryTextListener {

    private SearchView searchView;
    private String mKeyword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_join_group;
    }

    @Override
    protected void initView() {
        RecommendGroupFragment recommendGroupFragment = new RecommendGroupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,
                recommendGroupFragment).commit();
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mKeyword = uri.getQueryParameter("keyword");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_join_group, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            BaseEvent.SearchGroupKeyEvent search = new BaseEvent.SearchGroupKeyEvent();
            search.keyword = query;
            EventBus.getDefault().post(search);
        }
        return true;
    }

    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        searchView.onActionViewCollapsed();
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
