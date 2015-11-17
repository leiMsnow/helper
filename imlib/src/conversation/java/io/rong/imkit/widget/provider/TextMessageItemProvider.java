package io.rong.imkit.widget.provider;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.text.method.LinkTouchMovementMethod;
import io.rong.imkit.text.method.TouchableSpan;
import io.rong.imkit.util.AndroidEmoji;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * Created by DragonJ on 14-8-2.
 */
@ProviderTag(messageContent = TextMessage.class)
public class TextMessageItemProvider extends IContainerItemProvider.MessageProvider<TextMessage> {

    private Context mContext;

    class ViewHolder {
        TextView message;
        boolean longClick;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_text_message, null);

        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(android.R.id.text1);
        holder.message.setAutoLinkMask(Linkify.ALL);
        holder.message.setSelected(false);
        holder.message.setMovementMethod(LinkMovementMethod.getInstance());
        holder.longClick = false;
        holder.message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                holder.longClick = true;
                return false;
            }
        });
        holder.message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewHolder holder = (ViewHolder) v.getTag();
                if (event.getAction() == MotionEvent.ACTION_UP && holder.longClick == true) {
                    holder.longClick = false;
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    holder.longClick = false;
                }
                return v.onTouchEvent(event);
            }
        });
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(TextMessage data) {
        if (data != null && data.getContent() != null)
            return new SpannableString(AndroidEmoji.ensure(data.getContent()));
        return null;
    }

    @Override
    public void onItemClick(View view, int position, TextMessage content, Message message) {

    }

    @Override
    public void onItemLongClick(final View view, int position, final TextMessage content, final Message message) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.longClick = true;
        if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            if (text != null && text instanceof Spannable)
                Selection.removeSelection((Spannable) text);
        }

        String name = null;
        if (message.getConversationType().getName().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName()) ||
                message.getConversationType().getName().equals(Conversation.ConversationType.PUBLIC_SERVICE.getName())) {
            ConversationKey key = ConversationKey.obtain(message.getTargetId(), message.getConversationType());
            PublicServiceInfo info = RongContext.getInstance().getPublicServiceInfoCache().get(key.getKey());
            if (info != null)
                name = info.getName();
        } else {
            if (message.getSenderUserId() != null) {
                UserInfo userInfo = RongContext.getInstance().getUserInfoCache().get(message.getSenderUserId());
                if (userInfo != null)
                    name = userInfo.getName();
            }
        }
        String[] items;

        items = new String[]{view.getContext().getResources().getString(R.string.rc_dialog_item_message_copy), view.getContext().getResources().getString(R.string.rc_dialog_item_message_delete)};

        ArraysDialogFragment.newInstance(name, items).setArraysDialogItemListener(new ArraysDialogFragment.OnArraysDialogItemListener() {
            @Override
            public void OnArraysDialogItemClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    @SuppressWarnings("deprecation")
                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(((TextMessage) content).getContent());
                } else if (which == 1) {
                    RongIM.getInstance().getRongIMClient().deleteMessages(new int[]{message.getMessageId()}, null);
                }

            }
        }).show(((FragmentActivity) view.getContext()).getSupportFragmentManager());
    }

    @Override
    public void bindView(View v, int position, TextMessage content, Message data) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (data.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_no_right);
        } else {
            holder.message.setBackgroundResource(R.drawable.rc_ic_bubble_no_left);
        }
        SpannableString ss = new SpannableString(content.getContent());
        setSpannable(holder.message, content.getContent(), ss);
        holder.message.setText(ss);
        holder.message.setMovementMethod(LinkTouchMovementMethod.getInstance());

        AndroidEmoji.ensure((Spannable) holder.message.getText());


//        holder.message.setText(AndroidEmoji.ensure(content.getContent()));

    }

    private void setSpannable(final TextView view, String text,
                              final SpannableString ss) {
        int start = text.indexOf("#");
        int end = text.lastIndexOf("#");
        if (start < 0 || start > end) {
            return;
        }
        final Event.TopicNameEvent topicNameEvent = new Event.TopicNameEvent();
        topicNameEvent.setTopicName(text.substring(start, (end+1)));
        ss.setSpan(new UnderlineSpan(), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TouchableSpan(mContext.getResources().getColor(android.R.color.holo_blue_dark),
                mContext.getResources().getColor(android.R.color.holo_blue_bright)) {

            @Override
            public void onClick(View v) {
                RongContext.getInstance().getEventBus().post(topicNameEvent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                view.setHighlightColor(mContext.getResources().getColor(android.
                        R.color.transparent));
            }

        }, start, (end + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
