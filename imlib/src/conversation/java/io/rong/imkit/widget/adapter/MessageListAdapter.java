package io.rong.imkit.widget.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.util.TimeUtils;
import io.rong.imkit.widget.ProviderContainerView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;

import com.bumptech.glide.Glide;
import com.sea_monster.resource.Resource;

import org.w3c.dom.Text;

import io.rong.imkit.widget.AsyncImageView;
import io.rong.message.TextMessage;


/**
 * Created by DragonJ on 14-10-11.
 */
public class MessageListAdapter extends BaseAdapter<Message> {
    LayoutInflater mInflater;
    Context mContext;
    Drawable mDefaultDrawable;
    OnItemHandlerListener mOnItemHandlerListener;

    class ViewHolder {
        // TODO 修改头像样式
        ImageView leftIconView;
        ImageView rightIconView;
        TextView nameView;
        ProviderContainerView contentView;
        ProgressBar progressBar;
        ImageView warning;
        ViewGroup layout;
        TextView time;
    }

    public MessageListAdapter(Context context) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDefaultDrawable = context.getResources().getDrawable(R.drawable.rc_ic_def_msg_portrait);
    }

    public void setOnItemHandlerListener(OnItemHandlerListener onItemHandlerListener) {
        this.mOnItemHandlerListener = onItemHandlerListener;
    }

    public interface OnItemHandlerListener {
        public void onWarningViewClick(int position, Message data, View v);
    }

    @Override
    public long getItemId(int position) {
        Message message = getItem(position);
        if (message == null)
            return -1;
        return message.getMessageId();
    }

    @Override
    protected View newView(final Context context, final int position, ViewGroup group) {
        View result = mInflater.inflate(R.layout.rc_item_message, null);

        final ViewHolder holder = new ViewHolder();
        holder.leftIconView = findViewById(result, R.id.rc_left);
        holder.rightIconView = findViewById(result, R.id.rc_right);
        holder.nameView = findViewById(result, R.id.rc_title);
        holder.contentView = findViewById(result, R.id.rc_content);
        holder.layout = findViewById(result, R.id.rc_layout);
        holder.progressBar = findViewById(result, R.id.rc_progress);
        holder.warning = findViewById(result, R.id.rc_warning);
        holder.time = findViewById(result, R.id.rc_time);

        result.setTag(holder);

        return result;
    }

    @Override
    protected void bindView(View v, final int position, final Message data) {

        ViewHolder holder = (ViewHolder) v.getTag();

        IContainerItemProvider provider = RongContext.getInstance().getMessageTemplate(data.getContent().getClass());

        View view = holder.contentView.inflate(provider);

        provider.bindView(view, position, data);

        ProviderTag tag = RongContext.getInstance().getMessageProviderTag(data.getContent().getClass());

        if (tag.hide()) {
            holder.contentView.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.nameView.setVisibility(View.GONE);
            holder.leftIconView.setVisibility(View.GONE);
            holder.rightIconView.setVisibility(View.GONE);
        } else {
            holder.contentView.setVisibility(View.VISIBLE);
        }

        if (data.getMessageDirection() == io.rong.imlib.model.Message.MessageDirection.SEND) {
            if (tag.showPortrait()) {
                holder.rightIconView.setVisibility(View.VISIBLE);
                holder.leftIconView.setVisibility(View.GONE);
            } else {
                holder.leftIconView.setVisibility(View.GONE);
                holder.rightIconView.setVisibility(View.GONE);
            }

            if (!tag.centerInHorizontal()) {
                setGravity(holder.layout, Gravity.RIGHT);
                holder.contentView.containerViewRight();
                holder.nameView.setGravity(Gravity.RIGHT);
            } else {
                setGravity(holder.layout, Gravity.CENTER);
                holder.contentView.containerViewCenter();
                holder.nameView.setGravity(Gravity.CENTER_HORIZONTAL);
                holder.contentView.setBackgroundColor(Color.TRANSPARENT);
            }

            if (data.getSentStatus() == Message.SentStatus.SENDING) {
                if (tag.showProgress())
                    holder.progressBar.setVisibility(View.VISIBLE);
                else
                    holder.progressBar.setVisibility(View.GONE);

                holder.warning.setVisibility(View.GONE);
            } else if (data.getSentStatus() == Message.SentStatus.FAILED) {
                holder.progressBar.setVisibility(View.GONE);
                holder.warning.setVisibility(View.VISIBLE);
            } else if (data.getSentStatus() == Message.SentStatus.SENT) {
                holder.progressBar.setVisibility(View.GONE);
                holder.warning.setVisibility(View.GONE);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                holder.warning.setVisibility(View.GONE);
            }
            holder.nameView.setVisibility(View.GONE);

            holder.rightIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.getSenderUserId());
                    if (userInfo == null) {
                        userInfo = new UserInfo(data.getSenderUserId(), null, null);
                    }

                    if (RongContext.getInstance().getConversationBehaviorListener() != null)
                        RongContext.getInstance().getConversationBehaviorListener().onUserPortraitClick(mContext, data.getConversationType(), userInfo);

                }
            });

            holder.rightIconView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.getSenderUserId());
                    if (userInfo == null) {
                        userInfo = new UserInfo(data.getSenderUserId(), null, null);
                    }

                    if (RongContext.getInstance().getConversationBehaviorListener() != null)
                        return RongContext.getInstance().getConversationBehaviorListener().onUserPortraitLongClick(mContext, data.getConversationType(), userInfo);

                    return true;
                }
            });

            if (tag.showWarning() == false)
                holder.warning.setVisibility(View.GONE);

        } else {
            if (tag.showPortrait()) {
                holder.rightIconView.setVisibility(View.GONE);
                holder.leftIconView.setVisibility(View.VISIBLE);
            } else {
                holder.leftIconView.setVisibility(View.GONE);
                holder.rightIconView.setVisibility(View.GONE);
            }

            if (!tag.centerInHorizontal()) {
                setGravity(holder.layout, Gravity.LEFT);
                holder.contentView.containerViewLeft();
                holder.nameView.setGravity(Gravity.LEFT);

            } else {
                setGravity(holder.layout, Gravity.CENTER);
                holder.contentView.containerViewCenter();
                holder.nameView.setGravity(Gravity.CENTER_HORIZONTAL);
                holder.contentView.setBackgroundColor(Color.TRANSPARENT);
            }

            holder.progressBar.setVisibility(View.GONE);
            holder.warning.setVisibility(View.GONE);

            holder.nameView.setVisibility(View.VISIBLE);

            if (data.getConversationType() == Conversation.ConversationType.PRIVATE || !tag.showPortrait()) {
                holder.nameView.setVisibility(View.GONE);
            } else {
                UserInfo userInfo = RongContext.getInstance().getUserInfoCache().get(data.getSenderUserId());
                if (userInfo == null)
                    holder.nameView.setText(data.getSenderUserId());
                else
                    holder.nameView.setText(userInfo.getName());
            }


            holder.leftIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.getSenderUserId());
                    if (RongContext.getInstance().getConversationBehaviorListener() != null)
                        RongContext.getInstance().getConversationBehaviorListener().onUserPortraitClick(mContext, data.getConversationType(), userInfo);

                }
            });

        }

        holder.leftIconView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.getSenderUserId());
                if (RongContext.getInstance().getConversationBehaviorListener() != null)
                    RongContext.getInstance().getConversationBehaviorListener().onUserPortraitLongClick(mContext, data.getConversationType(), userInfo);

                return false;
            }
        });


        if (holder.rightIconView.getVisibility() == View.VISIBLE) {
            if ((data.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE)
                    || data.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE))
                    && !TextUtils.isEmpty(data.getTargetId()) && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                ConversationKey mKey = ConversationKey.obtain(data.getTargetId(), data.getConversationType());
                PublicServiceInfo publicServiceInfo = RongContext.getInstance().getPublicServiceInfoFromCache(mKey.getKey());
                if ((publicServiceInfo != null) && publicServiceInfo.getPortraitUri() != null) {
//                    Resource resource = new Resource(publicServiceInfo.getPortraitUri());
//                    holder.rightIconView.setResource(resource);
                    Glide.with(mContext).load(publicServiceInfo.getPortraitUri()).placeholder(R.drawable.rc_default_portrait)
                            .into(holder.rightIconView);
                } else {
//                    holder.rightIconView.setResource(null);
                }
            } else if (!TextUtils.isEmpty(data.getSenderUserId())) {
                UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.getSenderUserId());
                if (userInfo != null && userInfo.getPortraitUri() != null) {
//                    Resource resource = new Resource(userInfo.getPortraitUri());
//                    holder.rightIconView.setResource(resource);
                    Glide.with(mContext).load(userInfo.getPortraitUri()).placeholder(R.drawable.rc_default_portrait).into(holder.rightIconView);

                } else {
//                    holder.rightIconView.setResource(null);
                }
            } else {
//                holder.rightIconView.setResource(null);
            }
        } else if (holder.leftIconView.getVisibility() == View.VISIBLE) {
            if ((data.getConversationType().equals(Conversation.ConversationType.PUBLIC_SERVICE)
                    || data.getConversationType().equals(Conversation.ConversationType.APP_PUBLIC_SERVICE))
                    && !TextUtils.isEmpty(data.getTargetId()) && data.getMessageDirection().equals(Message.MessageDirection.RECEIVE)) {
                ConversationKey mKey = ConversationKey.obtain(data.getTargetId(), data.getConversationType());
                PublicServiceInfo publicServiceInfo = RongContext.getInstance().getPublicServiceInfoFromCache(mKey.getKey());
                if ((publicServiceInfo != null) && publicServiceInfo.getPortraitUri() != null) {
//                    Resource resource = new Resource(publicServiceInfo.getPortraitUri());
//                    holder.leftIconView.setResource(resource);
                    Glide.with(mContext).load(publicServiceInfo.getPortraitUri()).placeholder(R.drawable.rc_default_portrait).into(holder.leftIconView);

                } else {
//                    holder.leftIconView.setResource(null);
                }
            } else if (!TextUtils.isEmpty(data.getSenderUserId())) {
                UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.getSenderUserId());
                if (userInfo != null && userInfo.getPortraitUri() != null) {
//                    Resource resource = new Resource(userInfo.getPortraitUri());
//                    holder.leftIconView.setResource(resource);
                    Glide.with(mContext).load(userInfo.getPortraitUri()).placeholder(R.drawable.rc_default_portrait).into(holder.leftIconView);

                } else {
//                    holder.leftIconView.setResource(null);
                }
            } else {
//                holder.leftIconView.setResource(null);
            }
        }

        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (RongContext.getInstance().getConversationBehaviorListener() != null)
                        if (RongContext.getInstance().getConversationBehaviorListener().onMessageClick(mContext, v, data))
                            return;

                    IContainerItemProvider.MessageProvider provider = RongContext.getInstance().getMessageTemplate(data.getContent().getClass());

                    provider.onItemClick(v, position, data.getContent(), data);

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (RongContext.getInstance().getConversationBehaviorListener() != null)
                        if (RongContext.getInstance().getConversationBehaviorListener().onMessageLongClick(mContext, v, data))
                            return false;

                    IContainerItemProvider.MessageProvider provider = RongContext.getInstance().getMessageTemplate(data.getContent().getClass());

                    provider.onItemLongClick(v, position, data.getContent(), data);
                    return false;

                }
            });
        }

        holder.warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemHandlerListener != null)
                    mOnItemHandlerListener.onWarningViewClick(position, data, v);
            }
        });


        if (tag.hide()) {
            holder.time.setVisibility(View.GONE);
            return;
        }

        if (position == 0) {
            holder.time.setText(TimeUtils.formatTime(data.getSentTime()));
        } else {
            Message pre = getItem(position - 1);
            if (data.getSentTime() - pre.getSentTime() > 60000) {
                holder.time.setText(TimeUtils.formatTime(data.getSentTime()));
                holder.time.setVisibility(View.VISIBLE);
            } else {
                holder.time.setVisibility(View.GONE);
            }
        }
    }

    private final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    @Override
    public void notifyDataSetChanged() {

        for (int i = (mList.size()-1); i > 0; i--) {
            MessageContent messageContent = mList.get(i).getContent();
            if (messageContent instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) messageContent;
                // TODO Auto-generated catch block
                if (textMessage.getContent().toString().contains("#")){
                    RongContext.getInstance().getEventBus().post(new Event.LastTopicNameEvent(textMessage.getContent()));
                    break;
                }
            }
        }

        super.notifyDataSetChanged();
    }
}
