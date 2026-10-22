package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.toString());

        locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en_GB", locale.toString());

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale);
        assertEquals("en_GB_xxx", locale.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertNotNull(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr")));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr")));
        assertTrue(list.contains(new Locale("en")));
    }

    @Test
    public void testLocaleLookupListWithNullLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        boolean isAvailable = LocaleUtils.isAvailableLocale(Locale.US);
        assertTrue(isAvailable);

        isAvailable = LocaleUtils.isAvailableLocale(new Locale("invalid"));
        assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}