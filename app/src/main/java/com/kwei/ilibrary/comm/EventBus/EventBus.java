package com.kwei.ilibrary.comm.EventBus;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final Map<BaseEventType, CopyOnWriteArrayList<Subscriber>> mSubscriberMap = new ConcurrentHashMap<>();
    private Dispatcher mDispatcher = new Dispatcher(mSubscriberMap);
    ThreadLocal<Queue<BaseEventType>> mLocalEvents = new ThreadLocal<Queue<BaseEventType>>() {
        @Override
        protected Queue<BaseEventType> initialValue() {
            return new ConcurrentLinkedQueue<>();
        }
    };

    private EventBus() {
    }

    private static class Instance {
        private static EventBus instance = new EventBus();
    }

    public static EventBus getInstance() {
        return Instance.instance;
    }


    public Subscriber register(Subscriber subscriber, BaseEventType tag) {
        CopyOnWriteArrayList<Subscriber> subscribes = mSubscriberMap.get(tag);
        if (subscribes == null) {
            subscribes = new CopyOnWriteArrayList<>();
        }

        if (!subscribes.contains(subscriber)) {
            subscribes.add(subscriber);
            mSubscriberMap.put(tag, subscribes);
        }

        return subscriber;
    }

    public void post(Object event, BaseEventType tag) {
        if (event == null) throw new RuntimeException(" event is null, tag is " + tag);

        mLocalEvents.get().offer(tag);

        mDispatcher.dispatchEvents(mLocalEvents, event);
    }

    public void unregister(Subscriber subscriber) {
        if (subscriber == null) return;

        synchronized (this) {
            Iterator<CopyOnWriteArrayList<Subscriber>> iterator = mSubscriberMap.values().iterator();
            while (iterator.hasNext()) {
                CopyOnWriteArrayList<Subscriber> subscribers = iterator.next();
                for (Subscriber subscription : subscribers) {
                    if (subscription.equals(subscriber)) {
                        subscribers.remove(subscriber);
                    }
                }
                if (subscribers.size() == 0) {
                    iterator.remove();
                }
            }
        }

    }
}
