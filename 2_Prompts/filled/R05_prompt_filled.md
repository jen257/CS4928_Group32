# Master Refactoring Prompt (RQ2)

Paste this prompt to the AI, with `R05`, `10`, `19`, `71`,
`package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    
    @Test
    public void testAreEqualWhenBothObjectsAreNull() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsNull() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreArrays() {
        assertTrue(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsArray() {
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new Object()));
        assertFalse(Equality.areEqual(new Object(), new int[] {1, 2, 3}));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreEqual() {
        assertTrue(Equality.areEqual("foo", "foo"));
        assertTrue(Equality.areEqual(5, 5));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreNotEqual() {
        assertFalse(Equality.areEqual("foo", "bar"));
        assertFalse(Equality.areEqual(5, 10));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsEqualAndOneObjectIsNull() {
        assertFalse(Equality.areEqual("foo", null));
        assertFalse(Equality.areEqual(null, "foo"));
        assertFalse(Equality.areEqual(5, null));
        assertFalse(Equality.areEqual(null, 5));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsEqualAndOneObjectIsArray() {
        assertFalse(Equality.areEqual("foo", new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, "foo"));
    }
    
    @Test
    public void testAreArraysEqualWhenArrayLengthsAreEqualAndArrayElementsAreEqual() {
        assertTrue(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreArrayLengthsEqualWhenArrayLengthsAreEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3, 4}));
    }
    
    @Test
    public void testAreArrayElementsEqualWhenArrayElementsAreEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testIsArrayWhenObjectIsArray() {
        assertTrue(Equality.isArray(new int[] {1, 2, 3}));
    }
    
    @Test
    public void testIsArrayWhenObjectIsNotArray() {
        assertFalse(Equality.isArray("foo"));
    }
    
}` and `/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.lang.reflect.Array;

//stolen from hamcrest because I didn't want to have more dependency than Matcher class 
public class Equality {

    public static boolean areEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return o1 == null && o2 == null;
        } else if (isArray(o1)) {
            return isArray(o2) && areArraysEqual(o1, o2);
        } else {
            return o1.equals(o2);
        }
    }

    static boolean areArraysEqual(Object o1, Object o2) {
        return areArrayLengthsEqual(o1, o2)
                && areArrayElementsEqual(o1, o2);
    }

    static boolean areArrayLengthsEqual(Object o1, Object o2) {
        return Array.getLength(o1) == Array.getLength(o2);
    }

    static boolean areArrayElementsEqual(Object o1, Object o2) {
        for (int i = 0; i < Array.getLength(o1); i++) {
            if (!areEqual(Array.get(o1, i), Array.get(o2, i))) return false;
        }
        return true;
    }

    static boolean isArray(Object o) {
        return o.getClass().isArray();
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

Sample R05 - baseline AR=10, LT=19, LOC=71.

Test class:

```java
package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {
    
    @Test
    public void testAreEqualWhenBothObjectsAreNull() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsNull() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreArrays() {
        assertTrue(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsArray() {
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, new Object()));
        assertFalse(Equality.areEqual(new Object(), new int[] {1, 2, 3}));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreEqual() {
        assertTrue(Equality.areEqual("foo", "foo"));
        assertTrue(Equality.areEqual(5, 5));
    }
    
    @Test
    public void testAreEqualWhenBothObjectsAreNotEqual() {
        assertFalse(Equality.areEqual("foo", "bar"));
        assertFalse(Equality.areEqual(5, 10));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsEqualAndOneObjectIsNull() {
        assertFalse(Equality.areEqual("foo", null));
        assertFalse(Equality.areEqual(null, "foo"));
        assertFalse(Equality.areEqual(5, null));
        assertFalse(Equality.areEqual(null, 5));
    }
    
    @Test
    public void testAreEqualWhenOneObjectIsEqualAndOneObjectIsArray() {
        assertFalse(Equality.areEqual("foo", new int[] {1, 2, 3}));
        assertFalse(Equality.areEqual(new int[] {1, 2, 3}, "foo"));
    }
    
    @Test
    public void testAreArraysEqualWhenArrayLengthsAreEqualAndArrayElementsAreEqual() {
        assertTrue(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testAreArrayLengthsEqualWhenArrayLengthsAreEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3, 4}));
    }
    
    @Test
    public void testAreArrayElementsEqualWhenArrayElementsAreEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[] {1, 2, 3}, new int[] {1, 2, 4}));
    }
    
    @Test
    public void testIsArrayWhenObjectIsArray() {
        assertTrue(Equality.isArray(new int[] {1, 2, 3}));
    }
    
    @Test
    public void testIsArrayWhenObjectIsNotArray() {
        assertFalse(Equality.isArray("foo"));
    }
    
}
```

Production class:

```java
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.lang.reflect.Array;

//stolen from hamcrest because I didn't want to have more dependency than Matcher class 
public class Equality {

    public static boolean areEqual(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return o1 == null && o2 == null;
        } else if (isArray(o1)) {
            return isArray(o2) && areArraysEqual(o1, o2);
        } else {
            return o1.equals(o2);
        }
    }

    static boolean areArraysEqual(Object o1, Object o2) {
        return areArrayLengthsEqual(o1, o2)
                && areArrayElementsEqual(o1, o2);
    }

    static boolean areArrayLengthsEqual(Object o1, Object o2) {
        return Array.getLength(o1) == Array.getLength(o2);
    }

    static boolean areArrayElementsEqual(Object o1, Object o2) {
        for (int i = 0; i < Array.getLength(o1); i++) {
            if (!areEqual(Array.get(o1, i), Array.get(o2, i))) return false;
        }
        return true;
    }

    static boolean isArray(Object o) {
        return o.getClass().isArray();
    }
}
```

