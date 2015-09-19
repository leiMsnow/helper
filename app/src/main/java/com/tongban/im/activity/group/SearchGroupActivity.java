package com.tongban.im.activity.group;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;

import com.tongban.corelib.utils.NetUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.SuggestionsBaseActivity;
import com.tongban.im.fragment.group.RecommendGroupFragment;
import com.tongban.im.model.BaseEvent;

import de.greenrobot.event.EventBus;

/**
 * 圈子-adapter
 *
 * @author zhangleilei
 * @createTime 2015/07/22
 */
public class SearchGroupActivity extends SuggestionsBaseActivity implements
        SearchView.OnQueryTextListener {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_join_group;
    }

    @Override
    protected void initView() {
        super.initView();
        RecommendGroupFragment recommendGroupFragment = new RecommendGroupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,
                recommendGroupFragment).commit();
    }

    @Override
    protected void initData() {
//        if (getIntent() != null) {
//            Uri uri = getIntent().getData();
//        }
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
            BaseEvent.SearchGroupKeyEvent search = new BaseEvent.SearchGroupKeyEvent();
            search.keyword = query;
            EventBus.getDefault().post(search);
        }
        return true;
    }
}
