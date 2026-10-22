package org.apache.commons.csv;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class CSVRecordTest {

    // Test case for get(int i) and get(String name)
    @Test
    public void testGet() {
        String[] values = {"1", "2", "3"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertEquals("Expected value at index 0 to be '1'", "1", record.get(0));
        assertEquals("Expected value at index 1 to be '2'", "2", record.get(1));
        assertEquals("Expected value at index 2 to be '3'", "3", record.get(2));

        String[] valuesNamed = {"John", "Doe"};
        Map<String, Integer> mappingNamed = new HashMap<>();
        mappingNamed.put("firstName", 0);
        mappingNamed.put("lastName", 1);
        CSVRecord recordNamed = new CSVRecord(valuesNamed, mappingNamed, null, 1);

        assertEquals("Expected value for key 'firstName' to be 'John'", "John", recordNamed.get("firstName"));
        assertEquals("Expected value for key 'lastName' to be 'Doe'", "Doe", recordNamed.get("lastName"));
    }

    // Test case for get(String name) without header mapping provided
    @Test(expected = IllegalStateException.class)
    public void testGetByNameWithoutMapping() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        record.get("firstName");
    }

    // Test case for isConsistent()
    @Test
    public void testIsConsistent() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue("Expected record with consistent mapping to be consistent", record.isConsistent());

        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);

        assertTrue("Expected record without mapping to be consistent", recordWithoutMapping.isConsistent());
    }

    // Test case for isMapped(String name)
    @Test
    public void testIsMapped() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue("Expected 'firstName' to be mapped", record.isMapped("firstName"));
        assertTrue("Expected 'lastName' to be mapped", record.isMapped("lastName"));

        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);

        assertFalse("Expected 'firstName' not to be mapped when mapping is null", recordWithoutMapping.isMapped("firstName"));
        assertFalse("Expected 'lastName' not to be mapped when mapping is null", recordWithoutMapping.isMapped("lastName"));
    }

    // Test case for isSet(String name)
    @Test
    public void testIsSet() {
        String[] values = {"John", "Doe"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("firstName", 0);
        mapping.put("lastName", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        assertTrue("Expected 'firstName' to be set", record.isSet("firstName"));
        assertTrue("Expected 'lastName' to be set", record.isSet("lastName"));

        String[] valuesPartial = {"John"};
        CSVRecord recordPartial = new CSVRecord(valuesPartial, mapping, null, 1);

        assertTrue("Expected 'firstName' to be set in partial record", recordPartial.isSet("firstName"));
        assertFalse("Expected 'lastName' not to be set when value is missing", recordPartial.isSet("lastName"));

        CSVRecord recordWithoutMapping = new CSVRecord(values, null, null, 1);

        assertFalse("Expected 'firstName' not to be set when mapping is null", recordWithoutMapping.isSet("firstName"));
        assertFalse("Expected 'lastName' not to be set when mapping is null", recordWithoutMapping.isSet("lastName"));
    }

    // Test case for iterator()
    @Test
    public void testIterator() {
        String[] values = {"1", "2", "3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        Iterator<String> it = record.iterator();
        assertTrue("Expected iterator to have next element before first item", it.hasNext());
        assertEquals("Expected first iterated value to be '1'", "1", it.next());
        assertTrue("Expected iterator to have next element before second item", it.hasNext());
        assertEquals("Expected second iterated value to be '2'", "2", it.next());
        assertTrue("Expected iterator to have next element before third item", it.hasNext());
        assertEquals("Expected third iterated value to be '3'", "3", it.next());
        assertFalse("Expected iterator not to have further elements", it.hasNext());
    }

    // Test case for values()
    @Test
    public void testValues() {
        String[] values = {"1", "2", "3"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertArrayEquals("Expected values array to match original array", new String[]{"1", "2", "3"}, record.values());
    }

    // Test case for getComment()
    @Test
    public void testGetComment() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, "This is a comment.", 1);

        assertEquals("Expected comment to match expected string", "This is a comment.", record.getComment());
    }

    // Test case for getRecordNumber()
    @Test
    public void testGetRecordNumber() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 10);

        assertEquals("Expected record number to be 10", 10, record.getRecordNumber());
    }

    // Test case for size()
    @Test
    public void testSize() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("Expected record size to be 2", 2, record.size());
    }

    // Test case for toString()
    @Test
    public void testToString() {
        String[] values = {"John", "Doe"};
        CSVRecord record = new CSVRecord(values, null, null, 1);

        assertEquals("Expected toString representation to match expected string", "[John, Doe]", record.toString());
    }

}
