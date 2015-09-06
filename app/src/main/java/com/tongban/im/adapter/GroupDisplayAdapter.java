package com.tongban.im.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tongban.im.common.Consts;
import com.tongban.im.fragment.group.GroupDisplayFragment;
import com.tongban.im.model.Group;

import java.util.List;

public class GroupDisplayAdapter extends FragmentPagerAdapter {


    private List<Group> groupList;

    public GroupDisplayAdapter(FragmentManager fm, List<Group> groupList) {
        super(fm);
        this.groupList = groupList;
    }

    @Override
    public Fragment getItem(int arg0) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Consts.KEY_GROUP_INFO, groupList.get(arg0));
        Fragment frag = new GroupDisplayFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
