package com.tongban.im.activity.group;

import android.net.Uri;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.ArrayList;
import java.util.List;

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
    private ListView lvGroups;
    private GroupListAdapter mAdapter;
    private String mKeyword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_join_group;
    }

    @Override
    protected void initView() {
        lvGroups = (ListView) findViewById(R.id.lv_groups);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(new GroupListenerImpl(mContext));
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mKeyword = uri.getQueryParameter("keyword");
        }
        List<Group> groups = new ArrayList<>();
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, groups);
        mAdapter.setDisplayModel(false);
        lvGroups.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_join_group, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery(mKeyword, false);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            mKeyword = query;
            GroupApi.getInstance().searchGroupList(mKeyword, 0, 15, this);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * 搜索群组成功的事件
     *
     * @param searchGroupEvent
     */
    public void onEventMainThread(BaseEvent.SearchGroupListEvent searchGroupEvent) {
        mAdapter.replaceAll(searchGroupEvent.getGroups());
        lvGroups.setVisibility(View.VISIBLE);
    }

    /**
     * 加入群组成功的事件回调
     *
     * @param joinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        RongIM.getInstance().startGroupChat(mContext, joinGroupEvent.getGroup_id(),
                joinGroupEvent.getGroup_name());
        GroupApi.getInstance().searchGroupList(mKeyword, 0, mAdapter.getCount(), this);
    }
}
