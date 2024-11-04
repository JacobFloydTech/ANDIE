package cosc202.andie;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.*;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * <p>
 * Actions provided by the Language menu.
 * </p>
 * 
 * <p>
 * Allows users to switch between Japanese and English (English by default).
 * Users are prompted to restart the application in order to see the changes take affect.
 * </p>
 * 
 * @author Blake Cooper and Bill Campbell
 * @version 1.0
 */
public class LanguageActions {
    
    /** A list of actions for the language menu. */
    protected ArrayList<Action> actions;
        
    /** Accesses the language bundle */
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    protected static Preferences prefs = Preferences.userNodeForPackage(LanguageActions.class);

    //instantiate file menu
    protected JMenu fileMenu = new JMenu(MessagePrefBundle.getString("MB_LANGUAGE"));

    /**
     * <p>
     * Create a set of Language menu actions.
     * </p>
     */
    public LanguageActions() {
        actions = new ArrayList<Action>();
        actions.add(new SetLanguageEnglishAction("English", null, "英語", null));
        actions.add(new SetLanguageJapaneseAction("日本語", null, "Japanese", null));
    
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, InputEvent.CTRL_DOWN_MASK), new SetLanguageEnglishAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK), new SetLanguageJapaneseAction(null, null, null, null));
    }

    /**
     * <p>
     * Create a menu containing the list of Language actions.
     * </p>
     * 
     * @return The language menu UI element.
     */
    public JMenu createMenu() {
            for(Action action: actions) {
                fileMenu.add(new JMenuItem(action));
            }
            return fileMenu;
        }
        
    /**
     * <p>
     * Action button to select English as the Application language.
     * </p>
     */
    public class SetLanguageEnglishAction extends ImageAction{
    
        /**
         * <p>
         * Create a new language change to English action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        SetLanguageEnglishAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }
        
        /**
         * <p>
         * Callback for setting the language to English.
         * </p>
         * 
         * @param e The event which triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            final String LANGUAGE_CODE = "en";
            final String COUNTRY_CODE = "NZ";

            if(prefs.get("language", "").equals(LANGUAGE_CODE) && prefs.get("country", "").equals(COUNTRY_CODE)) return;
            Locale.setDefault(new Locale(LANGUAGE_CODE, COUNTRY_CODE));
            prefs.put("language", LANGUAGE_CODE);
            prefs.put("country", COUNTRY_CODE);
            //Pop up window to prompt the restart.
            JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("PU_LANGUAGE_EN"));
        }
    }

    /**
     * <p>
     * Action button to select Japanese as the Application language.
     * </p>
     */
    public class SetLanguageJapaneseAction extends ImageAction{
        
        /**
         * <p>
         * Create a new language change to Japanese action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        SetLanguageJapaneseAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }
        
        /**
         * <p>
         * Callback for setting the language to Japanese.
         * </p>
         * 
         * @param e The event which triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            final String LANGUAGE_CODE = "ja";
            final String COUNTRY_CODE = "JP";

            if(prefs.get("language", "").equals(LANGUAGE_CODE) && prefs.get("country", "").equals(COUNTRY_CODE)) return;
            Locale.setDefault(new Locale(LANGUAGE_CODE, COUNTRY_CODE));
            prefs.put("language", LANGUAGE_CODE);
            prefs.put("country", COUNTRY_CODE);
            //Pop up window for the restart.
            JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("PU_LANGUAGE_JP"));
        }
    }
}