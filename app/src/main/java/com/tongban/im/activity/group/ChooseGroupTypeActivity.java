package com.tongban.im.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tb.api.model.BaseEvent;
import com.tb.api.model.group.GroupType;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.ChooseGroupTypeAdapter;
import com.tongban.im.common.Consts;

import java.util.ArrayList;
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
        List<GroupType> groupTypes = createGroupType();

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
     * 创建圈子类型数据
     *
     * @return
     */
    public static List<GroupType> createGroupType() {
        List<GroupType> groupTypes = new ArrayList<>();
        GroupType group = new GroupType();
        group.setGroupType(GroupType.CITY);
        group.setGroupTypeName("同城圈");
        group.setSrc(R.mipmap.ic_group_city);
        group.setGroupDesc("同一个城市的宝爸爸宝妈妈们");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_deep_purple);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(GroupType.AGE);
        group.setGroupTypeName("同龄圈");
        group.setSrc(R.mipmap.ic_group_age);
        group.setGroupDesc("同一个年龄的宝贝圈，喜好、成长会不会相同呢");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_pink);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(GroupType.CLASSMATE);
        group.setGroupTypeName("同学圈");
        group.setSrc(R.mipmap.ic_group_classmate);
        group.setGroupDesc("同一个班级的宝贝家长们，他们有什么经验分享呢");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_light_blue);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(GroupType.LIFE);
        group.setGroupTypeName("生活圈");
        group.setSrc(R.mipmap.ic_group_life);
        group.setGroupDesc("购物、宝宝日用品");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_light_green);

        groupTypes.add(group);

        group = new GroupType();
        group.setGroupType(GroupType.ACTIVITY);
        group.setGroupTypeName("活动圈");
        group.setSrc(R.mipmap.ic_group_activity);
        group.setGroupDesc("宝宝面对面，从此不孤单");
        group.setGroupBGColor(R.drawable.shape_group_icon_pressed_yellow);

        groupTypes.add(group);

        return groupTypes;

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
