package cosc202.andie;

import java.util.*;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel
 * directly
 * without reference to the rest of the image.
 * This includes conversion to grayscale in the sample code, but more operations
 * will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Blake, Bill, Tim, Jacob.
 * @version 2.0
 */
public class ColourActions {

    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /** Accesses the language bundle */
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    /** Instantiate the file */
    static JMenu fileMenu = new JMenu(MessagePrefBundle.getString("MB_COLOUR"));

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        actions.add(new ColorCyclingFilterAction(MessagePrefBundle.getString("AB_COLOUR_CYCLING"), null,
                MessagePrefBundle.getString("DS_COLOUR_CYCLING"), null));
        actions.add(new InvertColorFilterAction(MessagePrefBundle.getString("AB_COLOUR_INVERT"), null,
                MessagePrefBundle.getString("DS_COLOUR_INVERT"), null));
        actions.add(new ConvertToGreyAction(MessagePrefBundle.getString("AB_COLOUR_GREYSCALE"), null,
                MessagePrefBundle.getString("DS_COLOUR_GREYSCALE"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new BrightnessAndContrastAction(MessagePrefBundle.getString("AB_COLOUR_BRIGHTNESSANDCONTRAST"), null,
                MessagePrefBundle.getString("DS_COLOUR_BRIGHTNESSANDCONTRAST"), null));
        
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), new ColorCyclingFilterAction(null, null, null, null));      
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK), new InvertColorFilterAction(null, null, null, null));      
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK), new ConvertToGreyAction(null, null, null, null));      
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK), new BrightnessAndContrastAction(null, null, null, null));
    }

    /**
     * <p>
     * Create a menu containing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }
        return fileMenu;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to cycle the colour channels of an image
     * </p>
     */
    public class ColorCyclingFilterAction extends ImageAction {
        /**
         * <p>
         * Create a new colour cycling action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */

        ColorCyclingFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the colour cycling filter is used
         * </p>
         * @param e The event that triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            Boolean inputValid = false;
            JTextField textField = new JTextField();
            String radiusString = "";
            do {

                int option = JOptionPane.showOptionDialog(null, textField,
                        MessagePrefBundle.getString("PU_COLOUR_CYCLING"),
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                } else if (option == JOptionPane.OK_OPTION) {
                    // Get the string value from the text field
                    radiusString = textField.getText().toLowerCase();
                    // You can then use this radiusString value as needed
                }
                inputValid = validStringInput(radiusString);
            } while (!inputValid);
            target.getImage().apply(new ColorCyclingFilter(radiusString));
            target.repaint();
            target.getParent().revalidate();
        }

        /**
         * Private function to validate whether or not the string is valid
         * First, it splits the string into an array, before looping ofter and
         * only getting the characters in 'rgb' and if they already do not exist in the
         * new string
         * This is to ensure that someone can't just enter "rrr".
         * If the length of the new input string is 3 at the end of this loop, then we
         * know it is a valid order
         * of rgb cycles, and can continue onto the filter.
         */
        private boolean validStringInput(String s) {
            String newInputString = "";
            String[] splitInput = s.split("");
            for (String splitString : splitInput) {
                if (splitString.equals("r") && !newInputString.contains("r") || splitString.equals("g")
                        && !newInputString.contains("g") || splitString.equals("b") && !newInputString.contains("b")) {
                    newInputString += splitString;
                }
            }

            return newInputString.length() == 3;
        }
    }

    /**
     * <p>
     * Action to invert the colours in an image
     * </p>
     */
    public class InvertColorFilterAction extends ImageAction {
        /**
         * <p>
         * Create a new invert colour action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        InvertColorFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the invert colours option is used
         * </p>
         * @param e The event that triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            // Create and apply the filter
            target.getImage().apply(new InvertColorFilter());
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to adjust the brightness and contrast in the image
     * </p>
     */
    public class BrightnessAndContrastAction extends ImageAction {

        /**
         * <p>
         * Create a new brightness and contrast action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        BrightnessAndContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the brightness and contrast action is triggered.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the radius - ask the user.
            int brightness = 0;
            int contrast = 0;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(0, -100, 100, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            radiusSpinner.setPreferredSize(new Dimension(250, 20));

            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    MessagePrefBundle.getString("PU_COLOUR_BRIGHTNESSANDCONTRAST_BRIGHTNESS"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                brightness = radiusModel.getNumber().intValue();
            }
            SpinnerNumberModel contrastModel = new SpinnerNumberModel(0, -100, 100, 1);
            JSpinner contrastSpinner = new JSpinner(contrastModel);
            contrastSpinner.setPreferredSize(new Dimension(250, 20));
            int contrastOption = JOptionPane.showOptionDialog(null, contrastSpinner,
                    MessagePrefBundle.getString("PU_COLOUR_BRIGHTNESSANDCONTRAST_CONTRAST"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (contrastOption == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (contrastOption == JOptionPane.OK_OPTION) {
                contrast = contrastModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new BrightnessContrast(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}
