package service;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceTest {

    @Test
    void getLocalizedStrings_shouldReturnEnglishStrings_forEnglishLocale() {
        Map<String, String> strings = LocalizationService.getLocalizedStrings(new Locale("en", "US"));

        assertNotNull(strings);
        assertFalse(strings.isEmpty());

        assertEquals("Shopping Cart Calculator", strings.get("title"));
        assertNotNull(strings.get("amount"));
        assertNotNull(strings.get("pieces"));
        assertNotNull(strings.get("cost"));
    }

    @Test
    void getLocalizedStrings_shouldReturnFinnishStrings_forFinnishLocale() {
        Map<String, String> strings = LocalizationService.getLocalizedStrings(new Locale("fi", "FI"));

        assertNotNull(strings);
        assertFalse(strings.isEmpty());

        assertNotNull(strings.get("title"));
        assertNotNull(strings.get("amount"));
        assertNotNull(strings.get("pieces"));
        assertNotNull(strings.get("cost"));
    }

    @Test
    void getLocalizedStrings_shouldFallback_whenLocaleDoesNotExist() {
        Map<String, String> strings = LocalizationService.getLocalizedStrings(new Locale("xx", "XX"));

        assertNotNull(strings);
        assertFalse(strings.isEmpty());

        assertNotNull(strings.get("title"));
        assertNotNull(strings.get("amount"));
        assertNotNull(strings.get("pieces"));
        assertNotNull(strings.get("cost"));
    }

    @Test
    void getLocalizedStrings_shouldContainDefaultKeys() {
        Map<String, String> strings = LocalizationService.getLocalizedStrings(new Locale("en", "US"));

        assertTrue(strings.containsKey("title"));
        assertTrue(strings.containsKey("amount"));
        assertTrue(strings.containsKey("cost"));
        assertTrue(strings.containsKey("pieces"));
        assertTrue(strings.containsKey("results_def"));
        assertTrue(strings.containsKey("add_button"));
        assertTrue(strings.containsKey("scan_button"));
    }
}