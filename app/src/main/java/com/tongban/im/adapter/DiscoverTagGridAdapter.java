package com.tongban.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.im.R;
import com.tongban.im.model.Tag;

import java.util.List;

/**
 * 发现搜索页标签的GridAdapter
 * Created by Cheney on 15/9/10.
 */
public class DiscoverTagGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Tag> mTags;
    private LayoutInflater mInflater;

    public DiscoverTagGridAdapter(Context context, List<Tag> tags) {
        mContext = context;
        mTags = tags;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mTags.size();
    }

    @Override
    public Object getItem(int position) {
        return mTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_disvocer_tag_grid, parent, false);
        ImageView bg = (ImageView) convertView.findViewById(R.id.iv_bg);
        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        Glide.with(mContext).load(mTags.get(position).getTag_desc().trim()).into(bg);
        title.setText(mTags.get(position).getTag_name());
        convertView.setClickable(false);
        return convertView;
    }
}