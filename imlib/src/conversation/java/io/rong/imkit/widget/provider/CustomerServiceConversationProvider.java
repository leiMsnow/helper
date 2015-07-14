package io.rong.imkit.widget.provider;

import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;

/**
 * Created by jenny_zhou1980 on 15/6/17.
 */
@ConversationProviderTag(conversationType = "customer_service", portraitPosition = 1)
public class CustomerServiceConversationProvider extends PrivateConversationProvider implements IContainerItemProvider.ConversationProvider<UIConversation>{


}
