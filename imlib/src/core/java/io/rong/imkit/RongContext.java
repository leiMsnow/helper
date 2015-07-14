package io.rong.imkit;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;
import io.rong.common.ResourceUtils;
import io.rong.imkit.cache.RongCache;
import io.rong.imkit.cache.RongCacheWrap;
import io.rong.imkit.common.RongConst;
import io.rong.imkit.logic.MessageCounterLogic;
import io.rong.imkit.logic.MessageSoundLogic;
import io.rong.imkit.model.ConversationInfo;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.util.AndroidEmoji;
import io.rong.imkit.utils.SystemUtils;
import io.rong.imkit.widget.provider.AppServiceConversationProvider;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.CustomerServiceConversationProvider;
import io.rong.imkit.widget.provider.DiscussionConversationProvider;
import io.rong.imkit.widget.provider.GroupConversationProvider;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.PrivateConversationProvider;
import io.rong.imkit.widget.provider.PublicServiceConversationProvider;
import io.rong.imkit.widget.provider.PublicServiceMenuInputProvider;
import io.rong.imkit.widget.provider.SystemConversationProvider;
import io.rong.imkit.widget.provider.TextInputProvider;
import io.rong.imkit.widget.provider.VoiceInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceInfo;
import io.rong.imlib.model.UserInfo;
import com.sea_monster.common.PriorityRunnable;
import com.sea_monster.network.DiscardOldestPolicy;
import com.sea_monster.resource.ResourceHandler;

import org.w3c.dom.Text;

/**
 * Created by DragonJ on 14-7-29.
 */
public class RongContext extends ContextWrapper {
    private static RongContext sContext;
    //    private MessageLogic mMessageLogic;
    private EventBus mBus;

    private ThreadPoolExecutor mExecutor;
    private RongIM.ConversationBehaviorListener mConversationBehaviorListener;// 会话页面
    private RongIM.ConversationListBehaviorListener mConversationListBehaviorListener;// 会话列表页面
    private RongIM.PublicServiceBehaviorListener mPublicServiceBehaviorListener;//公众号界面
    private RongIM.OnSelectMemberListener mMemberSelectListener;
    private RongIM.OnSendMessageListener mOnSendMessageListener;//发送消息监听

    private RongIM.UserInfoProvider mUserInfoProvider;
    private RongIM.GroupInfoProvider mGroupProvider;
    private boolean mIsCacheUserInfo = true;
    private boolean mIsCacheGroupInfo = true;
//    private RongIM.GetNewMessageSoundProvider mNewMessageSoundProvider;


    private Map<Class<? extends MessageContent>, IContainerItemProvider.MessageProvider> mTemplateMap;
    private IContainerItemProvider.MessageProvider mDefaultTemplate;
    private Map<Class<? extends MessageContent>, ProviderTag> mProviderMap;
    private Map<String, IContainerItemProvider.ConversationProvider> mConversationProviderMap;
    private Map<String, ConversationProviderTag> mConversationTagMap;
    private Map<String, Boolean> mConversationTypeStateMap;

    private RongCache<String, UserInfo> mUserInfoCache;
    private RongCache<String, Group> mGroupCache;
    private RongCache<String, Discussion> mDiscussionCache;
    private RongCache<String, PublicServiceInfo> mPublicServiceInfoCache;
    private RongCache<String, Conversation.ConversationNotificationStatus> mNotificationCache;

    private InputProvider.MainInputProvider mPrimaryProvider;
    private InputProvider.MainInputProvider mSecondaryProvider;
    private InputProvider.MainInputProvider mMenuProvider;

    private RongIM.LocationProvider mLocationProvider;
    private MessageCounterLogic mCounterLogic;

    private List<ConversationInfo> mCurrentConversationList;

    private Map<Conversation.ConversationType, List<InputProvider.ExtendProvider>> mExtendProvider;


    ImageInputProvider mImageInputProvider;
    CameraInputProvider mCameraInputProvider;
    LocationInputProvider mLocationInputProvider;
    InputProvider.ExtendProvider mVoIPInputProvider;

    Handler mHandler;

    private UserInfo mCurrentUserInfo;

    private boolean isUserInfoAttached;


    static public void init(Context context) {

        String processName = SystemUtils.getCurProcessName(context);
        String mainProcessName = context.getPackageName();

        if (!mainProcessName.equals(processName))
            return;

        if (sContext == null)
            sContext = new RongContext(context);

        sContext.initRegister();
    }


    public static RongContext getInstance() {
        return sContext;
    }

    protected RongContext(Context base) {
        super(base);

        ResourceUtils.init(base);

        mBus = EventBus.getDefault();
        mHandler = new Handler(getMainLooper());

        mTemplateMap = new HashMap<Class<? extends MessageContent>, IContainerItemProvider.MessageProvider>();

        mProviderMap = new HashMap<Class<? extends MessageContent>, ProviderTag>();

        mConversationProviderMap = new HashMap<String, IContainerItemProvider.ConversationProvider>();

        mConversationTagMap = new HashMap<String, ConversationProviderTag>();

        mConversationTypeStateMap = new HashMap<String, Boolean>();

        mCounterLogic = new MessageCounterLogic(this);

        mCurrentConversationList = new ArrayList<ConversationInfo>();

        BlockingQueue<Runnable> mWorkQueue = new PriorityBlockingQueue<Runnable>(RongConst.WORK_QUEUE_MAX_COUNT);

        ThreadFactory mThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(final Runnable r) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        r.run();
                    }
                };

                return new Thread(runnable, "ConnectTask #" + mCount.getAndIncrement());
            }
        };

        mExecutor = new ThreadPoolExecutor(RongConst.DEF_THREAD_WORKER_COUNT, RongConst.MAX_THREAD_WORKER_COUNT, RongConst.CREATE_THREAD_TIME_SPAN,
                TimeUnit.SECONDS, mWorkQueue, mThreadFactory);

        //TODO DiscardOldestPolicy
        mExecutor.setRejectedExecutionHandler(new DiscardOldestPolicy());

        mExtendProvider = new HashMap<Conversation.ConversationType, List<InputProvider.ExtendProvider>>();
        initCache();

        new ResourceHandler.Builder().enableBitmapCache().setOutputSizeLimit(120).setType("kit").build(this);

        AndroidEmoji.init(this);

        PushNotificationManager.init(this);

        MessageSoundLogic.init(this);
    }

    private void initRegister() {

        registerDefaultConversationGatherState();
        registerConversationTemplate(new PrivateConversationProvider());
        registerConversationTemplate(new GroupConversationProvider());
        registerConversationTemplate(new DiscussionConversationProvider());
        registerConversationTemplate(new SystemConversationProvider());
        registerConversationTemplate(new CustomerServiceConversationProvider());
        registerConversationTemplate(new AppServiceConversationProvider());
        registerConversationTemplate(new PublicServiceConversationProvider());
        setPrimaryInputProvider(new TextInputProvider(sContext));
        setSecondaryInputProvider(new VoiceInputProvider(sContext));
        setMenuInputProvider(new PublicServiceMenuInputProvider(sContext));

        mImageInputProvider = new ImageInputProvider(sContext);
        mCameraInputProvider = new CameraInputProvider(sContext);
        mLocationInputProvider = new LocationInputProvider(sContext);


        try {
            Class<? extends InputProvider.ExtendProvider> type = (Class<? extends InputProvider.ExtendProvider>) Class.forName("io.rong.imkit.widget.provider.VoIPInputProvider");

            Constructor<? extends InputProvider.ExtendProvider> constructor = type.getDeclaredConstructor(RongContext.class);
            mVoIPInputProvider = constructor.newInstance(sContext);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        List<InputProvider.ExtendProvider> privateProvider = new ArrayList<InputProvider.ExtendProvider>();
        privateProvider.add(mImageInputProvider);
        privateProvider.add(mCameraInputProvider);
        privateProvider.add(mLocationInputProvider);

        if (mVoIPInputProvider != null)
            privateProvider.add(mVoIPInputProvider);

        List<InputProvider.ExtendProvider> chatRoomProvider = new ArrayList<InputProvider.ExtendProvider>();
        chatRoomProvider.add(mImageInputProvider);
        chatRoomProvider.add(mCameraInputProvider);
        chatRoomProvider.add(mLocationInputProvider);

        List<InputProvider.ExtendProvider> groupProvider = new ArrayList<InputProvider.ExtendProvider>();
        groupProvider.add(mImageInputProvider);
        groupProvider.add(mCameraInputProvider);
        groupProvider.add(mLocationInputProvider);

        List<InputProvider.ExtendProvider> customerProvider = new ArrayList<InputProvider.ExtendProvider>();
        customerProvider.add(mImageInputProvider);
        customerProvider.add(mCameraInputProvider);
        customerProvider.add(mLocationInputProvider);

        List<InputProvider.ExtendProvider> discussionProvider = new ArrayList<InputProvider.ExtendProvider>();
        discussionProvider.add(mImageInputProvider);
        discussionProvider.add(mCameraInputProvider);
        discussionProvider.add(mLocationInputProvider);

        List<InputProvider.ExtendProvider> publicProvider = new ArrayList<InputProvider.ExtendProvider>();
        publicProvider.add(mImageInputProvider);
        publicProvider.add(mCameraInputProvider);
        publicProvider.add(mLocationInputProvider);

        List<InputProvider.ExtendProvider> publicAppProvider = new ArrayList<InputProvider.ExtendProvider>();
        publicAppProvider.add(mImageInputProvider);
        publicAppProvider.add(mCameraInputProvider);
        publicAppProvider.add(mLocationInputProvider);

        List<InputProvider.ExtendProvider> systemProvider = new ArrayList<InputProvider.ExtendProvider>();
        systemProvider.add(mImageInputProvider);
        systemProvider.add(mCameraInputProvider);
        systemProvider.add(mLocationInputProvider);

        mExtendProvider.put(Conversation.ConversationType.PRIVATE, privateProvider);
        mExtendProvider.put(Conversation.ConversationType.CHATROOM, chatRoomProvider);
        mExtendProvider.put(Conversation.ConversationType.GROUP, groupProvider);
        mExtendProvider.put(Conversation.ConversationType.CUSTOMER_SERVICE, customerProvider);
        mExtendProvider.put(Conversation.ConversationType.DISCUSSION, discussionProvider);
        mExtendProvider.put(Conversation.ConversationType.APP_PUBLIC_SERVICE, publicAppProvider);
        mExtendProvider.put(Conversation.ConversationType.PUBLIC_SERVICE, publicProvider);
        mExtendProvider.put(Conversation.ConversationType.SYSTEM, systemProvider);
    }

    public ImageInputProvider getImageInputProvider() {
        return mImageInputProvider;
    }

    public CameraInputProvider getCameraInputProvider() {
        return mCameraInputProvider;
    }

    public LocationInputProvider getLocationInputProvider() {
        return mLocationInputProvider;
    }

    public InputProvider.ExtendProvider getVoIPInputProvider() {
        return mVoIPInputProvider;
    }

    private void initCache() {

        mUserInfoCache = new RongCacheWrap<String, UserInfo>(this, RongConst.Cache.USER_CACHE_MAX_COUNT) {

            Vector<String> mRequests = new Vector<String>();


            @Override
            public UserInfo obtainValue(final String key) {

                if (TextUtils.isEmpty(key))
                    return null;

                UserInfo userInfo = null;

                synchronized (mRequests) {
                    if (mRequests.contains(key))
                        return null;
                    mRequests.add(key);
                }

                if (getContext().getUserInfoProvider() != null) {

                    userInfo = getContext().getUserInfoProvider().getUserInfo(key);

                    mRequests.remove(key);

                    if (userInfo != null && !TextUtils.isEmpty(key) && key.equals(userInfo.getUserId())) {
                        RLog.i(this, "UserInfoCache", "getUserInfoProvider name " + userInfo.getName());

                        if (!isIsSync()) {
                            if(mIsCacheUserInfo == true) {
                                put(key, userInfo);
                            }
                            getEventBus().post(userInfo);
                        }
                        return userInfo;
                    } else {
                        RLog.w(this, "warning", "setUserInfoProvider() data error!");
                    }
                }
                return null;
            }
        };

        mGroupCache = new RongCacheWrap<String, Group>(this, RongConst.Cache.GROUP_CACHE_MAX_COUNT) {

            Vector<String> mRequests = new Vector<String>();

            @Override
            public Group obtainValue(String key) {

                if (TextUtils.isEmpty(key))
                    return null;

                Group group = null;

                synchronized (mRequests) {
                    if (mRequests.contains(key))
                        return null;
                    mRequests.add(key);
                }

                if (getContext().getGroupInfoProvider() != null) {
                    group = getContext().getGroupInfoProvider().getGroupInfo(key);
                    mRequests.remove(key);
                }

                if (group != null) {
                    if (!isIsSync()) {
                        if (mIsCacheGroupInfo == true) {
                            put(key, group);
                        }
                        getEventBus().post(group);
                    }
                }else{
                    group = new Group(key,null,null);
                    getEventBus().post(group);
                }

                return group;
            }
        };

        mDiscussionCache = new RongCacheWrap<String, Discussion>(this, RongConst.Cache.DISCUSSION_CACHE_MAX_COUNT) {
            Vector<String> mRequests = new Vector<String>();

            @Override
            public Discussion obtainValue(final String key) {

                if (TextUtils.isEmpty(key))
                    return null;

                synchronized (mRequests) {
                    if (mRequests.contains(key))
                        return null;
                    mRequests.add(key);
                }

                Discussion discussion = null;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                            RongIM.getInstance().getRongIMClient().getDiscussion(key, new RongIMClient.ResultCallback<Discussion>() {

                                @Override
                                public void onSuccess(Discussion discussion) {

                                    if (discussion != null) {
                                        mRequests.remove(key);
                                        getContext().getEventBus().post(discussion);
                                        put(key, discussion);
                                    }
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    mRequests.remove(key);
                                }
                            });
                        }
                    }
                });


                return discussion;


            }
        };

        mPublicServiceInfoCache = new RongCacheWrap<String, PublicServiceInfo>(this, RongConst.Cache.PUBLIC_ACCOUNT_CACHE_MAX_COUNT) {
            Vector<String> mRequests = new Vector<String>();

            @Override
            public PublicServiceInfo obtainValue(final String key) {
                String[] strs = null;

                if (key != null) {
                    strs = key.split("#@6RONG_CLOUD9@#");

                    if(strs.length < 1
                            || strs[0] == null
                            || strs[1] == null
                            || strs[0].isEmpty()
                            || strs[1].isEmpty())
                        return null;
                } else {
                    return null;
                }

                final String id = strs[0];
                final String type = strs[1];

                RLog.d(this, "PublicServiceInfoCache", "type = "+ type + ", id = " + id);
                synchronized (mRequests) {
                    if (mRequests.contains(key))
                        return null;
                    mRequests.add(key);
                }

                PublicServiceInfo publicAccountInfo = null;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                            Conversation.PublicServiceType publicServiceType = null;
                            if (Integer.parseInt(type) == Conversation.ConversationType.APP_PUBLIC_SERVICE.getValue())
                                publicServiceType = Conversation.PublicServiceType.APP_PUBLIC_SERVICE;
                            else if (Integer.parseInt(type) == Conversation.ConversationType.PUBLIC_SERVICE.getValue())
                                publicServiceType = Conversation.PublicServiceType.PUBLIC_SERVICE;
                            else {
                                RLog.d(this, "PublicServiceInfoCache", "not found type : " + type);
                                return;
                            }

                            RongIM.getInstance().getRongIMClient().getPublicServiceProfile(publicServiceType, id,
                                    new RongIMClient.ResultCallback<PublicServiceInfo>() {

                                        @Override
                                        public void onSuccess(PublicServiceInfo info) {
                                            if (info != null) {
                                                mRequests.remove(key);
                                                getContext().getEventBus().post(info);
                                                put(key, info);
                                            }
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode e) {
                                            mRequests.remove(key);
                                        }
                                    });
                        }
                    }
                });


                return publicAccountInfo;
            }
        };

        mNotificationCache = new RongCacheWrap<String, Conversation.ConversationNotificationStatus>(this, RongConst.Cache.NOTIFICATION_CACHE_MAX_COUNT) {
            Vector<String> mRequests = new Vector<String>();
            Conversation.ConversationNotificationStatus notificationStatus = null;

            @Override
            public Conversation.ConversationNotificationStatus obtainValue(final String key) {

                if (TextUtils.isEmpty(key))
                    return null;

                synchronized (mRequests) {
                    if (mRequests.contains(key))
                        return null;
                    mRequests.add(key);
                }

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {

                            final ConversationKey conversationKey = ConversationKey.obtain(key);

                            if (conversationKey != null) {

                                RongIM.getInstance().getRongIMClient().getConversationNotificationStatus(conversationKey.getType(),
                                        conversationKey.getTargetId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {

                                            @Override
                                            public void onSuccess(Conversation.ConversationNotificationStatus status) {
                                                mRequests.remove(key);
                                                put(key, status);
                                                getContext().getEventBus().post(new Event.ConversationNotificationEvent(conversationKey.getTargetId(),
                                                        conversationKey.getType(), notificationStatus));
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {
                                                mRequests.remove(key);
                                            }
                                        });
                            }
                        }
                    }
                });


                return notificationStatus;
            }
        };
    }

    public synchronized RongCache<String, UserInfo> getUserInfoCache() {
        return mUserInfoCache;
    }

    public RongCache<String, Group> getGroupInfoCache() {
        return mGroupCache;
    }

    public RongCache<String, Discussion> getDiscussionCache() {
        return mDiscussionCache;
    }

    public RongCache<String, PublicServiceInfo> getPublicServiceInfoCache() {
        return mPublicServiceInfoCache;
    }

    public List<ConversationInfo> getCurrentConversationList() {
        return mCurrentConversationList;
    }

    public EventBus getEventBus() {
        return mBus;
    }

    public MessageCounterLogic getMessageCounterLogic() {
        return mCounterLogic;
    }

    public PushNotificationManager getPushNotificationMng() {
        return PushNotificationManager.getInstance();
    }

    public void registerConversationTemplate(IContainerItemProvider.ConversationProvider provider) {
        ConversationProviderTag tag = provider.getClass().getAnnotation(ConversationProviderTag.class);
        if (tag == null)
            throw new RuntimeException("No ConversationProviderTag added with your provider!");
        mConversationProviderMap.put(tag.conversationType(), provider);
        mConversationTagMap.put(tag.conversationType(), tag);
    }

    public IContainerItemProvider.ConversationProvider getConversationTemplate(String conversationType) {
        return mConversationProviderMap.get(conversationType);
    }

    public ConversationProviderTag getConversationProviderTag(String conversationType) {
        if (!mConversationProviderMap.containsKey(conversationType)) {
            throw new RuntimeException("the conversation type hasn't been registered!");
        }
        return mConversationTagMap.get(conversationType);
    }

    public void registerDefaultConversationGatherState() {
        setConversationGatherState(Conversation.ConversationType.PRIVATE.getName(), false);
        setConversationGatherState(Conversation.ConversationType.GROUP.getName(), true);
        setConversationGatherState(Conversation.ConversationType.DISCUSSION.getName(), false);
        setConversationGatherState(Conversation.ConversationType.CHATROOM.getName(), false);
        setConversationGatherState(Conversation.ConversationType.CUSTOMER_SERVICE.getName(), false);
        setConversationGatherState(Conversation.ConversationType.SYSTEM.getName(), true);
        setConversationGatherState(Conversation.PublicServiceType.APP_PUBLIC_SERVICE.getName(), false);
        setConversationGatherState(Conversation.ConversationType.PUBLIC_SERVICE.getName(), false);

    }

    public void setConversationGatherState(String type, Boolean state) {
        if (type == null)
            throw new IllegalArgumentException("The name of the register conversation type can't be null");
        mConversationTypeStateMap.put(type, state);
    }

    public Boolean getConversationGatherState(String type) {
        if (mConversationTypeStateMap.containsKey(type) == false)
            throw new RuntimeException("The conversation type not found!");
        return mConversationTypeStateMap.get(type);
    }


    public void registerMessageTemplate(IContainerItemProvider.MessageProvider provider) {
        ProviderTag tag = provider.getClass().getAnnotation(ProviderTag.class);
        if (tag == null)
            throw new RuntimeException("ProviderTag not def MessageContent type");
        mTemplateMap.put(tag.messageContent(), provider);
        mProviderMap.put(tag.messageContent(), tag);
    }

    public IContainerItemProvider.MessageProvider getMessageTemplate(Class<? extends MessageContent> type) {
        IContainerItemProvider.MessageProvider provider = mTemplateMap.get(type);
        if (provider == null)
            return mDefaultTemplate;
        return provider;
    }

    ProviderTag mDefaultProviderTag;

    public void setDefaultMessageTemplate(IContainerItemProvider.MessageProvider provider) {
        mDefaultTemplate = provider;

        mDefaultProviderTag = mDefaultTemplate.getClass().getAnnotation(ProviderTag.class);
        if (mDefaultProviderTag == null)
            throw new RuntimeException("ProviderTag not def MessageContent type");
    }

    public ProviderTag getMessageProviderTag(Class<? extends MessageContent> type) {
        if (!mProviderMap.containsKey(type))
            return mDefaultProviderTag;
        return mProviderMap.get(type);
    }

    public void executorBackground(final Runnable runnable) {
        if (runnable == null)
            return;

        PriorityRunnable execRunnable = null;

        if (runnable instanceof PriorityRunnable) {
            execRunnable = (PriorityRunnable) runnable;
        } else {
            execRunnable = new PriorityRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            };
        }

        mExecutor.execute(execRunnable);
    }

    public void executorBackground(final PriorityRunnable runnable) {
        if (runnable == null)
            return;

        mExecutor.execute(runnable);
    }


    public UserInfo getUserInfoFromCache(String userId) {
        if(userId != null)
            return mUserInfoCache.get(userId);
        else
            return null;
    }

    public Group getGroupInfoFromCache(String groupId) {
        if(groupId != null)
            return mGroupCache.get(groupId);
        else
            return null;
    }

    public Discussion getDiscussionInfoFromCache(String discussionId) {
        return mDiscussionCache.get(discussionId);
    }

    public PublicServiceInfo getPublicServiceInfoFromCache(String messageKey) {
        return mPublicServiceInfoCache.get(messageKey);
    }

    public Conversation.ConversationNotificationStatus getConversationNotifyStatusFromCache(ConversationKey messageKey) {
        if(messageKey != null && messageKey.getKey() != null)
            return mNotificationCache.get(messageKey.getKey());
        else
            return null;
    }

    public void setConversationNotifyStatusToCache(ConversationKey conversationKey, Conversation.ConversationNotificationStatus status) {
        mNotificationCache.put(conversationKey.getKey(), status);
    }

    public RongIM.ConversationBehaviorListener getConversationBehaviorListener() {
        return mConversationBehaviorListener;
    }

    public void setConversationBehaviorListener(RongIM.ConversationBehaviorListener conversationBehaviorListener) {
        this.mConversationBehaviorListener = conversationBehaviorListener;
    }

    public RongIM.PublicServiceBehaviorListener getPublicServiceBehaviorListener() {
        return this.mPublicServiceBehaviorListener;
    }

    public void setPublicServiceBehaviorListener(RongIM.PublicServiceBehaviorListener publicServiceBehaviorListener) {
        this.mPublicServiceBehaviorListener = publicServiceBehaviorListener;
    }

    public void setOnMemberSelectListener(RongIM.OnSelectMemberListener listener) {
        this.mMemberSelectListener = listener;
    }

    public RongIM.OnSelectMemberListener getMemberSelectListener() {
        return mMemberSelectListener;
    }

    public void setGetUserInfoProvider(RongIM.UserInfoProvider provider, boolean isCache) {
        this.mUserInfoProvider = provider;
        this.mIsCacheUserInfo = isCache;
    }

    void setGetGroupInfoProvider(RongIM.GroupInfoProvider provider, boolean mIsCacheGroupInfo) {
        this.mGroupProvider = provider;
        this.mIsCacheGroupInfo = mIsCacheGroupInfo;
    }

    RongIM.UserInfoProvider getUserInfoProvider() {
        return mUserInfoProvider;
    }

    public RongIM.GroupInfoProvider getGroupInfoProvider() {
        return mGroupProvider;
    }

    public void addInputExtentionProvider(Conversation.ConversationType conversationType, InputProvider.ExtendProvider[] providers) {
        if (providers == null || conversationType == null)
            return;
        if (mExtendProvider.containsKey(conversationType)) {
            for (InputProvider.ExtendProvider p : providers) {
                mExtendProvider.get(conversationType).add(p);
            }
        }
    }

    public void resetInputExtentionProvider(Conversation.ConversationType conversationType, InputProvider.ExtendProvider[] providers) {
        if (conversationType == null)
            return;
        if (mExtendProvider.containsKey(conversationType)) {
            mExtendProvider.get(conversationType).clear();
            if (providers == null)
                return;
            for (InputProvider.ExtendProvider p : providers) {
                mExtendProvider.get(conversationType).add(p);
            }
        }
    }


    public void setPrimaryInputProvider(InputProvider.MainInputProvider provider) {
        mPrimaryProvider = provider;
        mPrimaryProvider.setIndex(0);
    }

    public void setSecondaryInputProvider(InputProvider.MainInputProvider provider) {
        mSecondaryProvider = provider;
        mSecondaryProvider.setIndex(1);
    }

    public void setMenuInputProvider(InputProvider.MainInputProvider provider) {
        mMenuProvider = provider;
    }

    public InputProvider.MainInputProvider getSecondaryInputProvider() {
        return mSecondaryProvider;
    }

    public List<InputProvider.ExtendProvider> getRegisteredExtendProviderList(Conversation.ConversationType conversationType) {
        return mExtendProvider.get(conversationType);
    }

    public InputProvider.MainInputProvider getPrimaryInputProvider() {
        return mPrimaryProvider;
    }

    public InputProvider.MainInputProvider getMenuInputProvider() {
        return mMenuProvider;
    }

    public void registerConversationInfo(ConversationInfo info) {
        mCurrentConversationList.add(info);
    }

    public void unregisterConversationInfo(ConversationInfo info) {
        mCurrentConversationList.remove(info);
    }


    public RongIM.LocationProvider getLocationProvider() {
        return mLocationProvider;
    }

    public void setLocationProvider(RongIM.LocationProvider locationProvider) {
        this.mLocationProvider = locationProvider;
    }

    public RongIM.OnSendMessageListener getOnSendMessageListener() {
        return mOnSendMessageListener;
    }

    public void setOnSendMessageListener(RongIM.OnSendMessageListener onSendMessageListener) {
        mOnSendMessageListener = onSendMessageListener;
    }

    /**
     * 设置当前用户信息。
     *
     * @param userInfo 当前用户信息。
     */
    public void setCurrentUserInfo(UserInfo userInfo) {
        mCurrentUserInfo = userInfo;
    }

    /**
     * 获取当前用户信息。
     *
     * @return 当前用户信息。
     */
    public UserInfo getCurrentUserInfo() {
        if (mCurrentUserInfo != null)
            return mCurrentUserInfo;

        return null;
    }

    /**
     * 获取保存的token信息。
     *
     * @return 当前用户的token信息。
     */
    public String getToken() {
        return getSharedPreferences("rc_token", Context.MODE_PRIVATE).getString("token_value", "");
    }

    /**
     * 设置消息体内是否携带用户信息。
     *
     * @param state 是否携带用户信息？true:携带；false:不携带。
     */
    public void setUserInfoAttachedState(boolean state) {
        this.isUserInfoAttached = state;
    }

    /**
     * 获取当前用户关于消息体内是否携带用户信息的配置
     *
     * @return 是否携带用户信息
     */
    public boolean getUserInfoAttachedState() {
        return isUserInfoAttached;
    }


    public RongIM.ConversationListBehaviorListener getConversationListBehaviorListener() {
        return mConversationListBehaviorListener;
    }

    public void setConversationListBehaviorListener(RongIM.ConversationListBehaviorListener conversationListBehaviorListener) {
        mConversationListBehaviorListener = conversationListBehaviorListener;
    }
}
