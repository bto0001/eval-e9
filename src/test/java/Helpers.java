import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;
import com.msi.evale9.model.WorkItem;

import java.util.Collection;
import java.util.UUID;


public class Helpers {
    public static Collection<Item> createNumberOfItems(final Priority priority, final int count,
                                                       final Collection<Item> items, final String key) {
        for (int i = 0; i < count; i++) {
            items.add(new WorkItem(key, priority));
        }
        return items;
    }

    public static Collection<Item> createNumberOfUniqueItems(final Priority priority, final int count,
                                                             final Collection<Item> items) {
        for (int i = 0; i < count; i++) {
            items.add(new WorkItem(UUID.randomUUID().toString(), priority));
        }
        return items;
    }
}
