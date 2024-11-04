package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighborhood.
 * This includes a mean filter (a simple blur) in the sample code, but more
 * operations will need to be added.
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
public class FilterActions {

    /** A list of actions for the Filter menu. */
    protected ArrayList<Action> actions;

    /** Accesses the language bundle */
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    /** Instantiate the file menu */
    protected static JMenu fileMenu = new JMenu(MessagePrefBundle.getString("MB_FILTER"));

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     */
    public FilterActions() {
        actions = new ArrayList<Action>();
        actions.add(new MeanFilterAction(MessagePrefBundle.getString("AB_FILTER_MEAN"), null,
                MessagePrefBundle.getString("DS_FILTER_MEAN"), null));
        actions.add(new MedianFilterAction(MessagePrefBundle.getString("AB_FILTER_MEDIAN"), null,
                MessagePrefBundle.getString("DS_FILTER_MEDIAN"), null));
        actions.add(new SharpenFilterAction(MessagePrefBundle.getString("AB_FILTER_SHARPEN"), null,
                MessagePrefBundle.getString("DS_FILTER_SHARPEN"), null));
        actions.add(new GaussianFilterAction(MessagePrefBundle.getString("AB_FILTER_GAUSSIAN"), null,
                MessagePrefBundle.getString("DS_FILTER_GAUSSIAN"), null));
        actions.add(new BlockAveragingFilterAction(MessagePrefBundle.getString("AB_FILTER_BLOCKAVERAGE"), null,
                MessagePrefBundle.getString("DS_FILTER_BLOCKAVERAGE"), null));
        actions.add(new RandomScatteringAction(MessagePrefBundle.getString("AB_FILTER_SCATTER"), null,
                MessagePrefBundle.getString("DS_FILTER_SCATTER"), null));
        actions.add(new EmbossFilterAction(MessagePrefBundle.getString("AB_FILTER_EMBOSS"), null,
                MessagePrefBundle.getString("DS_FILTER_EMBOSS"), null));
        actions.add(new SobelHorizontalFilterAction(MessagePrefBundle.getString("AB_FILTER_SOBEL_HORIZONTAL"), null,
                MessagePrefBundle.getString("DS_FILTER_SOBEL_HORIZONTAL"), null));
        actions.add(new SobelVerticalFilterAction(MessagePrefBundle.getString("AB_FILTER_SOBEL_VERTICAL"), null,
                MessagePrefBundle.getString("DS_FILTER_SOBEL_VERTICAL"), null));
        
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK), new MeanFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), new MedianFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), new SharpenFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK), new GaussianFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK), new BlockAveragingFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), new RandomScatteringAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK), new EmbossFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), new SobelHorizontalFilterAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), new SobelVerticalFilterAction(null, null, null, null));
    
    }

    /**
     * <p>
     * Create a menu containing the list of Filter actions.
     * </p>
     * 
     * @return The filter menu UI element.
     */
    public JMenu createMenu() {
        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));

        }
        return fileMenu;
    }

    /**
     * Action to apply a sharpen filter to the image
     */
    public class SharpenFilterAction extends ImageAction {
        /**
         * <p>
         * Create a new sharpen-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic); // Call superclass constructor
        }
        
        /**
         * Callback for when the sharpen action is used
         * @param e The event which triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    MessagePrefBundle.getString("PU_FILTER_SHARPEN"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new SharpenFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * Action to apply a mean filter to the image
     */
    public class MeanFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applies an appropriately sized
         * {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    MessagePrefBundle.getString("PU_FILTER_MEAN"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new MeanFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to remove noise with a median filter.
     * </p>
     * 
     * @see MedianFilter
     */
    public class MedianFilterAction extends ImageAction {
        /**
         * <p>
         * Create a new median-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */

        MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It prompts the user for a filter radius, then applies the appropriately sized
         * {@link MedianFilter}
         * </p>
         * 
         * @param e The event triggering the callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    MessagePrefBundle.getString("PU_FILTER_MEDIAN"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new MedianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }
    
    /**
     * Action to apply a gaussian filter to the image
     */
    public class GaussianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new gaussian-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        GaussianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the gaussian action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the gaussian action is triggered.
         * It prompts the user for a filter radius, then applies an appropriately sized
         * {@link GaussianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the radius - ask the user.
            int radius = 1;

            // Pop-up dialog box to ask for the radius value.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner,
                    MessagePrefBundle.getString("PU_FILTER_GAUSSIAN"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                radius = radiusModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new GaussianFilter(radius));
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to apply a block averaging filter.
     * </p>
     * 
     * @see MedianFilter
     */
    public class BlockAveragingFilterAction extends ImageAction {
        /**
         * <p>
         * Create a new block averaging filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */

        BlockAveragingFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * This method is called whenever the block averaging filter action is triggered.
         * </p>
         * 
         * @param e The event triggering the callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the height and width - ask the user.
            int blockHeight = 1;
            int blockWidth = 1;

            // Pop-up dialog box to ask for the height value of the block.
            SpinnerNumberModel heightModel = new SpinnerNumberModel(1, 1, 1000, 1);
            JSpinner heightSpinner = new JSpinner(heightModel);
            int heightOption = JOptionPane.showOptionDialog(null, heightSpinner,
                    MessagePrefBundle.getString("PU_FILTER_BLOCKAVERAGE_HEIGHT"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            // Check the return value from the dialog box.
            if (heightOption == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (heightOption == JOptionPane.OK_OPTION) {
                blockHeight = heightModel.getNumber().intValue();
            }

            // Pop-up dialog box to ask for the width value.
            SpinnerNumberModel widthModel = new SpinnerNumberModel(1, 1, 1000, 1);
            JSpinner widthSpinner = new JSpinner(widthModel);
            int widthOption = JOptionPane.showOptionDialog(null, widthSpinner,
                    MessagePrefBundle.getString("PU_FILTER_BLOCKAVERAGE_WIDTH"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            // Check the return value from the dialog box.
            if (widthOption == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (widthOption == JOptionPane.OK_OPTION) {
                blockWidth = widthModel.getNumber().intValue();
            }

            // Create and apply the filter
            target.getImage().apply(new BlockAveragingFilter(blockHeight, blockWidth));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * Action to apply a random scattering filter to the image
     */
    public class RandomScatteringAction extends ImageAction {

        /**
         * <p>
         * Create a new RandomScattering action.
         * </p>
         *
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        RandomScatteringAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the RandomScattering action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the RandomScatteringAction is triggered.
         * It applies the random scattering effect to the image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage())
                return;
            
            SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 1000, 1);
            JSpinner spinner = new JSpinner(model);
            int option = JOptionPane.showOptionDialog(null, spinner,
                    MessagePrefBundle.getString("PU_FILTER_SCATTER"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            // Check the return value from the dialog box.
            if (option == JOptionPane.OK_OPTION) {
                int radius = model.getNumber().intValue();
                target.getImage().apply(new RandomScattering(radius));
                target.repaint();
                target.getParent().revalidate();
            }
        }

    }


    /**
     * <p>
     * Action to apply an Emboss filter.
     * </p>
     */
    public class EmbossFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new emboss filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         * @param kernelIndex The index of the emboss kernel to use.
         */
        EmbossFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the emboss filter action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the EmbossFilterAction is triggered.
         * It applies the emboss filter to the currently loaded image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
                   
            // Prompt for strength with a detailed message
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 315, 45);
            JSpinner spinner = new JSpinner(model);
            //The following line is from the question in https://overflow.perennialte.ch/questions/6904326/how-do-i-disable-keyboard-and-mouse-entry-for-a-jspinner
            ((DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
            int option = JOptionPane.showOptionDialog(null, spinner,
                    MessagePrefBundle.getString("PU_FILTER_EMBOSS"), JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
        
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
            int orientation = model.getNumber().intValue();
        
            // Apply the emboss filter with the selected kernel and strength
            target.getImage().apply(new EmbossFilter(orientation % 45));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to apply the Sobel horizontal filter.
     * </p>
     */
    public class SobelHorizontalFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Sobel horizontal filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        SobelHorizontalFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Sobel horizontal filter action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the SobelHorizontalFilterAction is triggered.
         * It applies the Sobel horizontal filter to the currently loaded image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            target.getImage().apply(new SobelFilter(SobelFilter.HORIZONTAL));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to apply the Sobel vertical filter.
     * </p>
     */
    public class SobelVerticalFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new Sobel vertical filter action.
         * </p>
         *
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        SobelVerticalFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the Sobel vertical filter action is triggered.
         * </p>
         *
         * <p>
         * This method is called whenever the SobelVerticalFilterAction is triggered.
         * It applies the Sobel vertical filter to the currently loaded image.
         * </p>
         *
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
                
            target.getImage().apply(new SobelFilter(SobelFilter.VERTICAL));
            target.repaint();
            target.getParent().revalidate();
        }
    }
}