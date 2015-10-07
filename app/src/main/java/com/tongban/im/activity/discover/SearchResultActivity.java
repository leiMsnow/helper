package com.tongban.im.activity.discover;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.im.R;
import com.tongban.im.activity.base.SuggestionsBaseActivity;
import com.tongban.im.fragment.user.ProductListFragment;
import com.tongban.im.fragment.user.ThemeListFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.discover.ProductBook;
import com.tongban.im.model.discover.Theme;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * 搜索结果页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class SearchResultActivity extends SuggestionsBaseActivity {

    @Bind(R.id.ccv_theme)
    ChangeColorView ccvTheme;
    @Bind(R.id.ccv_product)
    ChangeColorView ccvProduct;
    @Bind(R.id.v_indicator)
    View mIndicator;
    @Bind(R.id.vp_search_result)
    ViewPager mViewPager;

    private FragmentPagerAdapter mAdapter;

    private List<Fragment> mFragments = new ArrayList<>();
    private List<ChangeColorView> mTabs = new ArrayList<>();
    private ThemeListFragment mThemeListFragment;
    private ProductListFragment mProductListFragment;

    private int mIndicatorWidth;

    // 是否是第一次在该页面进行搜索
    private boolean isFirstSearch = true;
    // 存储搜索结果
    private List<Theme> mThemeList;
    // 存储搜索结果
    private List<ProductBook> mProductBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discover_search_result;
    }

    /**
     * 设置指示器宽度
     *
     * @param count 分割数量
     */
    private void initIndicator(int count) {
        mIndicatorWidth = ScreenUtils.getScreenWidth(mContext) / count;
        ViewGroup.LayoutParams lp = mIndicator.getLayoutParams();
        lp.width = mIndicatorWidth;
        mIndicator.setLayoutParams(lp);
    }

    @Override
    protected void initData() {
        super.initData();
        ccvTheme.setIconAlpha(1.0f);
        initIndicator(2);
        mTabs.add(ccvTheme);
        mTabs.add(ccvProduct);
        //专题搜索列表Fragment
        mThemeListFragment = ThemeListFragment.newInstance(0);
        mFragments.add(mThemeListFragment);
        //单品搜索列表Fragment
        mProductListFragment = ProductListFragment.newInstance(0);
        mFragments.add(mProductListFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mQueryText = uri.getQueryParameter("keyword");
            isExpanded = Boolean.parseBoolean(uri.getQueryParameter("isExpanded"));
        }
        // 搜索
        if (!TextUtils.isEmpty(mQueryText)) {
            mThemeListFragment.searchTheme(mQueryText);
            mProductListFragment.searchProduct(mQueryText);
        }
    }


    @Override
    protected int getMenuInflate() {
        return R.menu.menu_search_topic;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            suggestionsListView.setVisibility(View.GONE);
            isShowSuggestions = true;
            mThemeListFragment.searchTheme(query);
            mProductListFragment.searchProduct(query);
        }
        return false;
    }

    @OnPageChange(value = R.id.vp_search_result, callback = OnPageChange.Callback.PAGE_SCROLLED)
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            mTabs.get(position).setIconAlpha(1 - positionOffset);
            mTabs.get(position + 1).setIconAlpha(positionOffset);
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
        lp.leftMargin = (int) (mIndicatorWidth * (position + positionOffset));
        mIndicator.setLayoutParams(lp);
    }

    private void resetTabs(int index) {
        for (int i = 0; i < mTabs.size(); i++) {
            mTabs.get(i).setIconAlpha(0.0f);
        }
        mTabs.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);
    }

    @OnClick({R.id.ccv_theme,R.id.ccv_product})
    public void onClick(View v) {
        if (v == ccvTheme) {
            if (mViewPager.getCurrentItem() != 0)
                resetTabs(0);
        } else if (v == ccvProduct) {
            if (mViewPager.getCurrentItem() != 1)
                resetTabs(1);
        }
    }

    /**
     * 搜索专题成功的Event,用于确定切换到专题列表还是单品列表,只有第一次搜索有效
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchThemeResultEvent event) {
        mThemeList = event.mThemes;
        if (mThemeList != null && mThemeList.size() > 0) {
            if (mProductBookList == null || mProductBookList.size() == 0 || isFirstSearch) {
                ccvTheme.performClick();
                isFirstSearch = false;
            }
        }
    }

    /**
     * 搜索单品成功的Event,用于确定切换到专题列表还是单品列表
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchProductResultEvent event) {
        mProductBookList = event.mProductBooks;
        if (mProductBookList != null && mProductBookList.size() > 0) {
            if (mThemeList == null || mThemeList.size() == 0) {
                ccvProduct.performClick();
            }
        }
    }
}
