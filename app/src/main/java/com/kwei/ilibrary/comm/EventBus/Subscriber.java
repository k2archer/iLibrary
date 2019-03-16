package com.kwei.ilibrary.comm.EventBus;

public interface Subscriber<T> {
    void onEvent(T event);
}
