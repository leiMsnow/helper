package com.tongban.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tongban.corelib.widget.view.ScrollableGridView;
import com.tongban.im.R;
import com.tongban.im.activity.discover.SearchResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Tag;

import java.util.List;

/**
 * 发现页搜索标签的adapter
 * Created by Cheney on 15/9/10.
 */
public class DiscoverTagListAdapter extends BaseExpandableListAdapter {

    private String[] type = {"童书", "玩具", "早教"};

    private Context mContext;
    private List<Tag> mBooks, mToys, mChildEdus;

    private ViewGroup.LayoutParams lp;
    private DiscoverTagGridAdapter mGridAdapter;

    public DiscoverTagListAdapter(Context context, List<Tag> books, List<Tag> toys, List<Tag> childEdus) {
        mContext = context;
        mBooks = books;
        mToys = toys;
        mChildEdus = childEdus;
        lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getGroupCount() {
        return type.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return type[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return mBooks.get(childPosition);
        } else if (groupPosition == 1) {
            return mToys.get(childPosition);
        } else if (groupPosition == 2) {
            return mChildEdus.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView title = new TextView(mContext);
        title.setText((String) getGroup(groupPosition));
        title.setTextColor(mContext.getResources().getColor(R.color.main_black));
        title.setTextSize(16);
        return title;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ScrollableGridView gridView = new ScrollableGridView(mContext);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setLayoutParams(lp);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        gridView.setGravity(Gravity.CENTER);
        if (groupPosition == 0) {
            mGridAdapter = new DiscoverTagGridAdapter(mContext, mBooks);
        } else if (groupPosition == 1) {
            mGridAdapter = new DiscoverTagGridAdapter(mContext, mToys);
        } else if (groupPosition == 2) {
            mGridAdapter = new DiscoverTagGridAdapter(mContext, mChildEdus);
        }
        gridView.setAdapter(mGridAdapter);

        // 子view的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keyword = "";
                if (groupPosition == 0) {
                    keyword = mBooks.get(position).getTag_name();
                } else if (groupPosition == 1) {
                    keyword = mToys.get(position).getTag_name();
                } else if (groupPosition == 2) {
                    keyword = mChildEdus.get(position).getTag_name();
                }
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Consts.KEY_SEARCH_VALUE, keyword);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return gridView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
