package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be
 * added.
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
public class EditActions {

    /** Accesses the language bundle */
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    protected static JMenu editMenu = new JMenu(MessagePrefBundle.getString("MB_EDIT"));

    /** A list of actions for the Edit menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<Action>();
        actions.add(new UndoAction(MessagePrefBundle.getString("AB_EDIT_UNDO"), null,
                MessagePrefBundle.getString("DS_EDIT_UNDO"), Integer.valueOf(KeyEvent.VK_Z)));
        actions.add(new RedoAction(MessagePrefBundle.getString("AB_EDIT_REDO"), null,
                MessagePrefBundle.getString("DS_EDIT_REDO"), Integer.valueOf(KeyEvent.VK_Y)));
        actions.add(new FlipAction(MessagePrefBundle.getString("AB_EDIT_FLIPHORIZONTAL"), null,
                MessagePrefBundle.getString("DS_EDIT_FLIPHORIZONTAL"), Integer.valueOf(KeyEvent.VK_H),
                true));
        actions.add(new FlipAction(MessagePrefBundle.getString("AB_EDIT_FLIPVERTICAL"), null,
                MessagePrefBundle.getString("DS_EDIT_FLIPVERTICAL"), Integer.valueOf(KeyEvent.VK_V),
                false));
        actions.add(new ScaleTransformAction(MessagePrefBundle.getString("AB_EDIT_RESIZE"), null,
                MessagePrefBundle.getString("DS_EDIT_RESIZE"), null));
        actions.add(new RotateTransformAction(MessagePrefBundle.getString("AB_EDIT_ROTATION"), null,
                MessagePrefBundle.getString("DS_EDIT_ROTATION"), null));
        actions.add(new CropAction(MessagePrefBundle.getString("AB_EDIT_CROP"), null,
                MessagePrefBundle.getString("DS_EDIT_CROP"), null));
        actions.add(new DrawRectangleAction(MessagePrefBundle.getString("AB_EDIT_DRAW_RECTANGLE"), null,
                MessagePrefBundle.getString("DS_EDIT_DRAW_RECTANGLE"), null));
        actions.add(new DrawOvalAction(MessagePrefBundle.getString("AB_EDIT_DRAW_OVAL"), null,
                MessagePrefBundle.getString("DS_EDIT_DRAW_OVAL"), null));
        actions.add(new DrawLineAction(MessagePrefBundle.getString("AB_EDIT_DRAW_LINE"), null, 
                MessagePrefBundle.getString("DS_EDIT_DRAW_LINE"), null));
        Toolbar.add(new UndoAction(MessagePrefBundle.getString("AB_EDIT_UNDO"), new ImageIcon("src/toolbarIcons/UndoIcon.png"),
                MessagePrefBundle.getString("DS_EDIT_UNDO"), null));
        Toolbar.add(new RedoAction(MessagePrefBundle.getString("AB_EDIT_REDO"), new ImageIcon("src/toolbarIcons/RedoIcon.png"), 
                MessagePrefBundle.getString("DS_EDIT_REDO"), null));
        Toolbar.add(new DrawOvalAction(MessagePrefBundle.getString("AB_EDIT_DRAW_OVAL"), new ImageIcon("src/toolbarIcons/DrawOvalIcon.png"),
                MessagePrefBundle.getString("DS_EDIT_DRAW_OVAL"), null));
        Toolbar.add(new DrawRectangleAction(MessagePrefBundle.getString("AB_EDIT_DRAW_RECTANGLE"), new ImageIcon("src/toolbarIcons/DrawRectangleIcon.png"),
                MessagePrefBundle.getString("DS_EDIT_DRAW_RECTANGLE"), null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), new UndoAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), new RedoAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK), new FlipAction(null, null, null, null, true));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), new FlipAction(null, null, null, null, false));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), new ScaleTransformAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK), new RotateTransformAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK), new DrawRectangleAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK), new DrawOvalAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK), new DrawLineAction(null, null, null, null));
    }
    

    

    /**
     * <p>
     * Create a menu containing the list of Edit actions.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {

        for (Action action : actions) {
            editMenu.add(new JMenuItem(action));
        }

        return editMenu;
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>  
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().undo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            target.getImage().redo();
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to flip an image horizontally or vertically.
     * </p>
     */
    public class FlipAction extends ImageAction {
        private boolean horizontal;

        /**
         * <p>
         * Create a new flip action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         * @param horizontal true for horizontal flip, false for vertical flip
         */
        FlipAction(String name, ImageIcon icon, String desc, Integer mnemonic, boolean horizontal) {
            super(name, icon, desc, mnemonic);
            this.horizontal = horizontal;
        }

        /**
         * <p>
         * Callback for when the flip action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FlipAction is triggered.
         * It flips the image horizontally or vertically based on the flag.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            target.getImage().apply(new ImageFlip(horizontal));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    /**
     * <p>
     * Action to rotate an image
     * </p>
     */
    public class RotateTransformAction extends ImageAction {
        /**
         * <p>
         * Create a new rotate action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         */
        RotateTransformAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        
        /**
         * <p>
         * Callback for when the rotate transform action is triggered
         * </p>
         * @param e The event which triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            int angle = 180;
            Integer[] options = { 90, 180, 270, 360 };
            JComboBox<Integer> angleOptions = new JComboBox<>(options);
            int option = JOptionPane.showOptionDialog(null, angleOptions, MessagePrefBundle.getString("PU_EDIT_ROTATION"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                angle = (int) angleOptions.getSelectedItem();
            }
            target.getImage().apply(new RotateTransform(angle));
            target.repaint();
            target.getParent().revalidate();
        }
    }

    public class ScaleTransformAction extends ImageAction {
        /**
         * <p>
         * Create a new scale transform action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         */
        ScaleTransformAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the scale transform action is triggered
         * </p>
         * 
         * @param e The event triggering the callback 
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            // Determine the scale factors - ask the user.
            int scaleX = 1;
            int scaleY = 1;

            // Pop-up dialog box to ask for the horizontal scale factor.
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 1000, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int option = JOptionPane.showOptionDialog(null, radiusSpinner, MessagePrefBundle.getString("PU_EDIT_RESIZE_X"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (option == JOptionPane.OK_OPTION) {
                scaleX = radiusModel.getNumber().intValue();
            }
            // Pop-up dialog box to ask for the vertical scale factor.
            SpinnerNumberModel yRadiusModel = new SpinnerNumberModel(1, 1, 1000, 1);
            JSpinner yRadiusSpinner = new JSpinner(yRadiusModel);
            int yOption = JOptionPane.showOptionDialog(null, yRadiusSpinner, MessagePrefBundle.getString("PU_EDIT_RESIZE_Y"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            // Check the return value from the dialog box.
            if (yOption == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (yOption == JOptionPane.OK_OPTION) {
                scaleY = yRadiusModel.getNumber().intValue();
            }
            System.out.println(scaleX + " " + scaleY);
            target.getImage().apply(new ScaleTransform(scaleX, scaleY));
            target.repaint();
            target.getParent().revalidate();

        }
    }

    /**
     * <p>
     * Action to crop the image to a selected region
     * </p>
     */
    public class CropAction extends ImageAction {
        /**
         * <p>
         * Create a new crop action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         */
        CropAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the crop action is triggered
         * </p>
         * 
         * @param e The event triggering the callback
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            int[][] selection = target.getSelection();
            if(selection[1] == null){
                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_EDIT_CROP"), null, JOptionPane.WARNING_MESSAGE);
                return;
            }
            target.getImage().apply(new CropFilter(selection));
            target.clearSelection();
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to draw a rectangle on the image
     * </p>
     */
    public class DrawRectangleAction extends ImageAction {
        /**
         * <p>
         * Create a new draw rectangle action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         */
        DrawRectangleAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        
        /**
         * <p>
         * Callback for when the draw rectangle action is triggered
         * </p>
         * 
         * @param e The event triggering the callback
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage())
                return;
            if(target.getSelection()[1] == null){
                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_EDIT_DRAW_NOSELECTION"), null, JOptionPane.WARNING_MESSAGE);
                return;
            }
            DrawingSettings settings = target.getDrawingSettings();
            settings = drawingSettingsPrompt(target.getDrawingSettings(), true, true, true);
            if (settings.outlineColour != null && settings.fillColour != null) {
                int[][] selection = target.getSelection();
                target.getImage().apply(new DrawRectangle(selection[0][0], selection[0][1], selection[1][0], selection[1][1], settings));
                target.setDrawingSettings(settings);
                target.repaint();
                target.getParent().revalidate();
            }
        }
    }

    /**
     * <p>
     * Action to draw an oval on the image.
     * </p>
     */
    public class DrawOvalAction extends ImageAction {
        /**
         * <p>
         * Create a new draw oval action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         */
        DrawOvalAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the draw oval action is triggered
         * </p>
         * 
         * @param e The event triggering the callback
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage())
                return;
            if(target.getSelection()[1] == null){
                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_EDIT_DRAW_NOSELECTION"), null, JOptionPane.WARNING_MESSAGE);
                return;
            }
            DrawingSettings settings = target.getDrawingSettings();
            settings = drawingSettingsPrompt(target.getDrawingSettings(), true, true, true);
            if (settings.outlineColour != null && settings.fillColour != null) {
                int[][] selection = target.getSelection();
                target.getImage().apply(new DrawOval(selection[0][0], selection[0][1], selection[1][0], selection[1][1], settings));
                target.setDrawingSettings(settings);
                target.repaint();
                target.getParent().revalidate();
            }
        }
    }

    /**
     * <p>
     * Action to draw a line on the image
     * </p>
     */
    public class DrawLineAction extends ImageAction {
        /**
         * <p>
         * Create a new draw line action.
         * </p>
         * 
         * @param name       The name of the action (ignored if null).
         * @param icon       An icon to use to represent the action (ignored if null).
         * @param desc       A brief description of the action (ignored if null).
         * @param mnemonic   A mnemonic key to use as a shortcut (ignored if null).
         */
        DrawLineAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the draw line action is triggered
         * </p>
         * 
         * @param e The event triggering the callback
         */
        public void actionPerformed(ActionEvent e) {
            if(!target.getImage().hasImage())
                return;
            if(target.getSelection()[1] == null){
                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_EDIT_DRAW_NOSELECTION"), null, JOptionPane.WARNING_MESSAGE);
                return;
            }
            DrawingSettings settings = target.getDrawingSettings();
            settings = drawingSettingsPrompt(target.getDrawingSettings(), false, true, true);
            if (settings.fillColour != null) {
                int[][] selection = target.getSelection();
                target.getImage().apply(new DrawLine(selection[0][0], selection[0][1], selection[1][0], selection[1][1], settings));
                target.setDrawingSettings(settings);
                target.repaint();
                target.getParent().revalidate();
            }
        }
    }


    /**
     * Prompts the user for what settings they want to use for a drawing action.
     * 
     * @param currentDrawingSettings The current settings which are being used for drawing, for default values for the prompt
     * @param fillOutlinePrompt Whether to prompt the user about whether they want fill, outline or both
     * @param strokeSizePrompt Whether to prompt the user about what size of stroke they want to use for drawing
     * @param colourPrompt Whether to prompt the user about what colour(s) they want to draw in
     * @return
     */
    private DrawingSettings drawingSettingsPrompt(DrawingSettings currentDrawingSettings, boolean fillOutlinePrompt, boolean strokeSizePrompt, boolean colourPrompt){
        DrawingSettings newDrawingSettings = new DrawingSettings(currentDrawingSettings);
        if(fillOutlinePrompt){
            int i = -1;
            if(currentDrawingSettings.outline) i++;
            if(currentDrawingSettings.fill) i += 2;
            if(i == -1) i = 0;
            String[] outlineOrFillOptions = new String[]{
                MessagePrefBundle.getString("PU_EDIT_DRAW_OUTLINE"),
                MessagePrefBundle.getString("PU_EDIT_DRAW_FILL"),
                MessagePrefBundle.getString("PU_EDIT_DRAW_OUTLINEANDFILL")
            };
            String optionSelected = (String)JOptionPane.showInputDialog(null, MessagePrefBundle.getString("PU_EDIT_DRAW_OUTLINEORFILL"),
                    null, JOptionPane.QUESTION_MESSAGE, null, outlineOrFillOptions, outlineOrFillOptions[i]);
            int selectedOptionIndex = 0;            
            while(outlineOrFillOptions[selectedOptionIndex] != optionSelected) selectedOptionIndex++;
            newDrawingSettings.outline = false;
            newDrawingSettings.fill = false;
            if(selectedOptionIndex == 0 || selectedOptionIndex == 2) newDrawingSettings.outline = true;
            if(selectedOptionIndex == 1 || selectedOptionIndex == 2) newDrawingSettings.fill = true;
        }
        if(strokeSizePrompt && newDrawingSettings.outline){
            // Pop-up dialog box to ask for the height value of the block.
            SpinnerNumberModel model = new SpinnerNumberModel(currentDrawingSettings.strokeSize, 1, 100, 1);
            JSpinner spinner = new JSpinner(model);
            JOptionPane.showOptionDialog(null, spinner,
                    MessagePrefBundle.getString("PU_EDIT_DRAW_STROKESIZE"), JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            newDrawingSettings.strokeSize = model.getNumber().intValue();
        }
        if(colourPrompt){
            if(newDrawingSettings.outline && fillOutlinePrompt){
                newDrawingSettings.outlineColour = JColorChooser.showDialog(null, MessagePrefBundle.getString("PU_EDIT_DRAW_OUTLINECOLOUR"), currentDrawingSettings.outlineColour);
            }
            if(newDrawingSettings.fill || !fillOutlinePrompt){
                newDrawingSettings.fillColour = JColorChooser.showDialog(null, MessagePrefBundle.getString("PU_EDIT_DRAW_FILLCOLOUR"), currentDrawingSettings.outlineColour);
            }
        }
        return newDrawingSettings;
    }
}