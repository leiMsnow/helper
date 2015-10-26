package com.tongban.im.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.PoiSearchAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.model.group.GroupType;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * poi搜索功能
 */
public class SearchPoiActivity extends AppBaseActivity implements
        OnGetPoiSearchResultListener
        , OnLoadMoreListener
        , SearchView.OnQueryTextListener {

    @Bind(R.id.lv_location)
    LoadMoreListView lvLocation;

    private SearchView searchView;

    private PoiSearch mPoiSearch;
    private PoiSearchAdapter mAdapter;

    private int mLoadIndex = 0;
    private int mPageSize = 10;
    private String mSelected;
    private String mCity;
    private String mKey;

    private int mGroupType = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        if (getIntent().getExtras() != null) {
            mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE, 0);
            mSelected = getIntent().getExtras().getString(Consts.KEY_SELECTED_POI_NAME, "");
        }
        return R.layout.activity_poisearch;
    }

    @Override
    protected void initData() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();

        mCity = SPUtils.get(mContext, Consts.CITY, "北京").toString();
        if (mCity.contains("市")) {
            mCity = mCity.replace("市", "");
        }
        mKey = SPUtils.get(mContext, Consts.ADDRESS, "").toString();
        mAdapter = new PoiSearchAdapter(mContext,
                R.layout.item_poi_search_list, null);
        mAdapter.setCurrentSelected(mSelected);
        lvLocation.setAdapter(mAdapter);
        lvLocation.setPageSize(mPageSize);
        startPoiSearch();

        mPoiSearch.setOnGetPoiSearchResultListener(this);
        lvLocation.setOnLoadMoreListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_poi, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(mGroupType == GroupType.CLASSMATE ? "输入宝宝学校" : "输入附近的位置");
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewCollapsed();
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
    }

    public void onGetPoiResult(PoiResult result) {
        lvLocation.onLoadComplete();
        lvLocation.setResultSize(result.getCurrentPageCapacity());
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//            ToastUtil.getInstance(mContext).showToast("未找到您当前的位置");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (mLoadIndex == 0)
                mAdapter.clear();

            mAdapter.addAll(result.getAllPoi());
            mLoadIndex++;
            return;
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
//            ToastUtil.getInstance(mContext).showToast("未找到您当前的位置");
        } else {
            ToastUtil.getInstance(mContext).showToast(result.getName() + ": " +
                    result.getAddress());
        }
    }

    @OnItemClick(R.id.lv_location)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.getItem(position) == null) {
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Consts.KEY_SELECTED_POI_NAME, mAdapter.getItem(position).name);
        bundle.putDouble(Consts.LONGITUDE, mAdapter.getItem(position).location.longitude);
        bundle.putDouble(Consts.LATITUDE, mAdapter.getItem(position).location.latitude);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void startPoiSearch() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(mCity)
                .keyword(mKey)
                .pageNum(mLoadIndex)
                .pageCapacity(mPageSize));
    }


    @Override
    public void onLoadMore() {
        startPoiSearch();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            mLoadIndex = 0;
            mKey = query;
            startPoiSearch();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
