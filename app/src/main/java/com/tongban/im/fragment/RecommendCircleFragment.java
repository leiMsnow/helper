package com.tongban.im.fragment;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;

/**
 * 推荐圈子的Fragment
 * Created by Cheney on 15/8/3.
 */
public class RecommendCircleFragment extends BaseApiFragment implements OnQueryTextListener {
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_recommend_circle;
    }

    @Override
    protected void initView() {
        mSearchView = (SearchView) mView.findViewById(R.id.search_view);
        mListView = (ListView) mView.findViewById(R.id.lv_circle);
    }

    @Override
    protected void initListener() {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            // cey 搜索
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
