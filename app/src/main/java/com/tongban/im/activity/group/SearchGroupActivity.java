package com.tongban.im.activity.group;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import com.tongban.corelib.utils.NetUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.SuggestionsBaseActivity;
import com.tongban.im.api.GroupApi;
import com.tongban.im.fragment.group.RecommendGroupFragment;

/**
 * 圈子-adapter
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class SearchGroupActivity extends SuggestionsBaseActivity implements
        SearchView.OnQueryTextListener {


   private RecommendGroupFragment recommendGroupFragment;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_join_group;
    }

    @Override
    protected void initData() {
        super.initData();
        recommendGroupFragment = new RecommendGroupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,
                recommendGroupFragment).commit();
    }

    @Override
    protected int getMenuInflate() {
        return R.menu.menu_join_group;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!NetUtils.isConnected(mContext)) {
            return false;
        }
        if (!TextUtils.isEmpty(query)) {
            GroupApi.getInstance().searchGroupList(query, 0, 15, this);
            recommendGroupFragment.setKeyword(query);
        }
        return true;
    }
}
