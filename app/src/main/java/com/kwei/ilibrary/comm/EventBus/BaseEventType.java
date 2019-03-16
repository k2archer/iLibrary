package com.kwei.ilibrary.comm.EventBus;

public class BaseEventType {
    public String tag;

    public BaseEventType(String tag){
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        BaseEventType other = (BaseEventType) obj;

        if (tag == null) {
            return other.tag == null;
        } else return tag.equals(other.tag);
    }
}
