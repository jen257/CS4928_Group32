# Master Refactoring Prompt (RQ2)

Paste this prompt to the AI, with `R08`, `9`, `9`, `133`,
`package org.apache.commons.compress.archivers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null, inputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(null, outputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream("unknown", inputStream);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected ArchiveException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream("unknown", outputStream);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected ArchiveException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        byte[] data = new byte[] {80, 75, 3, 4}; // Zip archive signature
        InputStream inputStream = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException e) {
            assertNull("Expected no exception", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectNoArchiveFound() {
        byte[] data = new byte[] {0, 0, 0, 0}; // Invalid signature
        InputStream inputStream = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected ArchiveException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithUnsupportedMark() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
            
            @Override
            public boolean markSupported() {
                return false;
            }
        };
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(inputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }
  
}` and `/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.archivers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * <p>Factory to create Archive[In|Out]putStreams from names or the first bytes of
 * the InputStream. In order add other implementations you should extend
 * ArchiveStreamFactory and override the appropriate methods (and call their
 * implementation from super of course).</p>
 * 
 * Compressing a ZIP-File:
 * 
 * <pre>
 * final OutputStream out = new FileOutputStream(output); 
 * ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
 * 
 * os.putArchiveEntry(new ZipArchiveEntry("testdata/test1.xml"));
 * IOUtils.copy(new FileInputStream(file1), os);
 * os.closeArchiveEntry();
 *
 * os.putArchiveEntry(new ZipArchiveEntry("testdata/test2.xml"));
 * IOUtils.copy(new FileInputStream(file2), os);
 * os.closeArchiveEntry();
 * os.close();
 * </pre>
 * 
 * Decompressing a ZIP-File:
 * 
 * <pre>
 * final InputStream is = new FileInputStream(input); 
 * ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
 * ZipArchiveEntry entry = (ZipArchiveEntry)in.getNextEntry();
 * OutputStream out = new FileOutputStream(new File(dir, entry.getName()));
 * IOUtils.copy(in, out);
 * out.close();
 * in.close();
 * </pre>
 * 
 * @Immutable
 */
public class ArchiveStreamFactory {

    /**
     * Constant used to identify the AR archive format.
     * @since Commons Compress 1.1
     */
    public static final String AR = "ar";
    /**
     * Constant used to identify the CPIO archive format.
     * @since Commons Compress 1.1
     */
    public static final String CPIO = "cpio";
    /**
     * Constant used to identify the Unix DUMP archive format.
     * @since Commons Compress 1.3
     */
    public static final String DUMP = "dump";
    /**
     * Constant used to identify the JAR archive format.
     * @since Commons Compress 1.1
     */
    public static final String JAR = "jar";
    /**
     * Constant used to identify the TAR archive format.
     * @since Commons Compress 1.1
     */
    public static final String TAR = "tar";
    /**
     * Constant used to identify the ZIP archive format.
     * @since Commons Compress 1.1
     */
    public static final String ZIP = "zip";

    /**
     * Create an archive input stream from an archiver name and an input stream.
     * 
     * @param archiverName the archive name, i.e. "ar", "zip", "tar", "jar", "dump" or "cpio"
     * @param in the input stream
     * @return the archive input stream
     * @throws ArchiveException if the archiver name is not known
     * @throws IllegalArgumentException if the archiver name or stream is null
     */
    public ArchiveInputStream createArchiveInputStream(
            final String archiverName, final InputStream in)
            throws ArchiveException {
        
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        
        if (in == null) {
            throw new IllegalArgumentException("InputStream must not be null.");
        }

        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveInputStream(in);
        }
        if (ZIP.equalsIgnoreCase(archiverName)) {
            return new ZipArchiveInputStream(in);
        }
        if (TAR.equalsIgnoreCase(archiverName)) {
            return new TarArchiveInputStream(in);
        }
        if (JAR.equalsIgnoreCase(archiverName)) {
            return new JarArchiveInputStream(in);
        }
        if (CPIO.equalsIgnoreCase(archiverName)) {
            return new CpioArchiveInputStream(in);
        }
        if (DUMP.equalsIgnoreCase(archiverName)) {
            return new DumpArchiveInputStream(in);
        }
        
        throw new ArchiveException("Archiver: " + archiverName + " not found.");
    }

    /**
     * Create an archive output stream from an archiver name and an input stream.
     * 
     * @param archiverName the archive name, i.e. "ar", "zip", "tar", "jar" or "cpio"
     * @param out the output stream
     * @return the archive output stream
     * @throws ArchiveException if the archiver name is not known
     * @throws IllegalArgumentException if the archiver name or stream is null
     */
    public ArchiveOutputStream createArchiveOutputStream(
            final String archiverName, final OutputStream out)
            throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must not be null.");
        }

        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveOutputStream(out);
        }
        if (ZIP.equalsIgnoreCase(archiverName)) {
            return new ZipArchiveOutputStream(out);
        }
        if (TAR.equalsIgnoreCase(archiverName)) {
            return new TarArchiveOutputStream(out);
        }
        if (JAR.equalsIgnoreCase(archiverName)) {
            return new JarArchiveOutputStream(out);
        }
        if (CPIO.equalsIgnoreCase(archiverName)) {
            return new CpioArchiveOutputStream(out);
        }
        throw new ArchiveException("Archiver: " + archiverName + " not found.");
    }

    /**
     * Create an archive input stream from an input stream, autodetecting
     * the archive type from the first few bytes of the stream. The InputStream
     * must support marks, like BufferedInputStream.
     * 
     * @param in the input stream
     * @return the archive input stream
     * @throws ArchiveException if the archiver name is not known
     * @throws IllegalArgumentException if the stream is null or does not support mark
     */
    public ArchiveInputStream createArchiveInputStream(final InputStream in)
            throws ArchiveException {
        if (in == null) {
            throw new IllegalArgumentException("Stream must not be null.");
        }

        if (!in.markSupported()) {
            throw new IllegalArgumentException("Mark is not supported.");
        }

        final byte[] signature = new byte[12];
        in.mark(signature.length);
        try {
            int signatureLength = in.read(signature);
            in.reset();
            if (ZipArchiveInputStream.matches(signature, signatureLength)) {
                return new ZipArchiveInputStream(in);
            } else if (JarArchiveInputStream.matches(signature, signatureLength)) {
                return new JarArchiveInputStream(in);
            } else if (ArArchiveInputStream.matches(signature, signatureLength)) {
                return new ArArchiveInputStream(in);
            } else if (CpioArchiveInputStream.matches(signature, signatureLength)) {
                return new CpioArchiveInputStream(in);
            }

            // Dump needs a bigger buffer to check the signature;
            final byte[] dumpsig = new byte[32];
            in.mark(dumpsig.length);
            signatureLength = in.read(dumpsig);
            in.reset();
            if (DumpArchiveInputStream.matches(dumpsig, signatureLength)) {
                return new DumpArchiveInputStream(in);
            }

            // Tar needs an even bigger buffer to check the signature; read the first block
            final byte[] tarheader = new byte[512];
            in.mark(tarheader.length);
            signatureLength = in.read(tarheader);
            in.reset();
            if (TarArchiveInputStream.matches(tarheader, signatureLength)) {
                return new TarArchiveInputStream(in);
            }
            // COMPRESS-117 - improve auto-recognition
            try {
                TarArchiveInputStream tais = new TarArchiveInputStream(new ByteArrayInputStream(tarheader));
                tais.getNextEntry();
                return new TarArchiveInputStream(in);
            } catch (Exception e) { // NOPMD
                // can generate IllegalArgumentException as well as IOException
                // autodetection, simply not a TAR
                // ignored
            }
        } catch (IOException e) {
            throw new ArchiveException("Could not use reset and mark operations.", e);
        }

        throw new ArchiveException("No Archiver found for the stream signature");
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

Sample R08 - baseline AR=9, LT=9, LOC=133.

Test class:

```java
package org.apache.commons.compress.archivers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null, inputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(null, outputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownArchiverName() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream("unknown", inputStream);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected ArchiveException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream("unknown", outputStream);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected ArchiveException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        byte[] data = new byte[] {80, 75, 3, 4}; // Zip archive signature
        InputStream inputStream = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertNotNull(archiveInputStream);
        } catch (ArchiveException e) {
            assertNull("Expected no exception", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectNoArchiveFound() {
        byte[] data = new byte[] {0, 0, 0, 0}; // Invalid signature
        InputStream inputStream = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected ArchiveException", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithUnsupportedMark() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
            
            @Override
            public boolean markSupported() {
                return false;
            }
        };
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(inputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        } catch (Exception e) {
            assertNull("Expected IllegalArgumentException", e.getMessage());
        }
    }
  
}
```

Production class:

```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.archivers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * <p>Factory to create Archive[In|Out]putStreams from names or the first bytes of
 * the InputStream. In order add other implementations you should extend
 * ArchiveStreamFactory and override the appropriate methods (and call their
 * implementation from super of course).</p>
 * 
 * Compressing a ZIP-File:
 * 
 * <pre>
 * final OutputStream out = new FileOutputStream(output); 
 * ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
 * 
 * os.putArchiveEntry(new ZipArchiveEntry("testdata/test1.xml"));
 * IOUtils.copy(new FileInputStream(file1), os);
 * os.closeArchiveEntry();
 *
 * os.putArchiveEntry(new ZipArchiveEntry("testdata/test2.xml"));
 * IOUtils.copy(new FileInputStream(file2), os);
 * os.closeArchiveEntry();
 * os.close();
 * </pre>
 * 
 * Decompressing a ZIP-File:
 * 
 * <pre>
 * final InputStream is = new FileInputStream(input); 
 * ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
 * ZipArchiveEntry entry = (ZipArchiveEntry)in.getNextEntry();
 * OutputStream out = new FileOutputStream(new File(dir, entry.getName()));
 * IOUtils.copy(in, out);
 * out.close();
 * in.close();
 * </pre>
 * 
 * @Immutable
 */
public class ArchiveStreamFactory {

    /**
     * Constant used to identify the AR archive format.
     * @since Commons Compress 1.1
     */
    public static final String AR = "ar";
    /**
     * Constant used to identify the CPIO archive format.
     * @since Commons Compress 1.1
     */
    public static final String CPIO = "cpio";
    /**
     * Constant used to identify the Unix DUMP archive format.
     * @since Commons Compress 1.3
     */
    public static final String DUMP = "dump";
    /**
     * Constant used to identify the JAR archive format.
     * @since Commons Compress 1.1
     */
    public static final String JAR = "jar";
    /**
     * Constant used to identify the TAR archive format.
     * @since Commons Compress 1.1
     */
    public static final String TAR = "tar";
    /**
     * Constant used to identify the ZIP archive format.
     * @since Commons Compress 1.1
     */
    public static final String ZIP = "zip";

    /**
     * Create an archive input stream from an archiver name and an input stream.
     * 
     * @param archiverName the archive name, i.e. "ar", "zip", "tar", "jar", "dump" or "cpio"
     * @param in the input stream
     * @return the archive input stream
     * @throws ArchiveException if the archiver name is not known
     * @throws IllegalArgumentException if the archiver name or stream is null
     */
    public ArchiveInputStream createArchiveInputStream(
            final String archiverName, final InputStream in)
            throws ArchiveException {
        
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        
        if (in == null) {
            throw new IllegalArgumentException("InputStream must not be null.");
        }

        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveInputStream(in);
        }
        if (ZIP.equalsIgnoreCase(archiverName)) {
            return new ZipArchiveInputStream(in);
        }
        if (TAR.equalsIgnoreCase(archiverName)) {
            return new TarArchiveInputStream(in);
        }
        if (JAR.equalsIgnoreCase(archiverName)) {
            return new JarArchiveInputStream(in);
        }
        if (CPIO.equalsIgnoreCase(archiverName)) {
            return new CpioArchiveInputStream(in);
        }
        if (DUMP.equalsIgnoreCase(archiverName)) {
            return new DumpArchiveInputStream(in);
        }
        
        throw new ArchiveException("Archiver: " + archiverName + " not found.");
    }

    /**
     * Create an archive output stream from an archiver name and an input stream.
     * 
     * @param archiverName the archive name, i.e. "ar", "zip", "tar", "jar" or "cpio"
     * @param out the output stream
     * @return the archive output stream
     * @throws ArchiveException if the archiver name is not known
     * @throws IllegalArgumentException if the archiver name or stream is null
     */
    public ArchiveOutputStream createArchiveOutputStream(
            final String archiverName, final OutputStream out)
            throws ArchiveException {
        if (archiverName == null) {
            throw new IllegalArgumentException("Archivername must not be null.");
        }
        if (out == null) {
            throw new IllegalArgumentException("OutputStream must not be null.");
        }

        if (AR.equalsIgnoreCase(archiverName)) {
            return new ArArchiveOutputStream(out);
        }
        if (ZIP.equalsIgnoreCase(archiverName)) {
            return new ZipArchiveOutputStream(out);
        }
        if (TAR.equalsIgnoreCase(archiverName)) {
            return new TarArchiveOutputStream(out);
        }
        if (JAR.equalsIgnoreCase(archiverName)) {
            return new JarArchiveOutputStream(out);
        }
        if (CPIO.equalsIgnoreCase(archiverName)) {
            return new CpioArchiveOutputStream(out);
        }
        throw new ArchiveException("Archiver: " + archiverName + " not found.");
    }

    /**
     * Create an archive input stream from an input stream, autodetecting
     * the archive type from the first few bytes of the stream. The InputStream
     * must support marks, like BufferedInputStream.
     * 
     * @param in the input stream
     * @return the archive input stream
     * @throws ArchiveException if the archiver name is not known
     * @throws IllegalArgumentException if the stream is null or does not support mark
     */
    public ArchiveInputStream createArchiveInputStream(final InputStream in)
            throws ArchiveException {
        if (in == null) {
            throw new IllegalArgumentException("Stream must not be null.");
        }

        if (!in.markSupported()) {
            throw new IllegalArgumentException("Mark is not supported.");
        }

        final byte[] signature = new byte[12];
        in.mark(signature.length);
        try {
            int signatureLength = in.read(signature);
            in.reset();
            if (ZipArchiveInputStream.matches(signature, signatureLength)) {
                return new ZipArchiveInputStream(in);
            } else if (JarArchiveInputStream.matches(signature, signatureLength)) {
                return new JarArchiveInputStream(in);
            } else if (ArArchiveInputStream.matches(signature, signatureLength)) {
                return new ArArchiveInputStream(in);
            } else if (CpioArchiveInputStream.matches(signature, signatureLength)) {
                return new CpioArchiveInputStream(in);
            }

            // Dump needs a bigger buffer to check the signature;
            final byte[] dumpsig = new byte[32];
            in.mark(dumpsig.length);
            signatureLength = in.read(dumpsig);
            in.reset();
            if (DumpArchiveInputStream.matches(dumpsig, signatureLength)) {
                return new DumpArchiveInputStream(in);
            }

            // Tar needs an even bigger buffer to check the signature; read the first block
            final byte[] tarheader = new byte[512];
            in.mark(tarheader.length);
            signatureLength = in.read(tarheader);
            in.reset();
            if (TarArchiveInputStream.matches(tarheader, signatureLength)) {
                return new TarArchiveInputStream(in);
            }
            // COMPRESS-117 - improve auto-recognition
            try {
                TarArchiveInputStream tais = new TarArchiveInputStream(new ByteArrayInputStream(tarheader));
                tais.getNextEntry();
                return new TarArchiveInputStream(in);
            } catch (Exception e) { // NOPMD
                // can generate IllegalArgumentException as well as IOException
                // autodetection, simply not a TAR
                // ignored
            }
        } catch (IOException e) {
            throw new ArchiveException("Could not use reset and mark operations.", e);
        }

        throw new ArchiveException("No Archiver found for the stream signature");
    }
}
```

