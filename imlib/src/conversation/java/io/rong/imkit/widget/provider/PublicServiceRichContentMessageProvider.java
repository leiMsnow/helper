package io.rong.imkit.widget.provider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.tools.RongWebviewActivity;
import io.rong.imkit.util.TimeUtils;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.PublicServiceRichContentMessage;
import com.sea_monster.resource.Resource;
import io.rong.imkit.widget.AsyncImageView ;

/**
 * Created by weiqinxiao on 15/4/18.
 */
@ProviderTag(messageContent = PublicServiceRichContentMessage.class, showPortrait = false, centerInHorizontal = true)
public class PublicServiceRichContentMessageProvider extends IContainerItemProvider.MessageProvider<PublicServiceRichContentMessage> {
    private Context mContext;

    @Override
    public View newView(Context context, ViewGroup group) {
        mContext = context;
        ViewHolder holder = new ViewHolder();
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_public_service_rich_content_message, null);

        holder.title = (TextView) view.findViewById(R.id.rc_title);
        holder.time = (TextView) view.findViewById(R.id.rc_time);
        holder.description = (TextView) view.findViewById(R.id.rc_content);
        holder.picurl = (AsyncImageView) view.findViewById(R.id.rc_img);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, PublicServiceRichContentMessage content, Message message) {
        ViewHolder holder = (ViewHolder) v.getTag();

        PublicServiceRichContentMessage msg = (PublicServiceRichContentMessage) message.getContent();

        holder.title.setText(msg.getMessage().getTitle());
        holder.description.setText(msg.getMessage().getDigest());
        holder.picurl.setResource(new Resource(msg.getMessage().getImageUrl()));
        holder.time.setText(TimeUtils.formatTime(message.getReceivedTime()));
    }

    @Override
    public Spannable getContentSummary(PublicServiceRichContentMessage data) {
        return new SpannableString(data.getMessage().getTitle());
    }

    @Override
    public void onItemClick(View view, int position, PublicServiceRichContentMessage content, Message message) {
        String url = content.getMessage().getUrl();
        Intent intent = new Intent(mContext, RongWebviewActivity.class);
        intent.putExtra("url", url);
        mContext.startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position, PublicServiceRichContentMessage content, final Message message) {
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

    private class ViewHolder {
        TextView title;
        AsyncImageView picurl;
        TextView time;
        TextView description;
    }

}
