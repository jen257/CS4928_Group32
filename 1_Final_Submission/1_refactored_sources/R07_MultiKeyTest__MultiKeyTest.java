package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

    @Test
    public void testMultiKey() {
        MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected first key at index 0 to be 'key1'", "key1", multiKey.getKey(0));
        assertEquals("Expected second key at index 1 to be 'key2'", "key2", multiKey.getKey(1));
        assertEquals("Expected size of multiKey to be 2", 2, multiKey.size());
    
        multiKey = new MultiKey<>("key1", "key2", "key3");
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected first key at index 0 to be 'key1'", "key1", multiKey.getKey(0));
        assertEquals("Expected second key at index 1 to be 'key2'", "key2", multiKey.getKey(1));
        assertEquals("Expected third key at index 2 to be 'key3'", "key3", multiKey.getKey(2));
        assertEquals("Expected size of multiKey to be 3", 3, multiKey.size());
    
        multiKey = new MultiKey<>("key1", "key2", "key3", "key4");
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected first key at index 0 to be 'key1'", "key1", multiKey.getKey(0));
        assertEquals("Expected second key at index 1 to be 'key2'", "key2", multiKey.getKey(1));
        assertEquals("Expected third key at index 2 to be 'key3'", "key3", multiKey.getKey(2));
        assertEquals("Expected fourth key at index 3 to be 'key4'", "key4", multiKey.getKey(3));
        assertEquals("Expected size of multiKey to be 4", 4, multiKey.size());
    
        multiKey = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected first key at index 0 to be 'key1'", "key1", multiKey.getKey(0));
        assertEquals("Expected second key at index 1 to be 'key2'", "key2", multiKey.getKey(1));
        assertEquals("Expected third key at index 2 to be 'key3'", "key3", multiKey.getKey(2));
        assertEquals("Expected fourth key at index 3 to be 'key4'", "key4", multiKey.getKey(3));
        assertEquals("Expected fifth key at index 4 to be 'key5'", "key5", multiKey.getKey(4));
        assertEquals("Expected size of multiKey to be 5", 5, multiKey.size());
    
        String[] keys = {"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected first key at index 0 to be 'key1'", "key1", multiKey.getKey(0));
        assertEquals("Expected second key at index 1 to be 'key2'", "key2", multiKey.getKey(1));
        assertEquals("Expected third key at index 2 to be 'key3'", "key3", multiKey.getKey(2));
        assertEquals("Expected size of multiKey to be 3", 3, multiKey.size());
    
        try {
            new MultiKey<>(null);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    
        keys = new String[]{"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        String[] retrievedKeys = multiKey.getKeys();
        assertArrayEquals("Expected retrieved keys array to match original array", keys, retrievedKeys);
    
        keys = new String[]{"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected first key at index 0 to be 'key1'", "key1", multiKey.getKey(0));
        assertEquals("Expected second key at index 1 to be 'key2'", "key2", multiKey.getKey(1));
        assertEquals("Expected third key at index 2 to be 'key3'", "key3", multiKey.getKey(2));
    
        try {
            keys = new String[]{"key1", "key2", "key3"};
            multiKey = new MultiKey<>(keys);
            multiKey.getKey(3);
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    
        keys = new String[]{"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected size of multiKey to be 3", 3, multiKey.size());
    
        keys = new String[]{"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        assertEquals("Expected multiKey to be equal to itself", multiKey, multiKey);
    
        String[] keys1 = {"key1", "key2", "key3"};
        String[] keys2 = {"key1", "key2", "key3"};
        MultiKey<String> multiKey1 = new MultiKey<>(keys1);
        MultiKey<String> multiKey2 = new MultiKey<>(keys2);
        assertEquals("Expected multiKeys with identical keys to be equal", multiKey1, multiKey2);
    
        keys1 = new String[]{"key1", "key2", "key3"};
        keys2 = new String[]{"key1", "key2"};
        multiKey1 = new MultiKey<>(keys1);
        multiKey2 = new MultiKey<>(keys2);
        assertNotEquals("Expected multiKeys with different sizes not to be equal", multiKey1, multiKey2);
    
        keys1 = new String[]{"key1", "key2", "key3"};
        keys2 = new String[]{"key1", "key2", "key4"};
        multiKey1 = new MultiKey<>(keys1);
        multiKey2 = new MultiKey<>(keys2);
        assertNotEquals("Expected multiKeys with different keys not to be equal", multiKey1, multiKey2);
    
        keys = new String[]{"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        int hashCode = multiKey.hashCode();
        assertEquals("Expected hashCode to remain consistent across calls", hashCode, multiKey.hashCode());
    
        keys = new String[]{"key1", "key2", "key3"};
        multiKey = new MultiKey<>(keys);
        assertNotNull("Expected multiKey instance not to be null", multiKey);
        assertEquals("Expected toString representation to match expected format", "MultiKey[key1, key2, key3]", multiKey.toString());
    }
}
