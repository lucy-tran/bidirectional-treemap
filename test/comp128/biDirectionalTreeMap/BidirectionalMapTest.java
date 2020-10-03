package comp128.biDirectionalTreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BidirectionalMapTest {

    private List<String> keys;
    private List<Integer> values;
    private List<AbstractMap.SimpleImmutableEntry<String, Integer>> entries;
    private BidirectionalTreeMap<String, Integer> map;

    @BeforeEach
    public void setup() {
        keys = new ArrayList<>(6);
        keys.add("carrot");
        keys.add("banana");
        keys.add("date");
        keys.add("fig");
        keys.add("eggplant");
        keys.add("apple");

        values = new ArrayList<>(6);
        values.add(4);
        values.add(5);
        values.add(6);
        values.add(2);
        values.add(1);
        values.add(3);

        map = new BidirectionalTreeMap<>();
        entries = new ArrayList<>(6);
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
            entries.add(new AbstractMap.SimpleImmutableEntry<>(keys.get(i), values.get(i)));
        }
    }

    @Test
    public void testGetValue() {
        for (int i = 0; i < keys.size(); i++) {
            assertEquals(values.get(i), map.getValue(keys.get(i)));
        }

        assertNull(map.getValue("orange"));
    }

    @Test
    public void testGetKeys() {
        for (int i = 0; i < values.size(); i++) {
            assertEquals(keys.get(i), map.getKey(values.get(i)));
        }

        assertNull(map.getKey(28));
    }

    @Test
    public void testContains() {
        assertTrue(map.containsKey("carrot"));
        assertFalse(map.containsKey("olive"));
        assertTrue(map.containsValue(2));
        assertFalse(map.containsValue(10));
    }

    @Test
    public void testKeyOrdering() {
        Comparator<AbstractMap.SimpleImmutableEntry<String, Integer>> byKey =
                (AbstractMap.SimpleImmutableEntry<String, Integer> o1, AbstractMap.SimpleImmutableEntry<String, Integer> o2) -> o1.getKey().compareTo(o2.getKey());
        Collections.sort(entries, byKey);

        StringBuilder expectedOutput = new StringBuilder();
        for (AbstractMap.SimpleImmutableEntry<String, Integer> entry : entries) {
            expectedOutput.append("(");
            expectedOutput.append(entry.getKey());
            expectedOutput.append(", ");
            expectedOutput.append(entry.getValue());
            expectedOutput.append("), ");
        }
        expectedOutput.delete(expectedOutput.length() - 2, expectedOutput.length()); // trim last ", "

        assertEquals(expectedOutput.toString(), map.inOrderTraverseByKeys());
    }

    @Test
    public void testValueOrdering() {
        Comparator<AbstractMap.SimpleImmutableEntry<String, Integer>> byValue = Comparator.comparing(AbstractMap.SimpleImmutableEntry::getValue);
        Collections.sort(entries, byValue);

        StringBuilder expectedOutput = new StringBuilder();
        for (AbstractMap.SimpleImmutableEntry<String, Integer> entry : entries) {
            expectedOutput.append("(");
            expectedOutput.append(entry.getKey());
            expectedOutput.append(", ");
            expectedOutput.append(entry.getValue());
            expectedOutput.append("), ");
        }
        expectedOutput.delete(expectedOutput.length() - 2, expectedOutput.length()); // trim last ", "

        assertEquals(expectedOutput.toString(), map.inOrderTraverseByValues());
    }

    @Test
    public void testReturnValueOfPut() {
        assertTrue(map.put("orange", 7)); // can add an element where the key and value don't exist in the map already
        assertFalse(map.put("carrot", 10)); //keys can't already exist
        assertFalse(map.put("olive", 2)); // values also can't already exist
    }

}
