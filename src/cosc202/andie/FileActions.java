package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the File menu.
 * </p>
 * 
 * <p>
 * The File menu is very common across applications,
 * and there are several items that the user will expect to find here.
 * Opening and saving files is an obvious one, but also exiting the program.
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
public class FileActions {

    private final String LICENSE = "THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.";

    /** A list of actions for the File menu. */
    protected ArrayList<Action> actions;

    /** Accesses the language bundle */
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    protected static JMenu fileMenu = new JMenu(MessagePrefBundle.getString("MB_FILE"));

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {
        actions = new ArrayList<Action>();
        actions.add(new FileOpenAction(MessagePrefBundle.getString("AB_FILE_OPEN"), null,
                MessagePrefBundle.getString("DS_FILE_OPEN"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FileSaveAction(MessagePrefBundle.getString("AB_FILE_SAVE"), null,
                MessagePrefBundle.getString("DS_FILE_SAVE"), Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new FileSaveAsAction(MessagePrefBundle.getString("AB_FILE_SAVEAS"), null,
                MessagePrefBundle.getString("DS_FILE_SAVEAS"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(MessagePrefBundle.getString("AB_FILE_EXPORT"), null,
                MessagePrefBundle.getString("DS_FILE_EXPORT"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(new PrintAction(MessagePrefBundle.getString("AB_FILE_PRINT"), null,
                MessagePrefBundle.getString("DS_FILE_PRINT"), Integer.valueOf(KeyEvent.VK_P)));
        actions.add(new LicenseAction(MessagePrefBundle.getString("AB_FILE_LICENSE"), null,
                MessagePrefBundle.getString("DS_FILE_LICENSE"), null));
        actions.add(new FileExitAction(MessagePrefBundle.getString("AB_FILE_EXIT"), null,
                MessagePrefBundle.getString("DS_FILE_EXIT"), null));
        
        Toolbar.add(new FileSaveAction(null, new ImageIcon("src/toolbarIcons/saveIcon.png"),
                MessagePrefBundle.getString("DS_FILE_SAVE"), null));
        
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), new FileOpenAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), new FileSaveAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), new FileSaveAsAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), new FileExportAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK), new PrintAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), new LicenseAction(null, null, null, null));
        KeyboardListener.add(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), new FileExitAction(null, null, null, null));
    }

    /**
     * <p>
     * Create a menu containing the list of File actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {

        for (Action action : actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     * @see EditableImage#open(String)
     */
    public class FileOpenAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileOpenAction is triggered.
         * It prompts the user to select a file and opens it as an image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (target.getImage().hasImage()) {
                if(!target.getImage().isSaved()) {
                    int choice = JOptionPane.showConfirmDialog(null, MessagePrefBundle.getString("PU_FILE_OPEN"), null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    switch (choice){
                        case JOptionPane.CANCEL_OPTION:
                        case JOptionPane.CLOSED_OPTION:
                            return; //cancel the open operation
                        case JOptionPane.OK_OPTION:
                            try { target.getImage().save(); }
                            catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_SAVE"), null, JOptionPane.ERROR_MESSAGE);
                                return;
                            } // if ok is selected then attempt to save and continue
                        // if no is selected then neither of these two things happen and execution continues
                    }
                }
            }
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().open(imageFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_OPEN"), null, JOptionPane.ERROR_MESSAGE);
                }
            }

            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     * 
     * @see EditableImage#save()
     */
    public class FileSaveAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAction is triggered.
         * It saves the image to its original filepath.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            try {
                target.getImage().save();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_SAVE"), null, JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     * @see EditableImage#saveAs(String)
     */
    public class FileSaveAsAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().saveAs(imageFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_SAVEAS"), null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    /**
     * <p>
     * Action to export an image to a new file location.
     * </p>
     * 
     * @see EditableImage#export(String)
     */
    public class FileExportAction extends ImageAction {

        /**
         * <p>
         * Create a new file-export action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().export(imageFilepath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_EXPORT"), null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * <p>
     * Action to print an image to a Printer
     * </p>
     * 
     * @see java.awt.print
     */
    public class PrintAction extends ImageAction {

        /**
         * <p>
         * Create a new print action.
         * </p>
         * 
         * @param name
         * @param icon
         * @param desc
         * @param mnemonic
         */
        PrintAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }
        
        /**
         * <p>
         * Call to the .print() method upon the selected image.
         * </p>
         * 
         * @see EditableImage#print()
         * 
         * <p>
         * This method is called whenever the PrintAction is triggered.
         * A popup window prompts the user with the java base printing options.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!target.getImage().hasImage())
                return;
            try{
                target.getImage().print();
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_PRINT"), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * <p>
     * Action to quit the ANDIE application.
     * </p>
     */
    public class FileExitAction extends ImageAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-exit action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExitAction is triggered.
         * It quits the program.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (target.getImage().hasImage()) {
                if(!target.getImage().isSaved()) {
                    int choice = JOptionPane.showConfirmDialog(null, MessagePrefBundle.getString("PU_FILE_EXIT"), null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    switch (choice){
                        case JOptionPane.CANCEL_OPTION:
                        case JOptionPane.CLOSED_OPTION:
                            return; //cancel the open operation
                        case JOptionPane.OK_OPTION:
                            try { target.getImage().save(); }
                            catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, MessagePrefBundle.getString("ER_FILE_SAVE"), null, JOptionPane.ERROR_MESSAGE);
                                return;
                            } // if ok is selected then attempt to save and continue
                        // if no is selected then neither of these two things happen and execution continues
                    }
                }
            }

            System.exit(0);
        }

    }

    /**
     * <p>
     * Action to show the license.
     * </p>
     */
    public class LicenseAction extends ImageAction {

        /**
         * <p>
         * Create a new license action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        LicenseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the action is triggered
         * </p>
         * @param e The event which triggered the callback
         */
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "<html><body><p style='width: 200px;'>"+LICENSE+"</p><body><html>", null, JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
