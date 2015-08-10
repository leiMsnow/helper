package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
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
    private View vHeader;
    private ChooseGroupTypeAdapter mAdapter;
    private TextView tvGroupTypeName;
    private ImageView ivGroupIcon;

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
            tvGroupTypeName = (TextView) findViewById(R.id.tv_group_name);
            ivGroupIcon = (ImageView) findViewById(R.id.iv_group_icon);
        }
    }

    @Override
    protected void initListener() {
        lvGroupType.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        List<GroupType> groupTypes = GroupType.createGroupType();

        tvGroupTypeName.setText("创建一个附近的圈子");

        mAdapter = new ChooseGroupTypeAdapter(mContext, R.layout.item_group_type, groupTypes);
        lvGroupType.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == lvGroupType) {
            Intent intent = new Intent(mContext, CreateGroupActivity.class);
            Bundle bundle = new Bundle();
            int type = position;
            String typeName = getString(R.string.create_group);
            if (position > 0) {
                type = mAdapter.getItem(position - 1).getGroupType();
                typeName = mAdapter.getItem(position - 1).getGroupTypeName();
            }
            bundle.putInt(Consts.KEY_GROUP_TYPE, type);
            bundle.putString(Consts.KEY_GROUP_TYPE_NAME, typeName);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
