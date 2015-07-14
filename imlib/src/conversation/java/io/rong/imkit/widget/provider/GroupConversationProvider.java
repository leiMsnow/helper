package io.rong.imkit.widget.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import io.rong.imkit.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.util.RongDateUtils;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by jenny_zhou1980 on 15/1/24.
 */
@ConversationProviderTag(conversationType = "group", portraitPosition = 1)
public class GroupConversationProvider extends PrivateConversationProvider implements IContainerItemProvider.ConversationProvider<UIConversation> {
    @Override
    public String getTitle(String groupId) {
        String name;
        if (RongContext.getInstance().getGroupInfoFromCache(groupId) == null) {
            name = "";
        } else {
            name = RongContext.getInstance().getGroupInfoFromCache(groupId).getName();
        }
        return name;
    }

    @Override
    public Uri getPortraitUri(String id) {
        Uri uri;
        if (RongContext.getInstance().getGroupInfoFromCache(id) == null) {
            uri = null;
        } else {
            uri = RongContext.getInstance().getGroupInfoFromCache(id).getPortraitUri();
        }
        return uri;
    }


}
