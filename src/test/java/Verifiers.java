import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Verifiers {
    public static boolean verifyOrder(final Collection<Item> items) {
        Iterator<Item> iterator = items.iterator();
        Item currentItem;

        // Set initial previous priority
        Priority previousPriority = iterator.hasNext() ? iterator.next().getPriority() : null;
        while (iterator.hasNext()) {
            currentItem = iterator.next();
            if (previousPriority != null && currentItem.getPriority() != null) {
                if (previousPriority.ordinal() <= currentItem.getPriority().ordinal()) {
                    previousPriority = currentItem.getPriority();
                } else {
                    return false;
                }
            } else {
                if (previousPriority == null && currentItem.getPriority() != null) {
                    return false;
                } else {
                    previousPriority = currentItem.getPriority();
                }
            }
        }
        return true;
    }

    public static boolean hasNoDuplicates(final Collection<Item> items) {
        Iterator<Item> iterator = items.iterator();
        Set<String> itemKeySet = new HashSet<>();
        Item currentItem;

        while(iterator.hasNext()) {
            currentItem = iterator.next();
            if (itemKeySet.contains(currentItem.getKey())) {
                return false;
            } else {
                itemKeySet.add(currentItem.getKey());
            }
        }

        return true;
    }

}
