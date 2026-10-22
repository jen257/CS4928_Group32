package org.apache.commons.csv;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test case for get(int i)
    @Test
    public void testGetByIndex() {
        String[] values = {"1", "2", "3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        assertEquals("3", record.get(2));
    }

    // Test case for get(String name) with header mapping provided
    @Test
    public void testGetByNameWithMapping() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("John", record.get("firstName"));
        assertEquals("Doe", record.get("lastName"));
    }

    // Test case for get(String name) without header mapping provided
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("firstName");
    }

    // Test case for isConsistent() with header mapping provided
    @Test
    public void testIsConsistentWithMapping() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isConsistent());
    }

    // Test case for isConsistent() without header mapping provided
    @Test
    public void testIsConsistentWithoutMapping() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertTrue(record.isConsistent());
    }

    // Test case for isMapped(String name) with header mapping provided
    @Test
    public void testIsMappedWithMapping() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isMapped("firstName"));
        assertTrue(record.isMapped("lastName"));
    }

    // Test case for isMapped(String name) without header mapping provided
    @Test
    public void testIsMappedWithoutMapping() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertFalse(record.isMapped("firstName"));
        assertFalse(record.isMapped("lastName"));
    }

    // Test case for isSet(String name) with header mapping provided and value present
    @Test
    public void testIsSetWithMappingAndValuePresent() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("firstName"));
        assertTrue(record.isSet("lastName"));
    }

    // Test case for isSet(String name) with header mapping provided and value not present
    @Test
    public void testIsSetWithMappingAndValueNotPresent() {
        String[] values = {"John"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue(record.isSet("firstName"));
        assertFalse(record.isSet("lastName"));
    }

    // Test case for isSet(String name) without header mapping provided
    @Test
    public void testIsSetWithoutMapping() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertFalse(record.isSet("firstName"));
        assertFalse(record.isSet("lastName"));
    }

    // Test case for iterator()
    @Test
    public void testIterator() {
        String[] values = {"1", "2", "3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Iterator<String> it = record.iterator();
        assertTrue(it.hasNext());
        assertEquals("1", it.next());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        assertTrue(it.hasNext());
        assertEquals("3", it.next());
        assertFalse(it.hasNext());
    }

    // Test case for values()
    @Test
    public void testValues() {
        String[] values = {"1", "2", "3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertArrayEquals(new String[]{"1", "2", "3"}, record.values());
    }

    // Test case for getComment()
    @Test
    public void testGetComment() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment.", 1);

        assertEquals("This is a comment.", record.getComment());
    }

    // Test case for getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 10);

        assertEquals(10, record.getRecordNumber());
    }

    // Test case for size()
    @Test
    public void testSize() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals(2, record.size());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("[John, Doe]", record.toString());
    }

}