package service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationService {
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
            System.err.println("Failed to load resource bundle for locale: " + locale);
            // Fallback to English
            try {
                ResourceBundle fallback = ResourceBundle.getBundle(
                        "i18n.MessagesBundle",
                        new Locale("en", "US")
                );
                for (String key : fallback.keySet()) {
                    strings.put(key, fallback.getString(key));
                }
            } catch (Exception ex) {
                // Use hardcoded defaults as last resort
                strings.put("title", "Shopping Cart Calculator");
                strings.put("amount", "Amount of different products:");
                strings.put("cost", "The cost of this product:");
                strings.put("pieces", "How many pieces:");
                strings.put("calculate", "Calculate avg");
                strings.put("total", "Total price:");
                strings.put("addItems", "Add items to see the total price!");
                strings.put("sign", "$");
                strings.put("error_invalid_input", "Please enter valid numbers");
                strings.put("itemNum", ". item");
                strings.put("results_def", "Results are shown here");
                strings.put("add_button", "Add Item");
                strings.put("scan_button", "Start Scanning");

            }
        }

        return strings;
    }


}
