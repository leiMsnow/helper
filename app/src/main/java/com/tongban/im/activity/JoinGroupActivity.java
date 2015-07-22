package com.tongban.im.activity;

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
import com.tongban.im.model.Group;

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
        vHeader = LayoutInflater.from(mContext).inflate(R.layout.header_search, null);
        if (vHeader != null) {
            lvGroups.addHeaderView(vHeader);
            etSearch = (EditText) vHeader.findViewById(R.id.et_search);
            etSearch.setEnabled(true);
        }
    }

    @Override
    protected void initListener() {
        if (etSearch != null)
            etSearch.setOnFocusChangeListener(this);
            etSearch.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        List<Group> groups = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Group group = new Group();
            group.setGroup_name("半岛国际" + i + "岁宝宝圈");
            groups.add(group);
        }
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
}
