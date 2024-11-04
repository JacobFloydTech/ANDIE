package cosc202.andie;

import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.ResourceBundle;

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various
 * image editing and processing operations.
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
public class Andie {
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     * 
     * <p>
     * This method sets up an interface consisting of an active image (an
     * {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save,
     * edit, etc.
     * These operations are implemented {@link ImageOperation}s and triggered via
     * {@code ImageAction}s grouped by their general purpose into menus.
     * </p>
     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * @see MacroActions
     * 
     * @throws Exception if something goes wrong.
     */
    private static void createAndShowGUI() throws Exception {
        // Set up the main GUI frame
        JFrame frame = new JFrame("ANDIE");

        frame.setFocusable(true);
        frame.requestFocus();
        frame.requestFocusInWindow();
        KeyboardListener kl = new KeyboardListener();
        frame.addKeyListener(kl);

        try {
            Image image = ImageIO.read(new File("./src/icon.png"));
            frame.setIconImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load ANDIE icon");
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main content area is an ImagePanel
        ImagePanel imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add in menus for various types of action the user may perform.
        JMenuBar menuBar = new JMenuBar();

        // Universal font support as ANDIE has Japanese compatibility.
        InputStream is = Andie.class.getResourceAsStream("NotoSansCJKsc-Regular.otf");
        System.out.println(is);
        Font font = Font.createFont(Font.TRUETYPE_FONT, is);
        frame.setFont(font.deriveFont(18f));
        menuBar.setFont(font.deriveFont(18f));

        // File menus are pretty standard, so things that usually go in File menus go
        // here.
        FileActions fileActions = new FileActions();
        menuBar.add(fileActions.createMenu());

        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        menuBar.add(editActions.createMenu());

        // View actions control how the image is displayed, but do not alter its actual
        // content
        ViewActions viewActions = new ViewActions();
        menuBar.add(viewActions.createMenu());

        // Filters apply a per-pixel operation to the image, generally based on a local
        // window
        FilterActions filterActions = new FilterActions();
        menuBar.add(filterActions.createMenu());

        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        menuBar.add(colourActions.createMenu());

        // Actions related to macros
        MacroActions macroActions = new MacroActions();
        menuBar.add(macroActions.createMenu());

        // Actions to change the language of the application
        LanguageActions languageActions = new LanguageActions();
        menuBar.add(languageActions.createMenu());

        frame.setJMenuBar(menuBar);
        frame.add(Toolbar.toolBar, BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Creates and launches the main GUI in a separate thread.
     * As a result, this is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        // sets up the language prefs
        LanguagePreferences.langPreferences();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }
}