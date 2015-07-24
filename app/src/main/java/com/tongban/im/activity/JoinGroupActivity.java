package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.JoinGroupAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.MessageApi;
import com.tongban.im.model.BaseEvent;
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
public class JoinGroupActivity extends BaseToolBarActivity implements View.OnClickListener {

    private ListView lvGroups;
    private View vHeader;
    private EditText etSearch;
    private TextView tvSearch;

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
//        SearchView searchView = new SearchView(mContext);
        if (vHeader != null) {
            lvGroups.addHeaderView(vHeader);
            etSearch = (EditText) vHeader.findViewById(R.id.et_search);
            tvSearch = (TextView) vHeader.findViewById(R.id.tv_search);
        }
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(this);

        if (tvSearch != null) {
            tvSearch.setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {
        List<Group> groups = new ArrayList<>();
//        for (int i = 1; i < 10; i++) {
//            Group group = new Group();
//            group.setGroup_id("55b1ed4ed7bdb0e4a1a23292");
//            group.setGroup_name("半岛国际" + i + "岁宝宝圈");
//            groups.add(group);
//        }
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
        if (v == tvSearch) {
            GroupApi.getInstance().searchGroupByName(etSearch.getText().toString().trim(), 0, 10, this);
        } else {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.btn_join:
                    Group group = (Group) v.getTag();
                    // TODO 加入群，不需要验证
//                    MessageApi.getInstance().joinGroup(new String[]{groupId}, "新人报道", this);
                    GroupApi.getInstance().joinGroup(group.getGroup_id(), group.getUser_id(), this);
                    break;
            }
        }
    }

    public void onEventMainThread(BaseEvent.SearchGroupEvent searchGroupEvent) {
        mAdapter.replaceAll(searchGroupEvent.getGroups());
    }

    public void onEventMainThread(BaseEvent.JoinGroupEvent joinGroupEvent) {
        ToastUtil.getInstance(mContext).showToast(joinGroupEvent.getMessage());
    }

}
