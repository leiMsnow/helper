package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.JoinGroupAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

import java.util.ArrayList;
import java.util.List;

/**
 * 加入圈子
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class JoinGroupActivity extends BaseToolBarActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private ListView lvGroups;
    private View vHeader;
    private EditText etSearch;

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
        vHeader = LayoutInflater.from(mContext).inflate(R.layout.include_search, null);
        if (vHeader != null) {
            lvGroups.addHeaderView(vHeader);
            etSearch = (EditText) vHeader.findViewById(R.id.et_search);
            etSearch.setEnabled(true);
        }
    }

    @Override
    protected void initListener() {
        if (etSearch != null) {
            etSearch.setOnFocusChangeListener(this);
            etSearch.setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {
        GroupApi.getInstance().searchGroupByName("hel", 0, 10, this);
        mAdapter = new JoinGroupAdapter(mContext, R.layout.item_join_group_list, null);
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
        if (v == etSearch) {
            ToastUtil.getInstance(mContext).showToast("搜索");
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == etSearch) {
            ToastUtil.getInstance(mContext).showToast("搜索");
        }
    }

    public void onEventMainThread(List<Group> groups) {
        if (groups != null && groups.size() > 0) {
            mAdapter.replaceAll(groups);
        }
    }
}
