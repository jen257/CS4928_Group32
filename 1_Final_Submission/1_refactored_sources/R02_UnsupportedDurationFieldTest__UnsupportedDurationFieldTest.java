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
    public void testUnsupportedDurationField() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("Expected getType() to return millis DurationFieldType", DurationFieldType.millis(), field.getType());

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("Expected getName() to return 'millis'", "millis", field.getName());

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertFalse("Expected isSupported() to return false", field.isSupported());

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue("Expected isPrecise() to return true", field.isPrecise());

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValue(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getValue(long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValueAsLong(1000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getValueAsLong(long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValue(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getValue(long, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getValueAsLong(1000, 2000);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getValueAsLong(long, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getMillis(int)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getMillis(long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getMillis(int, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getMillis(1000L, 2000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getMillis(long, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(1000L, 1);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for add(long, int)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.add(1000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for add(long, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getDifference(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getDifference(long, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        try {
            field.getDifferenceAsLong(2000L, 1000L);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expected unsupported message for getDifferenceAsLong(long, long)", "millis field is unsupported", e.getMessage());
        }

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("Expected getUnitMillis() to return 0", 0, field.getUnitMillis());

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("Expected compareTo() with itself to return 0", 0, field.compareTo(field));

        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertTrue("Expected field to equal itself", field1.equals(field1));
        assertTrue("Expected two instances of the same type to be equal", field1.equals(field2));
        assertFalse("Expected field not to equal null", field1.equals(null));
        assertFalse("Expected field not to equal an arbitrary Object", field1.equals(new Object()));

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("Expected hashCode() to match field name's hashCode()", field.getName().hashCode(), field.hashCode());

        field = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        assertEquals("Expected toString() to return 'UnsupportedDurationField[millis]'", "UnsupportedDurationField[millis]", field.toString());
    }
}
