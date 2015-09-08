package com.tongban.im.activity.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.ProductApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;

import java.util.List;

/**
 * 搜索首页
 *
 * @author zhangleilei
 * @createTime 2015/8/12
 */
public class SearchDiscoverActivity extends BaseToolBarActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private FlowLayout mFlowLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discover_search;
    }

    @Override
    protected void initView() {
        mFlowLayout = (FlowLayout) findViewById(R.id.fl_hotwords);
    }

    @Override
    protected void initData() {
        ProductApi.getInstance().fetchHotwords(20, this);
    }


    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_topic, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("搜索关键字");
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            Intent intent = new Intent(mContext, SearchResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Consts.KEY_SEARCH_VALUE, query);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * 获取热词成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchHotwordsEvent event) {
        List<String> hotwords = event.hotwords;
        if (hotwords != null && hotwords.size() > 0) {
            mFlowLayout.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 10, 10);
            for (final String tag : hotwords) {
                TextView tv = new TextView(mContext);
                tv.setSingleLine();
                tv.setMaxEms(10);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setText(tag);
                tv.setTextColor(getResources().getColor(R.color.main_black));
                tv.setLayoutParams(layoutParams);
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_corners_bg_grey));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SearchResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Consts.KEY_SEARCH_VALUE, tag);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                mFlowLayout.addView(tv);
            }
        }
    }
}
