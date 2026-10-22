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
        assertNotNull("Locale should not be null", locale);
        assertEquals("Locale string representation should match", "en", locale.toString());

        locale = LocaleUtils.toLocale("en_GB");
        assertNotNull("Locale should not be null", locale);
        assertEquals("Locale string representation should match", "en_GB", locale.toString());

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull("Locale should not be null", locale);
        assertEquals("Locale string representation should match", "en_GB_xxx", locale.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertNotNull("Lookup list should not be null", list);
        assertEquals("Lookup list size should be 3", 3, list.size());
        assertTrue("Lookup list should contain fr_CA_xxx locale", list.contains(new Locale("fr", "CA", "xxx")));
        assertTrue("Lookup list should contain fr_CA locale", list.contains(new Locale("fr", "CA")));
        assertTrue("Lookup list should contain fr locale", list.contains(new Locale("fr")));

        list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertNotNull("Lookup list with default locale should not be null", list);
        assertEquals("Lookup list with default locale size should be 4", 4, list.size());
        assertTrue("Lookup list should contain fr_CA_xxx locale", list.contains(new Locale("fr", "CA", "xxx")));
        assertTrue("Lookup list should contain fr_CA locale", list.contains(new Locale("fr", "CA")));
        assertTrue("Lookup list should contain fr locale", list.contains(new Locale("fr")));
        assertTrue("Lookup list should contain default en locale", list.contains(new Locale("en")));

        list = LocaleUtils.localeLookupList(null);
        assertNotNull("Lookup list with null locale should not be null", list);
        assertEquals("Lookup list with null locale size should be 0", 0, list.size());
    }

    @Test
    public void testAvailableLocales() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull("Available locale list should not be null", list);
        assertTrue("Available locale list should not be empty", list.size() > 0);

        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull("Available locale set should not be null", set);
        assertTrue("Available locale set should not be empty", set.size() > 0);

        boolean isAvailable = LocaleUtils.isAvailableLocale(Locale.US);
        assertTrue("US locale should be available", isAvailable);

        isAvailable = LocaleUtils.isAvailableLocale(new Locale("invalid"));
        assertFalse("Invalid locale should not be available", isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertNotNull("Languages by country list should not be null", list);
        assertTrue("Languages by country list should not be empty", list.size() > 0);

        list = LocaleUtils.languagesByCountry(null);
        assertNotNull("Languages by country list with null code should not be null", list);
        assertEquals("Languages by country list with null code size should be 0", 0, list.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assertNotNull("Countries by language list should not be null", list);
        assertTrue("Countries by language list should not be empty", list.size() > 0);

        list = LocaleUtils.countriesByLanguage(null);
        assertNotNull("Countries by language list with null code should not be null", list);
        assertEquals("Countries by language list with null code size should be 0", 0, list.size());
    }
}
