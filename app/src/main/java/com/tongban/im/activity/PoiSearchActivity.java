package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.adapter.PoiSearchAdapter;
import com.tongban.im.common.Consts;

/**
 * poi搜索功能
 */
public class PoiSearchActivity extends BaseToolBarActivity implements
        OnGetPoiSearchResultListener, AdapterView.OnItemClickListener,
        LoadMoreListView.OnLoadMoreListener, SearchView.OnQueryTextListener {

    private LoadMoreListView lvLocation;
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
            setToolbarTheme(mGroupType);
        }
        return R.layout.activity_poisearch;
    }

    @Override
    protected void initView() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        lvLocation = (LoadMoreListView) findViewById(R.id.lv_location);

    }

    @Override
    protected void initData() {
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
    }

    @Override
    protected void initListener() {
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        lvLocation.setOnItemClickListener(this);
        lvLocation.setOnLoadMoreListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_poi_search, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewCollapsed();
        return true;
    }

    protected void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }

    public void onGetPoiResult(PoiResult result) {
        lvLocation.onLoadComplete();
        lvLocation.setResultSize(result.getCurrentPageCapacity());
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            ToastUtil.getInstance(mContext).showToast("未找到您当前的位置");
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
            ToastUtil.getInstance(mContext).showToast("未找到您当前的位置");
        } else {
            ToastUtil.getInstance(mContext).showToast(result.getName() + ": " +
                    result.getAddress());
        }
    }

    @Override
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
