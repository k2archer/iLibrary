package com.kwei.ilibrary.comm;

import com.kwei.ilibrary.comm.EventBus.BaseEventType;

public class EventTag extends BaseEventType {
    public static final String RECOMMENDED_LIST = "recommendedList";

    public EventTag(String tag) {
        super(tag);
    }
}
