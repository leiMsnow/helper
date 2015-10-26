package com.tongban.im.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.ChooseGroupTypeAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.group.GroupType;

import java.util.List;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * 选择圈子类型
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class ChooseGroupTypeActivity extends AppBaseActivity {

    @Bind(R.id.lv_group_type)
    ListView lvGroupType;

    private ChooseGroupTypeAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_choose_group_type;
    }

    @Override
    protected void initData() {
        List<GroupType> groupTypes = GroupType.createGroupType();

        mAdapter = new ChooseGroupTypeAdapter(mContext, R.layout.item_group_type, groupTypes);
        lvGroupType.setAdapter(mAdapter);
    }

    @OnItemClick(R.id.lv_group_type)
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

    /**
     * 创建成功，关闭此界面
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.CreateGroupEvent obj) {
        finish();
    }
}
