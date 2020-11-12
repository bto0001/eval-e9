import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;
import com.msi.evale9.model.WorkItem;
import com.msi.evale9.service.ItemPriorityService;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.msi.evale9.model.Priority.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestItemPriorityService {
    private static final ItemPriorityService itemPriorityService = new ItemPriorityService();

    @Test
    public void testNullCollection() {
        assertDoesNotThrow(() -> itemPriorityService.getPrioritizedItems(null));
    }

    @Test
    public void testEmptyCollection() {
        Collection<Item> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> itemPriorityService.getPrioritizedItems(emptyList));
    }

    @Test
    public void testWorkItemComparator() {
        Item criticalItem = new WorkItem("critTest", CRITICAL);
        WorkItem lowItem = new WorkItem("lowTest", LOW);
        assertTrue(lowItem.compareTo(criticalItem) > 0);
    }

    @Test
    public void testPresortedCollectionWithDuplicates() {
        Collection<Item> unsortedItems = new ArrayList<>();
        Collection<Item> sortedItems;

        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfItems(priority, 1, unsortedItems, "sameName");
            Helpers.createNumberOfUniqueItems(priority, 40, unsortedItems);
        }
        Helpers.createNumberOfItems(null, 4, unsortedItems, "null");

        sortedItems = itemPriorityService.getPrioritizedItems(unsortedItems);

        assertTrue(Verifiers.verifyOrder(sortedItems));
        assertTrue(Verifiers.hasNoDuplicates(sortedItems));
    }

    @Test
    public void testItemPriorityServicePresortedCollectionWithNoDuplicatesAndThreeOfEachPriorityForOrder() {
        Collection<Item> unsortedItems = new ArrayList<>();
        Collection<Item> sortedItems;

        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfUniqueItems(priority, 30, unsortedItems);
        }
        Helpers.createNumberOfUniqueItems(null, 30, unsortedItems);

        sortedItems = itemPriorityService.getPrioritizedItems(unsortedItems);

        assertTrue(Verifiers.verifyOrder(sortedItems) && unsortedItems.size() == sortedItems.size());
    }

    @Test
    public void testItemPriorityServiceUnsortedCollectionWithOtherPriorityForOrder() {
        Collection<Item> unsortedItems = new ArrayList<>();
        Collection<Item> sortedItems;

        Helpers.createNumberOfUniqueItems(null, 3, unsortedItems);
        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfUniqueItems(null, 2, unsortedItems);
            Helpers.createNumberOfUniqueItems(priority, 3, unsortedItems);
            Helpers.createNumberOfUniqueItems(LOW, 1, unsortedItems);
        }
        Helpers.createNumberOfUniqueItems(null, 3, unsortedItems);

        sortedItems = itemPriorityService.getPrioritizedItems(unsortedItems);

        assertTrue(Verifiers.verifyOrder(sortedItems) && unsortedItems.size() == sortedItems.size());
    }

    @Test
    public void testReverseSortedCollection() {
        Collection<Item> reversedSort = new ArrayList<>();
        Collection<Item> sortedItems;
        Priority[] priorities = Priority.values();

        Helpers.createNumberOfUniqueItems(null, 5, reversedSort);
        for (int i = priorities.length - 1; i >= 0; i--) {
            Helpers.createNumberOfUniqueItems(priorities[i], 5, reversedSort);
        }

        sortedItems = itemPriorityService.getPrioritizedItems(reversedSort);

        assertTrue(Verifiers.verifyOrder(sortedItems) && reversedSort.size() == sortedItems.size());
    }

    @Test
    public void testAllDuplicates() {
        Collection<Item> duplicatedItems = new ArrayList<>();
        Collection<Item> sortedItems;
        String duplicateKey = "duplicate";

        Helpers.createNumberOfItems(null, 10, duplicatedItems, duplicateKey);
        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfItems(priority, 10, duplicatedItems, duplicateKey);
        }

        sortedItems = itemPriorityService.getPrioritizedItems(duplicatedItems);

        Iterator<Item> iterator = sortedItems.iterator();
        Item nullPriorityItem = iterator.hasNext() ? iterator.next() : null;

        assertTrue(nullPriorityItem != null && sortedItems.size() == 1 && nullPriorityItem.getPriority() == null);
    }

}
