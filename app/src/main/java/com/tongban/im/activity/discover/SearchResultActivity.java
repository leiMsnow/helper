package com.tongban.im.activity.discover;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ChangeColorView;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.ProductListFragment;
import com.tongban.im.fragment.user.ThemeListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class SearchResultActivity extends BaseToolBarActivity implements
        SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    private SearchView searchView;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private View mIndicator; // tab下的线
    private ChangeColorView ccvTheme;
    private ChangeColorView ccvProduct;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<ChangeColorView> mTabs = new ArrayList<>();
    private ThemeListFragment mThemeListFragment;
    private ProductListFragment mProductListFragment;

    private int mIndicatorWidth;
    private String mSearchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_discover_search_result;
    }

    @Override
    protected void initView() {
        ccvTheme = (ChangeColorView) findViewById(R.id.ccv_theme);
        ccvProduct = (ChangeColorView) findViewById(R.id.ccv_product);
        mIndicator = findViewById(R.id.v_indicator);
        mViewPager = (ViewPager) findViewById(R.id.vp_search_result);
        ccvTheme.setIconAlpha(1.0f);
        initIndicator(2);
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
    protected void initListener() {
        ccvTheme.setOnClickListener(this);
        ccvProduct.setOnClickListener(this);
    }

    @Override
    protected void initData() {
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
        mViewPager.addOnPageChangeListener(this);

        // 搜索
        if (getIntent() != null) {
            mSearchKey = getIntent().getStringExtra(Consts.KEY_SEARCH_VALUE);
            if (!TextUtils.isEmpty(mSearchKey)) {
                mThemeListFragment.searchTheme(mSearchKey);
                mProductListFragment.searchProduct(mSearchKey);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_topic, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchView.setQuery(mSearchKey, false);
            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewCollapsed();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            mThemeListFragment.searchTheme(query);
            mProductListFragment.searchProduct(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            mTabs.get(position).setIconAlpha(1 - positionOffset);
            mTabs.get(position + 1).setIconAlpha(positionOffset);
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIndicator.getLayoutParams();
        lp.leftMargin = (int) (mIndicatorWidth * (position + positionOffset));
        mIndicator.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void resetTabs(int index) {
        for (int i = 0; i < mTabs.size(); i++) {
            mTabs.get(i).setIconAlpha(0.0f);
        }
        mTabs.get(index).setIconAlpha(1.0f);
        mViewPager.setCurrentItem(index, false);
    }

    @Override
    public void onClick(View v) {
        if (v == ccvTheme) {
            if (mViewPager.getCurrentItem() != 0)
                resetTabs(0);
        } else if (v == ccvProduct) {
            if (mViewPager.getCurrentItem() != 1)
                resetTabs(1);
        }
    }
}
