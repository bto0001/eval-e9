package com.msi.evale9.service;

import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class keeps track of the number of times items of a particular priority are created.
 * Each time an item is created, the handleItemCreated method will be invoked. This can happen very frequently and
 * will occur in a multithreaded environment, so recording the access counts must be done in a threadsafe manner.
 */
public class ItemAccessCounter {

    // Atomic counters for Priority
    private final AtomicInteger criticalCount;
    private final AtomicInteger highCount;
    private final AtomicInteger mediumCount;
    private final AtomicInteger lowCount;
    private final AtomicInteger trivialCount;
    private final AtomicInteger noPriorityCount;

    public ItemAccessCounter() {
        // Initializing counters
        criticalCount = new AtomicInteger();
        highCount = new AtomicInteger();
        mediumCount = new AtomicInteger();
        lowCount = new AtomicInteger();
        trivialCount = new AtomicInteger();
        noPriorityCount = new AtomicInteger();
    }

    /**
     * When an item is created, this method will be invoked.
     * Use this opportunity to count the items so that we can return the number created on demand below.
     * Keep in mind this will be called with high concurrency.
     */
    public void handleItemCreated(Item item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null Item to list");
        }

        // Switch statement doesn't like nulls
        if (item.getPriority() == null) {
            noPriorityCount.getAndIncrement();
        } else {
            switch (item.getPriority()) {
                case CRITICAL:
                    criticalCount.getAndIncrement();
                    break;
                case HIGH:
                    highCount.getAndIncrement();
                    break;
                case MEDIUM:
                    mediumCount.getAndIncrement();
                    break;
                case LOW:
                    lowCount.getAndIncrement();
                    break;
                case TRIVIAL:
                    trivialCount.getAndIncrement();
                    break;
            }
        }
    }

    /**
     * Return the number of times that handleItemCreated was called for the given priority.
     */
    public int getCreatedItemCount(Priority priority) {
        if (priority == null) {
            return noPriorityCount.get();
        }
        switch (priority) {
            case CRITICAL:
                return criticalCount.get();
            case HIGH:
                return highCount.get();
            case MEDIUM:
                return mediumCount.get();
            case LOW:
                return lowCount.get();
            case TRIVIAL:
                return trivialCount.get();
            default:
                 // Throwing RuntimeException so that the method signature doesn't need to change for this assignment
                throw new RuntimeException("Unknown Priority state");
        }
    }
}
