package io.rong.imkit.widget.provider;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

public interface IContainerItemProvider<T extends Parcelable> {

    /**
     * 创建新View。
     *
     * @param context   当前上下文。
     * @param group     创建的新View所附属的父View。
     * @return  需要创建的新View。
     */
    public View newView(Context context, ViewGroup group);

    /**
     * 为View绑定数据。
     *
     * @param v         需要绑定数据的View。
     * @param position  绑定的数据位置。
     * @param data      绑定的数据。
     */
    public void bindView(View v, int position, T data);

    /**
     * 消息内容适配器。
     */
    public abstract class MessageProvider<K extends MessageContent> implements IContainerItemProvider<Message> {

        /**
         * 为View绑定数据。
         *
         * @param v         需要绑定数据的View。
         * @param position  绑定的数据位置。
         * @param data      绑定的消息。
         */
        @Override
        public final void bindView(View v, int position, Message data) {
            bindView(v, position, (K) data.getContent(), data);
        }

        /**
         *为View绑定数据。
         *
         * @param v         需要绑定数据的View。
         * @param position  绑定的数据位置。
         * @param content   绑定的消息内容。
         * @param message   绑定的消息。
         */
        public abstract void bindView(View v, int position, K content, Message message);

        /**
         * 当前数据的简单描述。
         *
         * @param data  当前需要绑定的数据
         * @return  数据的描述。
         */
        public final Spannable getSummary(Message data) {
            return getContentSummary((K) data.getContent());
        }

        /**
         * 当前数据的简单描述。
         *
         * @param data  当前需要绑定的数据
         * @return  数据的描述。
         */
        public abstract Spannable getContentSummary(K data);

        /**
         * View的点击事件。
         *
         * @param view      所点击的View。
         * @param position  点击的位置。
         * @param content   点击的消息内容。
         * @param message   点击的消息。
         */
        public abstract void onItemClick(View view, int position, K content, Message message);


        /**
         * View的长按事件。
         *
         * @param view      所长按的View。
         * @param position  长按的位置。
         * @param content   长按的消息内容。
         * @param message   长按的消息。
         */
        public abstract void onItemLongClick(final View view, int position, final K content, final Message message);

    }


    /**
     *
     * 会话适配器。
     */
    public interface ConversationProvider<T extends Parcelable> extends IContainerItemProvider<T> {
        /**
         * 绑定标题内容。
         *
         * @param id    需要绑定标题的Id。
         * @return      绑定标题内容。
         */
        public String getTitle(String id);

        /**
         * 绑定头像Uri。
         *
         * @param id    需要显示头像的Id。
         * @return      当前头像Uri。
         */
        public Uri getPortraitUri(String id);
    }


}
