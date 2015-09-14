package com.tongban.im.activity.base;

import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.api.CommonApi;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.widget.view.SuggestionPopupWindow;

import java.util.List;

/**
 * 通用的照片处理父类
 * 裁剪
 * 拍照
 * 相册
 * Created by fushudi on 2015/8/13.
 */
public abstract class SuggestionsBaseActivity extends BaseToolBarActivity implements
        SearchView.OnQueryTextListener, SuggestionPopupWindow.OnKeywordSelectListener {


    private SuggestionPopupWindow suggestionPopupWindow;
    //点击最近搜索，将不显示搜索建议
    protected boolean isShowSuggestions = true;
    protected SearchView searchView;

    private String mQueryText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuInflate(), menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("输入关键字");
        searchView.onActionViewExpanded();
        return true;
    }

    protected abstract int getMenuInflate();

    private void initListPopupWindow(List<String> data) {
        int mScreenHeight = ScreenUtils.getScreenHeight(mContext);
        int toolBarHeight = mToolbar.getHeight();
        suggestionPopupWindow = new SuggestionPopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, (mScreenHeight - toolBarHeight),
                data, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.view_suggestions_popup_window, null));
        suggestionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        suggestionPopupWindow.adapterUpdate(mQueryText, data);
        suggestionPopupWindow.setOnKeywordSelected(this);
        suggestionPopupWindow.showAsDropDown(mToolbar, 0, 0);

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    public void onEventMainThread(BaseEvent.SuggestionsEvent obj) {
        if (obj.keywords != null && obj.keywords.size() > 0) {
            initListPopupWindow(obj.keywords);
        } else {
            if (suggestionPopupWindow != null)
                suggestionPopupWindow.dismiss();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mQueryText = newText;
        if (!TextUtils.isEmpty(mQueryText)) {
            if (isShowSuggestions) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        CommonApi.getInstance().getHotWordsList(mQueryText,
                                SuggestionsBaseActivity.this);
                    }
                }, 500);
            }
        }

        return false;
    }

    @Override
    public void keywordSelected(String keyword) {
        isShowSuggestions = false;
        searchView.setQuery(keyword, true);
    }

    public void onEventMainThread(BaseEvent.SearchTopicListEvent obj) {
        isShowSuggestions = true;
        searchView.onActionViewCollapsed();
    }

    public void onEventMainThread(BaseEvent.SearchGroupListEvent obj) {
        isShowSuggestions = true;
        searchView.onActionViewCollapsed();
    }

}
