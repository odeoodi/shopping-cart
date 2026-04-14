package service;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizationService {
    private LocalizationService() {
        /* This utility class should not be instantiated */
    }

    /**
     * Get localized strings for a specific locale
     */
    public static Map<String, String> getLocalizedStrings(Locale locale) {
        Map<String, String> strings = new HashMap<>();

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(
                    "i18n.MessagesBundle",
                    locale
            );

            // Extract all keys
            for (String key : bundle.keySet()) {
                strings.put(key, bundle.getString(key));
            }
        } catch (Exception e) {
            Logger.getLogger(LocalizationService.class.getName()).log(Level.SEVERE, null, e);
            // Fallback to English
                ResourceBundle fallback = ResourceBundle.getBundle(
                        "i18n.MessagesBundle",
                        new Locale("en", "US")
                );
                for (String key : fallback.keySet()) {
                    strings.put(key, fallback.getString(key));
                }
        }

        return strings;
    }


}
