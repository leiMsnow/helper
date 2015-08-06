package com.tongban.im.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;

/**
 * poi搜索功能
 */
public class PoiSearchActivity extends BaseToolBarActivity implements
        OnGetPoiSearchResultListener, AdapterView.OnItemClickListener,
        LoadMoreListView.OnLoadMoreListener {

    private PoiSearch mPoiSearch = null;
    private LoadMoreListView lvLocation;

    private EditText keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;

    private int mLoadIndex = 0;
    private int mPageSize = 10;
    private String mCity;
    private String mKey;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_poisearch;
    }

    @Override
    protected void initView() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        keyWorldsView = (EditText) findViewById(R.id.search_key);
        lvLocation = (LoadMoreListView) findViewById(R.id.lv_location);

    }

    @Override
    protected void initData() {
        mCity = SPUtils.get(mContext, Consts.CITY, "北京").toString();
        if (mCity.contains("市")) {
            mCity = mCity.replace("市", "");
        }
        mKey = SPUtils.get(mContext, Consts.ADDRESS, "").toString();
        sugAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line);
        lvLocation.setAdapter(sugAdapter);
        lvLocation.setPageSize(mPageSize);
    }

    @Override
    protected void initListener() {
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        lvLocation.setOnItemClickListener(this);
        lvLocation.setOnLoadMoreListener(this);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable arg0) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                mKey = cs.toString();
                startPoiSearch();
            }
        });
    }

    protected void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }

    public void onGetPoiResult(PoiResult result) {
        lvLocation.onLoadComplete();
        lvLocation.setResultSize(result.getTotalPoiNum());
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(PoiSearchActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            for (PoiInfo info : result.getAllPoi()) {
                if (info.address != null)
                    sugAdapter.add(info.address);
            }
            sugAdapter.notifyDataSetChanged();
            mLoadIndex++;
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(PoiSearchActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PoiSearchActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(PoiSearchActivity.this, result.getName() + ": " +
                    result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onLoadMore() {
        startPoiSearch();
    }

    private void startPoiSearch() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(mCity)
                .keyword(mKey)
                .pageNum(mLoadIndex)
                .pageCapacity(mPageSize));
    }
}
