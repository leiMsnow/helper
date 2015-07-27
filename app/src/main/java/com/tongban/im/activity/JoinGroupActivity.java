package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.JoinGroupAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * 加入圈子
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class JoinGroupActivity extends BaseToolBarActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private ListView lvGroups;
    private SearchView mSearchView;
    private JoinGroupAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_join_group;
    }

    @Override
    protected void initView() {
        lvGroups = (ListView) findViewById(R.id.lv_groups);
        mSearchView = (SearchView) findViewById(R.id.id_search_view);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(this);
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    protected void initData() {
        List<Group> groups = new ArrayList<>();
        mAdapter = new JoinGroupAdapter(mContext, R.layout.item_join_group_list, groups);
        lvGroups.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_join_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_group) {
            Intent intent = new Intent(mContext, ChoiceGroupTypeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_join:
                Group group = (Group) v.getTag();
                // TODO 加入群，不需要验证
                GroupApi.getInstance().joinGroup(group.getGroup_id(), group.getUser_id(), this);
                break;
        }

    }

    public void onEventMainThread(BaseEvent.SearchGroupEvent searchGroupEvent) {
        mAdapter.replaceAll(searchGroupEvent.getGroups());
    }

    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        ToastUtil.getInstance(mContext).showToast(joinGroupEvent.getMessage());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            GroupApi.getInstance().searchGroupByName(query, 0, 10, this);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
