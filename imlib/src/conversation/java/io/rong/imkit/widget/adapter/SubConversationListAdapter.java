package io.rong.imkit.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import com.sea_monster.resource.Resource;
import io.rong.imkit.widget.AsyncImageView;

/**
 * Created by jenny_zhou1980 on 15/2/4.
 */
public class SubConversationListAdapter extends BaseAdapter<UIConversation> {

    LayoutInflater mInflater;
    Context mContext;

    @Override
    public long getItemId(int position) {
        UIConversation conversation = getItem(position);
        if (conversation == null)
            return 0;
        return conversation.hashCode();
    }

    class ViewHolder {
        View layout;
        View leftImageLayout;
        View rightImageLayout;
        AsyncImageView leftImageView;
        AsyncImageView rightImageView;
        ProviderContainerView contentView;
        TextView unReadMsgCount;
        TextView unReadMsgCountRight;
    }

    public SubConversationListAdapter(Context context) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public int findPosition(io.rong.imlib.model.Conversation.ConversationType type, String targetId) {
        int index = getCount();
        int position = -1;

        while (index-- > 0) {
            if (getItem(index).getConversationType().equals(type)
                    && getItem(index).getConversationTargetId().equals(targetId)) {
                position = index;
                break;
            }
        }

        return position;
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View result = mInflater.inflate(R.layout.rc_item_conversation, null);

        ViewHolder holder = new ViewHolder();
        holder.layout = findViewById(result, R.id.rc_item_conversation);
        holder.leftImageLayout = findViewById(result, R.id.rc_item1);
        holder.rightImageLayout = findViewById(result, R.id.rc_item2);
        holder.leftImageView = findViewById(result, R.id.rc_left);
        holder.rightImageView = findViewById(result, R.id.rc_right);
        holder.contentView = findViewById(result, R.id.rc_content);
        holder.unReadMsgCount = findViewById(result, R.id.rc_unread_message);
        holder.unReadMsgCountRight = findViewById(result, R.id.rc_unread_message_right);

        result.setTag(holder);

        return result;
    }

    @Override
    protected void bindView(View v, int position, UIConversation data) {
        ViewHolder holder = (ViewHolder) v.getTag();

        /*通过会话类型，获得对应的会话provider.ex: PrivateConversationProvider*/
        IContainerItemProvider provider = RongContext.getInstance().getConversationTemplate(data.getConversationType().getName());

        View view = holder.contentView.inflate(provider);

        provider.bindView(view, position, data);

        //设置背景色
        if (data.isTop())
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.rc_conversation_top_bg));
        else
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.rc_text_color_primary_inverse));

        ConversationProviderTag tag = RongContext.getInstance().getConversationProviderTag(data.getConversationType().getName());

        // 1:图像靠左显示。2：图像靠右显示。3：不显示图像。
        if (tag.portraitPosition() == 1) {
            holder.leftImageLayout.setVisibility(View.VISIBLE);
            if (data.getConversationType().equals(Conversation.ConversationType.GROUP) || data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                holder.leftImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_group_default_portrait));
            }else{
                holder.leftImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_default_portrait));
            }

            if (data.getIconUrl() != null) {
                holder.leftImageView.setResource(new Resource(data.getIconUrl()));
            } else {
                holder.leftImageView.setResource(null);
            }

            if (data.getUnReadMessageCount() > 0) {
                holder.unReadMsgCount.setVisibility(View.VISIBLE);
                if (data.getUnReadMessageCount() > 99) {
                    holder.unReadMsgCount.setText(mContext.getResources().getString(R.string.rc_message_unread_count));
                } else {
                    holder.unReadMsgCount.setText(Integer.toString(data.getUnReadMessageCount()));
                }
            } else {
                holder.unReadMsgCount.setVisibility(View.INVISIBLE);
            }
            holder.rightImageLayout.setVisibility(View.GONE);
        } else if (tag.portraitPosition() == 2) {
            holder.rightImageLayout.setVisibility(View.VISIBLE);

            if (data.getConversationType().equals(Conversation.ConversationType.GROUP)|| data.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
                holder.rightImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_group_default_portrait));
            }else{
                holder.rightImageView.setDefaultDrawable(v.getContext().getResources().getDrawable(R.drawable.rc_default_portrait));
            }

            if (data.getIconUrl() != null) {
                holder.rightImageView.setResource(new Resource(data.getIconUrl()));
            } else {
                holder.rightImageView.setResource(null);
            }

            if (data.getUnReadMessageCount() > 0) {
                holder.unReadMsgCountRight.setVisibility(View.VISIBLE);
                if (data.getUnReadMessageCount() > 99) {
                    holder.unReadMsgCountRight.setText(mContext.getResources().getString(R.string.rc_message_unread_count));
                } else {
                    holder.unReadMsgCountRight.setText(Integer.toString(data.getUnReadMessageCount()));
                }
            }

            holder.leftImageLayout.setVisibility(View.GONE);
        } else if (tag.portraitPosition() == 3) {
            holder.rightImageLayout.setVisibility(View.GONE);
            holder.leftImageLayout.setVisibility(View.GONE);
        } else {
            throw new IllegalArgumentException("the portrait position is wrong!");
        }
    }
}
