package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Macro menu.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills and Bill Campbell
 * @version 1.0
 */
public class MacroActions {

    /** A list of actions for the Macro menu. */
    protected ArrayList<Action> actions;

    /** Accesses the language bundle */
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    protected static JMenu macroMenu = new JMenu(MessagePrefBundle.getString("MB_MACRO"));

    /**
     * <p>
     * Create a set of Macro menu actions.
     * </p>
     */
    public MacroActions() {
        actions = new ArrayList<Action>();
        actions.add(new StartMacroAction(MessagePrefBundle.getString("AB_MACRO_START"), null,
                MessagePrefBundle.getString("DS_MACRO_START"), null));
        actions.add(new EndAndSaveMacroAction(MessagePrefBundle.getString("AB_MACRO_ENDANDSAVE"), null,
                MessagePrefBundle.getString("DS_MACRO_ENDANDSAVE"), null));
        actions.add(new CancelMacroAction(MessagePrefBundle.getString("AB_MACRO_CANCEL"), null,
                MessagePrefBundle.getString("DS_MACRO_CANCEL"), null));
        actions.add(new LoadMacroAction(MessagePrefBundle.getString("AB_MACRO_LOAD"), null,
                MessagePrefBundle.getString("DS_MACRO_LOAD"), null));

        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), new StartMacroAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), new EndAndSaveMacroAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), new CancelMacroAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), new LoadMacroAction(null, null, null, null));

    }

    /**
     * <p>
     * Create a menu containing the list of Macro actions.
     * </p>
     * 
     * @return The Macro menu UI element.
     */
    public JMenu createMenu() {

        for (Action action : actions) {
            macroMenu.add(new JMenuItem(action));
        }

        return macroMenu;
    }

    /**
     * <p>
     * Action to start recording a macro.
     * </p>
     */
    public class StartMacroAction extends ImageAction {

        /**
         * <p>
         * Create a new start macro action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        StartMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the start macro action is triggered.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            target.startRecordingBlink();
            target.getImage().startMacro();
        }
    }

    /**
     * <p>
     * Action to end and save a macro.
     * </p>
     */
    public class EndAndSaveMacroAction extends ImageAction {

        /**
         * <p>
         * Create a new end and save macro action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        EndAndSaveMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the end and save macro action is triggered.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            target.stopRecordingBlink();
            target.getImage().endMacro();
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String macroFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().saveMacro(macroFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_MACRO_ENDANDSAVE"), null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * <p>
     * Action to cancel recording a macro.
     * </p>
     */
    public class CancelMacroAction extends ImageAction {

        /**
         * <p>
         * Create a new cancel macro action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        CancelMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the cancel macro action is triggered.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            target.stopRecordingBlink();
            target.getImage().cancelMacro();
        }
    }

    /**
     * <p>
     * Action to load a macro from a file.
     * </p>
     */
    public class LoadMacroAction extends ImageAction {

        /**
         * <p>
         * Create a new load macro action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        LoadMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the load macro action is triggered.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String macroFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().loadMacro(macroFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_MACRO_LOAD"), null, JOptionPane.ERROR_MESSAGE);
                }
            }

            target.repaint();
            target.getParent().revalidate();
        }
    }
}