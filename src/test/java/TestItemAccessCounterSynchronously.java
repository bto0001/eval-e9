import static com.msi.evale9.model.Priority.*;
import static org.junit.jupiter.api.Assertions.*;

import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;
import com.msi.evale9.model.WorkItem;
import com.msi.evale9.service.ItemAccessCounter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;


public class TestItemAccessCounterSynchronously {

    private static final ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
    private static final Collection<Item> itemList = new ArrayList();

    static final int CRITICAL_COUNT = 100, HIGH_COUNT = 400, MED_COUNT = 200, LOW_COUNT = 200, TRIVIAL_COUNT = 100,
                     NO_PRIORITY_COUNT = 98;

    @BeforeAll
    public static void testItemCreation() {
        initializeItemListWithUniqueKeys(CRITICAL_COUNT, HIGH_COUNT, MED_COUNT, LOW_COUNT, TRIVIAL_COUNT,
                NO_PRIORITY_COUNT);
    }

    @Test
    public void testEmptyItemAccessCounter() {
        ItemAccessCounter counter = new ItemAccessCounter();
        assertEquals(0, counter.getCreatedItemCount(MEDIUM));
    }

    @Test
    public void testSynchronousCriticalPriorityCount() {
        assertEquals(CRITICAL_COUNT, itemAccessCounter.getCreatedItemCount(CRITICAL));
    }

    @Test
    public void testSynchronousHighPriorityCount() {
        assertEquals(HIGH_COUNT, itemAccessCounter.getCreatedItemCount(HIGH));
    }

    @Test
    public void testSynchronousMediumPriorityCount() {
        assertEquals(MED_COUNT, itemAccessCounter.getCreatedItemCount(MEDIUM));
    }

    @Test
    public void testSynchronousLowPriorityCount() {
        assertEquals(LOW_COUNT, itemAccessCounter.getCreatedItemCount(LOW));
    }

    @Test
    public void testSynchronousTrivialPriorityCount() {
        assertEquals(TRIVIAL_COUNT, itemAccessCounter.getCreatedItemCount(TRIVIAL));
    }

    @Test
    public void testSynchronousNoPriorityCount() {
        assertEquals(NO_PRIORITY_COUNT, itemAccessCounter.getCreatedItemCount(null));
    }

    @Test
    public void testWithNullKey() {
        assertThrows(IllegalArgumentException.class, () -> new WorkItem(null, TRIVIAL));
    }

    @Test
    public void testWithEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> new WorkItem("", TRIVIAL));
    }

//    @Test
//    public void testWithRedundantKey() {
//        Item duplicate = new WorkItem("duplicate", CRITICAL);
//        itemList.add(duplicate);
//        duplicate = new WorkItem("duplicate", HIGH);
//        itemList.add(duplicate);
//        assertTrue(CRITICAL_COUNT + 1 == itemList.stream().filter(e -> e.getPriority() == CRITICAL).count()
//            && HIGH_COUNT == itemList.stream().filter(e -> e.getPriority() == HIGH).count());
//    }

    private static void initializeItemListWithUniqueKeys(int criticalCount, int highCount, int mediumCount,
                                                         int lowCount, int trivialCount, int noPriorityCount) {

        createItemWithUniqueKey(CRITICAL, criticalCount);
        createItemWithUniqueKey(HIGH, highCount);
        createItemWithUniqueKey(MEDIUM, mediumCount);
        createItemWithUniqueKey(LOW, lowCount);
        createItemWithUniqueKey(TRIVIAL, trivialCount);
        createItemWithUniqueKey(null, noPriorityCount);
    }

    private static void createItemWithUniqueKey(Priority priority, int count) {
        Item workItem;
        for (int i = 0; i < count; i++) {
            workItem = new WorkItem(UUID.randomUUID().toString(), priority);
            itemAccessCounter.handleItemCreated(workItem);
            itemList.add(workItem);
        }
    }
}
