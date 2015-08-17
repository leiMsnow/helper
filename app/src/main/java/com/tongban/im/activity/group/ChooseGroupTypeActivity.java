package com.tongban.im.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.group.CreateGroupActivity;
import com.tongban.im.adapter.ChooseGroupTypeAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.model.GroupType;

import java.util.List;

/**
 * 选择圈子类型
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class ChooseGroupTypeActivity extends BaseToolBarActivity implements AbsListView.OnItemClickListener {

    private ListView lvGroupType;
    private ChooseGroupTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_choice_group_type;
    }

    @Override
    protected void initView() {
        lvGroupType = (ListView) findViewById(R.id.lv_group_type);
    }

    @Override
    protected void initListener() {
        lvGroupType.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        List<GroupType> groupTypes = GroupType.createGroupType();


        mAdapter = new ChooseGroupTypeAdapter(mContext, R.layout.item_group_type, groupTypes);
        lvGroupType.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == lvGroupType) {
            Intent intent = new Intent(mContext, CreateGroupActivity.class);
            Bundle bundle = new Bundle();
            int type = mAdapter.getItem(position).getGroupType();
            String typeName = mAdapter.getItem(position).getGroupTypeName();
            bundle.putInt(Consts.KEY_GROUP_TYPE, type);
            bundle.putString(Consts.KEY_GROUP_TYPE_NAME, typeName);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
