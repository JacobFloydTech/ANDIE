package cosc202.andie;

import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * <p>
 * Establishes the user preferences
 * </p>
 * 
 * <p>
 * Instantiates the language preference files,
 * loads in the corresponding language to what the user prefers.
 * </p>
 * 
 * @author Blake Cooper
 * @version 1.0
 */
public class LanguagePreferences {

    protected static ResourceBundle languageBundle;

    protected static Preferences prefs = Preferences.userNodeForPackage(LanguageActions.class);

    /**
     * <p>
     * Method to set up all user preferences for ANDIE.
     * </P>
     * 
     * @see Andie.java
     */
    public static void langPreferences() {
        prefs = Preferences.userNodeForPackage(Andie.class);
        Locale locale = new Locale(prefs.get("language", "en"), prefs.get("country", "NZ"));
        Locale.setDefault(locale);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        String bundlePath = "MessageBundle_" + locale.getLanguage() + "_" + locale.getLanguage() + ".properties";
        try (InputStream inputStream = classLoader.getResourceAsStream(bundlePath)) {
            languageBundle = new PropertyResourceBundle(inputStream);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
