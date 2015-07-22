package com.tongban.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tongban.im.R;
import com.tongban.im.model.Group;
import com.tongban.im.model.User;
import com.tongban.im.widget.view.HexagonImageView;
import com.tongban.im.widget.view.HexagonLayout;

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
    public void onBindViewHolder(final GroupViewHolder groupViewHolder, final int pos) {
        // TODO 现在是模拟用户,待接口完善之后改为真实数据
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<User> members = groups.get(pos).getMembers();
        for (int i = 0; i < 4; i++) {
            if (i > 3)
                break;
            final HexagonImageView portrait = new HexagonImageView(context);
            portrait.setLayoutParams(layoutParams);
//            portrait.setImageResource(R.mipmap.ic_launcher);
            groupViewHolder.hl_group_portrait.addView(portrait);
            Glide.with(context).load("http://img5.imgtn.bdimg.com/it/u=3017210771,879699792&fm=11&gp=0.jpg")
                    .into(portrait);
        }
        groupViewHolder.tv_group_name.setText(groups.get(pos).getGroup_name());

        if (mOnItemClickLitener != null) {
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int pos = groupViewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(groupViewHolder.itemView, pos);
                }
            });

            groupViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int pos = groupViewHolder.getLayoutPosition();
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
        HexagonLayout hl_group_portrait;
        TextView tv_group_name, tv_unread_msg, tv_remind_at, tv_latest_msg_time;

        public GroupViewHolder(View itemView) {
            super(itemView);
            this.hl_group_portrait = (HexagonLayout) itemView.findViewById(R.id.hl_group_portrait);
            this.tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
            this.tv_unread_msg = (TextView) itemView.findViewById(R.id.tv_unread_msg);
            this.tv_remind_at = (TextView) itemView.findViewById(R.id.tv_remind_at);
            this.tv_latest_msg_time = (TextView) itemView.findViewById(R.id.tv_latest_msg_time);
        }
    }
}
