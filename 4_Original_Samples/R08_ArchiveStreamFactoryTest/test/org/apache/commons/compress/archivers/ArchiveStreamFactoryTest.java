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