package org.joda.time.field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(DurationFieldType.millis(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("millis", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValue(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueAsLong_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValueAsLong(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValue_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValue(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetValueAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValueAsLong(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillis_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillis_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillis_int_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetMillis_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1000L, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testAdd_long_int() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(1000L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testAdd_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(1000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetDifference_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getDifference(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetDifferenceAsLong_long_long() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getDifferenceAsLong(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("millis field is unsupported", e.getMessage());
        }
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(0, field.compareTo(field));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals(field.getName().hashCode(), field.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("UnsupportedDurationField[millis]", field.toString());
    }
}