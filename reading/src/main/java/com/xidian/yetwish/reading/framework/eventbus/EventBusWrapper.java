package com.xidian.yetwish.reading.framework.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * 自己实现的EventBus的版本
 * Created by Yetwish on 2016/4/21 0021.
 */
public class EventBusWrapper {

    private static EventBusWrapper sInstance;

    private EventBus mRealEventBus;

    private EventBusWrapper() {

        mRealEventBus = new EventBus();

    }


    public static final EventBusWrapper getDefault() {
        if (sInstance == null) {
            synchronized (EventBusWrapper.class) {
                if (sInstance == null) {
                    sInstance = new EventBusWrapper();
                }
            }
        }
        return sInstance;
    }


    public void register(Object listener) {
        mRealEventBus.register(listener);
    }

    public void unregister(Object listener) {
        mRealEventBus.unregister(listener);
    }

    public void post(Object event) {
        mRealEventBus.post(event);
    }
}
