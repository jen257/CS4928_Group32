# Master Refactoring Prompt (RQ2)

Paste this prompt to the AI, with `R09`, `8`, `18`, `133`,
`package org.apache.commons.csv;
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

}` and `/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * A CSV record parsed from a CSV file.
 *
 * @version $Id$
 */
public class CSVRecord implements Serializable, Iterable<String> {

    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The values of the record */
    private final String[] values;

    /** The column name to index mapping. */
    private final Map<String, Integer> mapping;

    /** The accumulated comments (if any) */
    private final String comment;

    /** The record number. */
    private final long recordNumber;

    CSVRecord(final String[] values, final Map<String, Integer> mapping,
            final String comment, final long recordNumber) {
        this.recordNumber = recordNumber;
        this.values = values != null ? values : EMPTY_STRING_ARRAY;
        this.mapping = mapping;
        this.comment = comment;
    }

    /**
     * Returns a value by index.
     *
     * @param i
     *            a column index (0-based)
     * @return the String at the given index
     */
    public String get(final int i) {
        return values[i];
    }

    /**
     * Returns a value by name.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return the column value, or {@code null} if the column name is not found
     * @throws IllegalStateException
     *             if no header mapping was provided
     * @throws IllegalArgumentException
     *             if the record is inconsistent
     * @see #isConsistent()
     */
    public String get(final String name) {
        if (mapping == null) {
            throw new IllegalStateException(
                    "No header mapping was specified, the record values can't be accessed by name");
        }
        final Integer index = mapping.get(name);
            return index != null ? values[index.intValue()] : null;
    }

    /**
     * Returns true if this record is consistent, false if not. Currently, the only check is matching the record size to
     * the header size. Some programs can export files that fails this test but still produce parsable files.
     *
     * @return true of this record is valid, false if not
     * @see CSVParserTest#org.apache.commons.csv.CSVParserTest.testMappedButNotSetAsOutlook2007ContactExport()
     */
    public boolean isConsistent() {
        return mapping == null ? true : mapping.size() == values.length;
    }

    /**
     * Checks whether a given column is mapped.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped.
     */
    public boolean isMapped(final String name) {
        return mapping != null ? mapping.containsKey(name) : false;
    }

    /**
     * Checks whether a given columns is mapped and has a value.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped.
     */
    public boolean isSet(final String name) {
        return isMapped(name) && mapping.get(name).intValue() < values.length;
    }

    /**
     * Returns an iterator over the values of this record.
     *
     * @return an iterator over the values of this record.
     */
    public Iterator<String> iterator() {
        return Arrays.asList(values).iterator();
    }

    String[] values() {
        return values;
    }

    /**
     * Returns the comment for this record, if any.
     *
     * @return the comment for this record, or null if no comment for this
     *         record is available.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the number of this record in the parsed CSV file.
     *
     * @return the number of this record.
     */
    public long getRecordNumber() {
        return recordNumber;
    }

    /**
     * Returns the number of values in this record.
     *
     * @return the number of values.
     */
    public int size() {
        return values.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

}` replaced by the sample's actual
values. Keep every line; the "Detector rules" and "Hard constraints" sections
are what stop the model from going in the wrong direction.

---

You are an expert Java test-refactoring assistant. Refactor the JUnit test
class below to eliminate two test smells, Assertion Roulette (AR) and Lazy
Test (LT), as counted by the TsDetect tool. Preserve all original test
behaviour exactly, and produce code that compiles and passes.

## Detector rules (measured, not textbook)

These are the actual counting rules of TsDetect. Follow them literally.

**Assertion Roulette (AR):** a test method containing 2 or more assertion
statements AND at least one assertion without a message parameter is counted
once. Adding a message to EVERY assertion in a method removes that method from
the count. Do not split methods to fix AR.

**Lazy Test (LT):** for each production method M, if M is invoked from 2 or
more DIFFERENT test methods, TsDetect adds M's TOTAL invocation count (calls
inside the same method count multiple times) to the LT score. If M is invoked
from only one test method it contributes 0.

Consequences you MUST follow:

1. Splitting a test method into several smaller methods INCREASES LT, because
   more test methods then share the same production method. Do not split to fix LT.
2. To reduce LT, MERGE test methods that observe the same production method
   (typically toString(), peek(), get(), size(), etc.) so each production method
   is invoked from exactly one test method.

## Procedure

1. List each production method and count how many test methods call it.
   Identify methods called from 2+ test methods - these are the LT source.
2. For AR: add a clear, descriptive failure message to EVERY assertion.
3. For LT: group and merge test methods by the production method they observe.
   After merging, each production method must be invoked from a single test method.
4. A merged method may contain many assertions; that is acceptable, but every
   assertion must carry a message, otherwise that method trips AR.

## Hard constraints (any violation is a failure)

- Do not delete or change ANY assertion: predicate, expected value, comparison,
  or input literal. The total assertion count after refactoring must equal the original.
- Do not delete or change any test input, constructor argument, or string/char literal.
- Do not change expected-exception behaviour or the setup/act/assert order.
- Do not merge two different scenarios' expectations into one assertion.
- Do not move assertions into helper or private methods to hide them from the detector.
- Keep JUnit 4 @Test annotations and imports; no parameterized tests, no new
  dependencies; keep the package and the class name unchanged.
- Do not modify the production class.

## Output format

1. A table: production method -> test methods calling it before -> after.
2. A list of each new test method and which original methods it absorbed.
3. The COMPLETE refactored Java file, from `package` to the final `}`, with no
   `// ...` or truncation.
4. State the assertion count before and after; they must be equal.

If some LT source cannot be merged without changing semantics, say which and
why. If the semantics are ambiguous, say so; do not invent expected values.

## Files

Sample R09 - baseline AR=8, LT=18, LOC=133.

Test class:

```java
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
```

Production class:

```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * A CSV record parsed from a CSV file.
 *
 * @version $Id$
 */
public class CSVRecord implements Serializable, Iterable<String> {

    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The values of the record */
    private final String[] values;

    /** The column name to index mapping. */
    private final Map<String, Integer> mapping;

    /** The accumulated comments (if any) */
    private final String comment;

    /** The record number. */
    private final long recordNumber;

    CSVRecord(final String[] values, final Map<String, Integer> mapping,
            final String comment, final long recordNumber) {
        this.recordNumber = recordNumber;
        this.values = values != null ? values : EMPTY_STRING_ARRAY;
        this.mapping = mapping;
        this.comment = comment;
    }

    /**
     * Returns a value by index.
     *
     * @param i
     *            a column index (0-based)
     * @return the String at the given index
     */
    public String get(final int i) {
        return values[i];
    }

    /**
     * Returns a value by name.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return the column value, or {@code null} if the column name is not found
     * @throws IllegalStateException
     *             if no header mapping was provided
     * @throws IllegalArgumentException
     *             if the record is inconsistent
     * @see #isConsistent()
     */
    public String get(final String name) {
        if (mapping == null) {
            throw new IllegalStateException(
                    "No header mapping was specified, the record values can't be accessed by name");
        }
        final Integer index = mapping.get(name);
            return index != null ? values[index.intValue()] : null;
    }

    /**
     * Returns true if this record is consistent, false if not. Currently, the only check is matching the record size to
     * the header size. Some programs can export files that fails this test but still produce parsable files.
     *
     * @return true of this record is valid, false if not
     * @see CSVParserTest#org.apache.commons.csv.CSVParserTest.testMappedButNotSetAsOutlook2007ContactExport()
     */
    public boolean isConsistent() {
        return mapping == null ? true : mapping.size() == values.length;
    }

    /**
     * Checks whether a given column is mapped.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped.
     */
    public boolean isMapped(final String name) {
        return mapping != null ? mapping.containsKey(name) : false;
    }

    /**
     * Checks whether a given columns is mapped and has a value.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped.
     */
    public boolean isSet(final String name) {
        return isMapped(name) && mapping.get(name).intValue() < values.length;
    }

    /**
     * Returns an iterator over the values of this record.
     *
     * @return an iterator over the values of this record.
     */
    public Iterator<String> iterator() {
        return Arrays.asList(values).iterator();
    }

    String[] values() {
        return values;
    }

    /**
     * Returns the comment for this record, if any.
     *
     * @return the comment for this record, or null if no comment for this
     *         record is available.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the number of this record in the parsed CSV file.
     *
     * @return the number of this record.
     */
    public long getRecordNumber() {
        return recordNumber;
    }

    /**
     * Returns the number of values in this record.
     *
     * @return the number of values.
     */
    public int size() {
        return values.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

}
```

