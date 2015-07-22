package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.ChoiceGroupTypeAdapter;
import com.tongban.im.model.GroupType;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择圈子类型
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class ChoiceGroupTypeActivity extends BaseToolBarActivity implements AbsListView.OnItemClickListener {

    private ListView lvGroupType;
    private View vHeader;
    private ChoiceGroupTypeAdapter mAdapter;

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
        vHeader = LayoutInflater.from(mContext).inflate(R.layout.header_group_type, null);
        if (vHeader != null) {
            lvGroupType.addHeaderView(vHeader);
        }
    }

    @Override
    protected void initListener() {
        lvGroupType.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        List<GroupType> groupTypes = GroupType.createGroupType();
        mAdapter = new ChoiceGroupTypeAdapter(mContext, R.layout.item_group_type, groupTypes);

        lvGroupType.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == lvGroupType) {
            Intent intent = new Intent(mContext,CreateGroupActivity.class);
            startActivity(intent);
        }
    }
}
