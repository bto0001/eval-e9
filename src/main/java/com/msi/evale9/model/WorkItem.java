package com.msi.evale9.model;

import org.apache.commons.lang3.StringUtils;

/* Note: this class has a natural ordering that is inconsistent with equals */
public class WorkItem implements Item, Comparable<Item> {

    private String key;
    private Priority priority;

    public WorkItem(final String key, final Priority priority) {
        setKey(key);
        setPriority(priority);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("WorkItem key must be a valid string");
        }
        this.key = key;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Item)) {
            return false;
        }

        WorkItem that = (WorkItem) obj;
        return this.getKey().equals(that.getKey());
    }

    @Override
    public int compareTo(Item otherItem) {
        if (otherItem == null) {
            throw new NullPointerException("Trying to compare to null Item");
        }
        if (this.equals(otherItem)) {
            return 0;
        }
        if (this.getPriority() == null && otherItem.getPriority() == null) {
            return 1; // Can't return zero here or duplicate Priorities will be removed
        }
        if (this.getPriority() == null) {
            return 1;
        }
        if (otherItem.getPriority() == null) {
            return -1;
        }
        return this.getPriority().ordinal() < otherItem.getPriority().ordinal() ? -1 : 1;
    }
}
