import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;
import com.msi.evale9.service.ItemAccessCounter;
import com.msi.evale9.service.ItemPriorityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import static com.msi.evale9.model.Priority.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestWithRandomizedData {
    final static int SIZE = 300;
    final private static Collection<Item> randomItems = new ArrayList<>();
    static int criticalCount = 0, highCount = 0, mediumCount = 0, lowCount = 0, trivialCount = 0, nullCount = 0;

    @BeforeAll
    public static void createRandomizedDataCollection() {
        String previousKey = null, currentKey;
        boolean duplicateFlag = false;
        Priority[] priorities = Priority.values();
        int randomPriorityIndex;
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            // Decide if this key will be a duplicate of last key
            if (previousKey != null) {
                if (random.nextDouble() < 0.33) {
                    currentKey = previousKey;
                    duplicateFlag = true;
                } else {
                    currentKey = UUID.randomUUID().toString();
                    duplicateFlag = false;
                }
            } else {
                currentKey = UUID.randomUUID().toString();
            }

            randomPriorityIndex = (random.nextInt() & Integer.MAX_VALUE) % (priorities.length + 1);
            if (randomPriorityIndex == priorities.length) {
                Helpers.createNumberOfItems(null, 1, randomItems, currentKey);
                if (!duplicateFlag) {
                    nullCount++;
                }
            } else {
                switch (priorities[randomPriorityIndex]){
                    case CRITICAL:
                        Helpers.createNumberOfItems(CRITICAL, 1, randomItems, currentKey);
                        if (!duplicateFlag) {
                            criticalCount++;
                        }
                        break;
                    case HIGH:
                        Helpers.createNumberOfItems(HIGH, 1, randomItems, currentKey);
                        if (!duplicateFlag) {
                            highCount++;
                        }
                        break;
                    case MEDIUM:
                        Helpers.createNumberOfItems(MEDIUM, 1, randomItems, currentKey);
                        if (!duplicateFlag) {
                            mediumCount++;
                        }
                        break;
                    case LOW:
                        Helpers.createNumberOfItems(LOW, 1, randomItems, currentKey);
                        if (!duplicateFlag) {
                            lowCount++;
                        }
                        break;
                    case TRIVIAL:
                        Helpers.createNumberOfItems(TRIVIAL, 1, randomItems, currentKey);
                        if (!duplicateFlag) {
                            trivialCount++;
                        }
                        break;
                }
            }
            previousKey = currentKey;
        }
    }

    @Test
    public void testRandomAsyncCriticalPriorityCount() {
        ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
        TestItemAccessCounterAsynchronously.createItemWithUniqueKey(CRITICAL, criticalCount, itemAccessCounter);

        assertEquals(criticalCount, itemAccessCounter.getCreatedItemCount(CRITICAL));
    }

    @Test
    public void testRandomAsyncHighPriorityCount() {
        ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
        TestItemAccessCounterAsynchronously.createItemWithUniqueKey(HIGH, highCount, itemAccessCounter);

        assertEquals(highCount, itemAccessCounter.getCreatedItemCount(HIGH));
    }

    @Test
    public void testRandomAsyncMediumPriorityCount() {
        ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
        TestItemAccessCounterAsynchronously.createItemWithUniqueKey(MEDIUM, mediumCount, itemAccessCounter);

        assertEquals(mediumCount, itemAccessCounter.getCreatedItemCount(MEDIUM));
    }

    @Test
    public void testRandomAsyncLowPriorityCount() {
        ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
        TestItemAccessCounterAsynchronously.createItemWithUniqueKey(LOW, lowCount, itemAccessCounter);

        assertEquals(lowCount, itemAccessCounter.getCreatedItemCount(LOW));
    }

    @Test
    public void testRandomAsyncTrivialPriorityCount() {
        ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
        TestItemAccessCounterAsynchronously.createItemWithUniqueKey(TRIVIAL, trivialCount, itemAccessCounter);

        assertEquals(trivialCount, itemAccessCounter.getCreatedItemCount(TRIVIAL));
    }

    @Test
    public void testRandomAsyncNoPriorityCount() {
        ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
        TestItemAccessCounterAsynchronously.createItemWithUniqueKey(null, nullCount, itemAccessCounter);

        assertEquals(nullCount, itemAccessCounter.getCreatedItemCount(null));
    }

    @Test
    public void testRandomItemPriorityServiceHasNoDuplicates() {
        ItemPriorityService itemPriorityService = new ItemPriorityService();

        Collection<Item> sortedItems = itemPriorityService.getPrioritizedItems(randomItems);

        assertTrue(Verifiers.hasNoDuplicates(sortedItems));
    }

    @Test
    public void testRandomItemPriorityServiceVerifyOrder() {
        int totalSize = criticalCount + highCount + mediumCount + lowCount + trivialCount + nullCount;
        ItemPriorityService itemPriorityService = new ItemPriorityService();

        Collection<Item> sortedItems = itemPriorityService.getPrioritizedItems(randomItems);

        assertTrue(Verifiers.verifyOrder(sortedItems) && totalSize == sortedItems.size());
    }
}
