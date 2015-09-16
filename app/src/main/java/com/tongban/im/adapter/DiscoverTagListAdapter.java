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
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.Tag;

import java.util.List;
import java.util.Map;

/**
 * 发现页搜索标签的adapter
 * Created by Cheney on 15/9/10.
 */
public class DiscoverTagListAdapter extends BaseExpandableListAdapter {

    private String[] type;

    private Context mContext;

    private Map<String, List<Tag>> datas;
    private ViewGroup.LayoutParams lp;

    public DiscoverTagListAdapter(Context context, Map<String, List<Tag>> datas, String[] type) {
        mContext = context;
        this.datas = datas;
        this.type = type;
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
        return datas.get(type[groupPosition]).get(childPosition);
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
        title.setText(String.valueOf(getGroup(groupPosition)));
        title.setTextColor(mContext.getResources().getColor(R.color.main_black));
        title.setTextSize(16);
        return title;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        final List<Tag> childData = datas.get(type[groupPosition]);
        ScrollableGridView gridView = new ScrollableGridView(mContext);
        DiscoverTagGridAdapter mGridAdapter = new DiscoverTagGridAdapter(mContext,
                R.layout.item_disvocer_tag_grid, childData);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setHorizontalScrollBarEnabled(false);
        gridView.setLayoutParams(lp);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(20);
        gridView.setVerticalSpacing(20);
        gridView.setGravity(Gravity.CENTER);
        gridView.setAdapter(mGridAdapter);
        // 子view的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keyword = "";
                if (groupPosition == 0) {
                    keyword = childData.get(position).getTag_name();
                } else if (groupPosition == 1) {
                    keyword = childData.get(position).getTag_name();
                } else if (groupPosition == 2) {
                    keyword = childData.get(position).getTag_name();
                }
                TransferCenter.getInstance().startThemeSearchResult(false,keyword);
            }
        });
        return gridView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
