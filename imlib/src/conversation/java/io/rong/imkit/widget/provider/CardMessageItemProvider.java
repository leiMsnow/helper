package io.rong.imkit.widget.provider;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.sea_monster.resource.Resource;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.message.CardMessage;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;

/**
 * 卡片类型消息
 * Created by jenny_zhou1980 on 15/3/17.
 */
@ProviderTag(messageContent = CardMessage.class)
public class CardMessageItemProvider extends IContainerItemProvider.MessageProvider<CardMessage> {
    class ViewHolder {
        TextView title;
        TextView content;
        TextView price;
        Button finish;
        RelativeLayout mLayout;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_card_message, null);


        ViewHolder holder = new ViewHolder();

        holder.title = (TextView) view.findViewById(R.id.tv_title);
        holder.content = (TextView) view.findViewById(R.id.tv_serviceType);
        holder.price = (TextView) view.findViewById(R.id.tv_price);
        holder.finish = (Button) view.findViewById(R.id.btn_finish);
        holder.mLayout = (RelativeLayout) view.findViewById(R.id.rc_layout);

        view.setTag(holder);
        return view;
    }

    @Override
    public void onItemClick(View view, int position, CardMessage content, Message message) {

    }

    @Override
    public void onItemLongClick(View view, int position, CardMessage content, final Message message) {
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
    public void bindView(View v, int position, CardMessage content, Message message) {
        ViewHolder holder = (ViewHolder) v.getTag();
//        holder.title.setText(content.getTitle());
        holder.content.setText(content.getServiceType());
        holder.price.setText(content.getPrice());

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.mLayout.setBackgroundResource(R.drawable.rc_ic_bubble_no_right);
        } else {
            holder.mLayout.setBackgroundResource(R.drawable.ic_card_bg_left);
        }
    }

    @Override
    public Spannable getContentSummary(CardMessage data) {
        return new SpannableString(RongContext.getInstance().getResources().getString(R.string.rc_message_card));
    }
}
