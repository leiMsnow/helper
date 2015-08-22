package com.tongban.im.activity.user;


import android.widget.ListView;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.GroupListAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心（我的圈子）
 *
 * @author fushudi
 */
public class MyGroupActivity extends BaseToolBarActivity {
    private ListView lvMyGroupList;
    private GroupListAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_group;
    }

    @Override
    protected void initView() {
        setTitle(R.string.my_group);
        lvMyGroupList = (ListView) findViewById(R.id.lv_my_group_list);
    }

    @Override
    protected void initData() {
//        List<Group> myGroupList = new ArrayList<>();
//        for (int i = 1; i < 12; i++) {
//            Group group = new Group();
//            if (i%2==0){
//                group.setGroup_type(GroupType.AGE);
//                group.setGroup_avatar("http://g.hiphotos.baidu.com/image/w%3D310/sign=849647306963f6241c5d3f02b745eb32/5882b2b7d0a20cf4472432d674094b36acaf9907.jpg");
//                group.setGroup_name("2014年7月宝宝圈");
//                group.setDeclaration("半岛国际附近");
//            }else {
//                group.setGroup_type(GroupType.CLASSMATE);
//                group.setGroup_avatar("http://g.hiphotos.baidu.com/image/w%3D310/sign=849647306963f6241c5d3f02b745eb32/5882b2b7d0a20cf4472432d674094b36acaf9907.jpg");
//                group.setGroup_name("2014年9月同学圈");
//                group.setDeclaration("半岛国际附近");
//            }
//            myGroupList.add(group);
//        }
        mAdapter = new GroupListAdapter(mContext, R.layout.item_group_list, null);
        lvMyGroupList.setAdapter(mAdapter);

        UserCenterApi.getInstance().fetchMyGroupList(0, 10, this);
    }

    @Override
    protected void initListener() {

    }
    //返回圈子列表
    public void onEventMainThread(List<Group> groups) {
        mAdapter.replaceAll(groups);
    }
}
