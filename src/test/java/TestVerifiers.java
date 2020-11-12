import com.msi.evale9.model.Item;
import com.msi.evale9.model.Priority;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static com.msi.evale9.model.Priority.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestVerifiers {

    @Test
    public void testVerifyOrderPresortedCollectionWithNoDuplicatesAndThreeOfEachPriority() {
        Collection<Item> sortedItems = new ArrayList<>();

        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfItems(priority, 3, sortedItems, UUID.randomUUID().toString());
        }
        Helpers.createNumberOfItems(null, 3, sortedItems, UUID.randomUUID().toString());

        assertTrue(Verifiers.verifyOrder(sortedItems));
    }

    @Test
    public void testVerifyOrderUnsortedCollectionWithOtherPriority() {
        Collection<Item> sortedItems = new ArrayList<>();

        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfItems(priority, 3, sortedItems, UUID.randomUUID().toString());
            Helpers.createNumberOfItems(LOW, 1, sortedItems, UUID.randomUUID().toString());
        }
        Helpers.createNumberOfItems(null, 3, sortedItems, UUID.randomUUID().toString());

        assertFalse(Verifiers.verifyOrder(sortedItems));
    }

    @Test
    public void testVerifyOrderUnsortedCollectionWithExtraNulls() {
        Collection<Item> sortedItems = new ArrayList<>();

        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfItems(priority, 3, sortedItems, UUID.randomUUID().toString());
            Helpers.createNumberOfItems(null, 2, sortedItems, UUID.randomUUID().toString());
        }
        Helpers.createNumberOfItems(null, 3, sortedItems, UUID.randomUUID().toString());

        assertFalse(Verifiers.verifyOrder(sortedItems));
    }

    @Test
    public void testHasNoDuplicatesWithManyDuplicates() {
        Collection<Item> duplicatedItems = new ArrayList<>();
        String duplicateKey = "duplicate";

        Helpers.createNumberOfItems(null, 5, duplicatedItems, duplicateKey);
        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfItems(priority, 5, duplicatedItems, duplicateKey);
        }

        assertFalse(Verifiers.hasNoDuplicates(duplicatedItems));
    }

    @Test
    public void testHasNoDuplicatesWithOneSetOfDuplicates() {
        Collection<Item> duplicatedItems = new ArrayList<>();
        String duplicateKey = "duplicate";

        Helpers.createNumberOfItems(null, 1, duplicatedItems, duplicateKey);
        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfUniqueItems(priority, 1, duplicatedItems);
        }
        Helpers.createNumberOfItems(LOW, 1, duplicatedItems, duplicateKey);

        assertFalse(Verifiers.hasNoDuplicates(duplicatedItems));
    }

    @Test
    public void testHasNoDuplicatesWithNoDuplicates() {
        Collection<Item> uniqueItems = new ArrayList<>();

        Helpers.createNumberOfUniqueItems(null, 4, uniqueItems);
        for (Priority priority : Priority.values()) {
            Helpers.createNumberOfUniqueItems(priority, 4, uniqueItems);
        }

        assertTrue(Verifiers.hasNoDuplicates(uniqueItems));
    }
}
