package com.kwei.ilibrary.comm.EventBus;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher {
    private Map<BaseEventType, CopyOnWriteArrayList<Subscriber>> mSubscriberMap;

    public Dispatcher(Map<BaseEventType, CopyOnWriteArrayList<Subscriber>> subscribes) {
        mSubscriberMap = subscribes;
    }

    public void dispatchEvents(ThreadLocal<Queue<BaseEventType>> eventQueue, Object event) {
        Queue<BaseEventType> queue = eventQueue.get();
        while (queue.size() > 0) {
            handleEvent(queue.poll(), event);
        }
    }

    /*** 在 UI 线程处理 Event ***/
    Handler mEventHandler = new Handler(Looper.getMainLooper());

    private void handleEvent(BaseEventType type, final Object event) {
        List<Subscriber> subscriptions = mSubscriberMap.get(type);
        for (final Subscriber subscriber : subscriptions) {
            mEventHandler.post(new Runnable() {
                @Override
                public void run() {
                    subscriber.onEvent(event);
                }
            });
        }
    }

}
