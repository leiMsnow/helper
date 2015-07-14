package io.rong.imkit.widget.provider;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;
import com.sea_monster.resource.Resource;
import io.rong.imkit.widget.AsyncImageView ;

/**
 * Created by jenny_zhou1980 on 15/3/17.
 */
@ProviderTag(messageContent = RichContentMessage.class)
public class RichContentMessageItemProvider extends IContainerItemProvider.MessageProvider<RichContentMessage>{
    class ViewHolder {
        AsyncImageView img;
        TextView title;
        TextView content;
        RelativeLayout mLayout;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_rich_content_message, null);


        ViewHolder holder = new ViewHolder();

        holder.title = (TextView) view.findViewById(R.id.rc_title);
        holder.content = (TextView)view.findViewById(R.id.rc_content);
        holder.img = (AsyncImageView) view.findViewById(R.id.rc_img);
        holder.mLayout = (RelativeLayout) view.findViewById(R.id.rc_layout);

        view.setTag(holder);
        return view;
    }

    @Override
    public void onItemClick(View view, int position, RichContentMessage content, Message message) {

    }

    @Override
    public void onItemLongClick(View view, int position, RichContentMessage content, final Message message) {
        String name = null;
        if (message.getConversationType().getName().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName()) ||
                message.getConversationType().getName().equals(Conversation.ConversationType.PUBLIC_SERVICE.getName())) {
            ConversationKey key = ConversationKey.obtain(message.getTargetId(), message.getConversationType());
            PublicServiceInfo info = RongContext.getInstance().getPublicServiceInfoCache().get(key.getKey());
            if (info != null)
                name = info.getName();
        } else {
            UserInfo userInfo = RongContext.getInstance().getUserInfoCache().get(message.getSenderUserId());
            if (userInfo != null)
                name = userInfo.getName();
        }
        String[] items;

        items = new String[]{view.getContext().getResources().getString(R.string.rc_dialog_item_message_delete)};

        ArraysDialogFragment.newInstance(name, items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            @Override
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if (which == 0)
                    RongIM.getInstance().getRongIMClient().deleteMessages(new int[]{message.getMessageId()}, null);

            }
        }).show(((FragmentActivity) view.getContext()).getSupportFragmentManager());
    }

    @Override
    public void bindView(View v, int position, RichContentMessage content, Message message) {
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.title.setText(content.getTitle());
        holder.content.setText(content.getContent());
        holder.img.setResource(content.getImgUrl() == null ? null : new Resource(content.getImgUrl()));

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.mLayout.setBackgroundResource(R.drawable.rc_ic_bubble_no_right);
        } else {
            holder.mLayout.setBackgroundResource(R.drawable.rc_ic_bubble_no_left);
        }
    }

    @Override
    public Spannable getContentSummary(RichContentMessage data) {
        return new SpannableString(RongContext.getInstance().getResources().getString(R.string.rc_message_content_rich_text));
    }
}
