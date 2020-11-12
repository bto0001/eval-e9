import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;
import com.msi.evale9.model.WorkItem;
import com.msi.evale9.service.ItemAccessCounter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.msi.evale9.model.Priority.*;;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestItemAccessCounterAsynchronously {
    private static final ItemAccessCounter itemAccessCounter = new ItemAccessCounter();
    private static final Collection<Item> itemList = new HashSet<>();

    static final int THREAD_COUNT = 40;

    @BeforeAll
    public static void testItemCreation() {
        initializeItemListWithUniqueKeys();
    }

    @Test
    public void testAsynchronousCriticalPriorityCount() {
        assertEquals(THREAD_COUNT, itemAccessCounter.getCreatedItemCount(CRITICAL));
    }

    @Test
    public void testAsynchronousHighPriorityCount() {
        assertEquals(THREAD_COUNT, itemAccessCounter.getCreatedItemCount(HIGH));
    }

    @Test
    public void testAsynchronousMediumPriorityCount() {
        assertEquals(THREAD_COUNT, itemAccessCounter.getCreatedItemCount(MEDIUM));
    }

    @Test
    public void testAsynchronousLowPriorityCount() {
        assertEquals(THREAD_COUNT, itemAccessCounter.getCreatedItemCount(LOW));
    }

    @Test
    public void testAsynchronousTrivialPriorityCount() {
        assertEquals(THREAD_COUNT, itemAccessCounter.getCreatedItemCount(TRIVIAL));
    }

    @Test
    public void testAsynchronousNoPriorityCount() {
        assertEquals(THREAD_COUNT, itemAccessCounter.getCreatedItemCount(null));
    }

    private static void initializeItemListWithUniqueKeys() {
        createItemWithUniqueKey(CRITICAL, THREAD_COUNT, itemAccessCounter);
        createItemWithUniqueKey(HIGH, THREAD_COUNT, itemAccessCounter);
        createItemWithUniqueKey(MEDIUM, THREAD_COUNT, itemAccessCounter);
        createItemWithUniqueKey(LOW, THREAD_COUNT, itemAccessCounter);
        createItemWithUniqueKey(TRIVIAL, THREAD_COUNT, itemAccessCounter);
        createItemWithUniqueKey(null, THREAD_COUNT, itemAccessCounter);
    }

    public static void createItemWithUniqueKey(Priority priority, int threadCount, ItemAccessCounter itemCounter) {
        if (threadCount == 0) {
            return;
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch allThreadsReady = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completedThreads = new CountDownLatch(threadCount);
        try {
            Runnable runnable = () -> {
                try {
                    Item workItem = new WorkItem(UUID.randomUUID().toString(), priority);
                    allThreadsReady.countDown(); // Countdown as threads reach here
                    startLatch.await(); // Wait for all thread requests to come in
                    itemCounter.handleItemCreated(workItem);
                    itemList.add(workItem);
                    completedThreads.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException("CountDownLatch threw an exception.", e);
                }
            };

            for (int i = 0; i < threadCount; i++) {
                executor.execute(runnable);
            }
            allThreadsReady.await(); // Wait for all threads to start
            startLatch.countDown(); // Start threads once all are here
            completedThreads.await(); // Wait for threads to finish to proceed
        } catch (InterruptedException e) {
            throw new RuntimeException("CountDownLatch threw an exception.", e);
        } finally {
            executor.shutdownNow();
        }
    }
}
