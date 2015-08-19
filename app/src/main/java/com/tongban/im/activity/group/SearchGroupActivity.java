package com.tongban.im.activity.group;

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
public class SearchGroupActivity extends BaseToolBarActivity implements View.OnClickListener,
        SearchView.OnQueryTextListener {
    private SearchView searchView;
    private ListView lvGroups;
    private JoinGroupAdapter mAdapter;

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
        mAdapter.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        List<Group> groups = new ArrayList<>();
        mAdapter = new JoinGroupAdapter(mContext, R.layout.item_group_list, groups);
        lvGroups.setAdapter(mAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btn_join:
                Group group = (Group) v.getTag();
                // TODO 加入群，不需要验证
                GroupApi.getInstance().joinGroup(group.getGroup_id(), group.getGroup_name(),
                        group.getGroup_owner_id(), this);
                break;
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            GroupApi.getInstance().searchGroupList(query, 0, 15, this);
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
    public void onEventMainThread(BaseEvent.SearchGroupEvent searchGroupEvent) {
        mAdapter.replaceAll(searchGroupEvent.getGroups());
        lvGroups.setVisibility(View.VISIBLE);
    }

    /**
     * 加入群组成功的事件
     *
     * @param joinGroupEvent
     */
    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        ToastUtil.getInstance(mContext).showToast(joinGroupEvent.getMessage());
    }
}
