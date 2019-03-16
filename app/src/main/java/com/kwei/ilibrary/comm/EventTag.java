package com.kwei.ilibrary.comm;

import com.kwei.ilibrary.comm.EventBus.BaseEventType;

public class EventTag extends BaseEventType {
    public static final String RECOMMENDED_LIST = "recommended_list";
    public static final String ORDERED_LIST = "ordered_list";

    public EventTag(String tag) {
        super(tag);
    }
}
