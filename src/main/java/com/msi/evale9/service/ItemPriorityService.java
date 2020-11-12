package com.msi.evale9.service;

import com.msi.evale9.model.Item;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class ItemPriorityService {

    /**
     * Given an immutable collection of potentially duplicated, unordered items,
     * return a new collection of items with the following characteristics:
     * 1) sorted by priority - decreasing priority, with unprioritized (priority is null) items last.
     * 2) duplicates removed (duplicates defined by item key equality)
     * Note that priority can be negative.
     */
    public Collection<Item> getPrioritizedItems(Collection<Item> allItems) {
        if (allItems == null) {
            return new ArrayList<>();
        }

        Set<Item> itemSet = new HashSet<>();
        Collection<Item> sortedItems = new TreeSet<>();

        for (Item item : allItems) {
            if (!itemSet.contains(item)) {
                itemSet.add(item);
                sortedItems.add(item);
            }
        }

        return sortedItems;
    }
}
