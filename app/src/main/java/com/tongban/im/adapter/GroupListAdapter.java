package com.tongban.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.model.Group;

import java.util.List;

/**
 * 圈子列表的Adapter
 * author: chenenyu 15/7/14
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {

    private Context context;
    private List<Group> groups;

    /**
     * 点击事件的监听
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public GroupListAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        GroupViewHolder holder = new GroupViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_group_list, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder groupViewHolder, final int i) {
        groupViewHolder.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        groupViewHolder.textView.setText(groups.get(i).getGroup_name());
        if (mOnItemClickLitener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int pos = groupViewHolder.getLayoutPosition();
                    int pos = i;
                    mOnItemClickLitener.onItemClick(groupViewHolder.itemView, pos);
                }
            });

            groupViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int pos = groupViewHolder.getLayoutPosition();
                    int pos = i;
                    mOnItemClickLitener.onItemLongClick(groupViewHolder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv);
            this.textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
