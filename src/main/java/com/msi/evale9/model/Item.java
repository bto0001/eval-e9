package com.msi.evale9.model;

/**
 * A task or work item, uniquely identifiable by key, that potentially has some priority.
 * Priority may be null to indicate no priority.
 * Priority can be negative.
 */
public interface Item {

    String getKey();
    Priority getPriority();

}
