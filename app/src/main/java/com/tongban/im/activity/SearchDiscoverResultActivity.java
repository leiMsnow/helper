package com.tongban.im.activity;

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
import com.tongban.im.fragment.MutipleProductFragment;
import com.tongban.im.fragment.SingleProductFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class SearchDiscoverResultActivity extends BaseToolBarActivity implements
        SearchView.OnQueryTextListener,ViewPager.OnPageChangeListener, View.OnClickListener {

    private SearchView searchView;
    private ViewPager vpResult;
    private View mIndicator;
    private FragmentPagerAdapter mAdapter;
    private ChangeColorView ccvMultiple;
    private ChangeColorView ccvSingle;

    private List<Fragment> mTabs = new ArrayList<>();
    private List<ChangeColorView> mTabIndicator = new ArrayList<>();

    private int mIndicatorWidth;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search_discover_result;
    }

    @Override
    protected void initView() {

        ccvMultiple = (ChangeColorView) findViewById(R.id.ccv_multiple_product);
        ccvSingle = (ChangeColorView) findViewById(R.id.ccv_single_product);
        mIndicator = findViewById(R.id.v_indicator);
        vpResult = (ViewPager) findViewById(R.id.vp_search_result);
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
    protected void initData() {

        mTabIndicator.add(ccvMultiple);
        mTabIndicator.add(ccvSingle);
        //专题搜索结果
        mTabs.add(new MutipleProductFragment());
        //单品搜索结果
        mTabs.add(new SingleProductFragment());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        vpResult.setAdapter(mAdapter);
        vpResult.addOnPageChangeListener(this);
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
            mTabIndicator.get(position).setIconAlpha(1 - positionOffset);
            mTabIndicator.get(position + 1).setIconAlpha(positionOffset);
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
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0.0f);
        }
        mTabIndicator.get(index).setIconAlpha(1.0f);
        vpResult.setCurrentItem(index, false);
    }

    @Override
    public void onClick(View v) {

    }
}
