package cosc202.andie;

import java.awt.*;
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * <p>
 * UI display element for {@link EditableImage}s.
 * </p>
 * 
 * <p>
 * This class extends {@link JPanel} to allow for rendering of an image, as well
 * as zooming
 * in and out.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Bill, Blake, Jacob and Tim
 * @version 1.0
 */
public class ImagePanel extends JPanel {
    protected static ResourceBundle MessagePrefBundle = ResourceBundle.getBundle("MessageBundle");

    /**
     * The colour of the outline of the selection box which is drawn when the user
     * clicks and drags. This is hexadecimal argb.
     */
    private final int SELECTION_OUTLINE = 0x9930d9ea;

    /**
     * The colour of the inside of the selection box which is drawn when the user
     * clicks and drags. This is hexadecimal argb.
     */
    private final int SELECTION_FILL = 0x3828b9c8;

    /**
     * The colour of the circle which is drawn to show that a macro is being recorded.
     * This is in hexadecimal argb.
     */
    private final int RECORDING_MACRO = 0xFFDD1111;

    /**
     * The default colour to draw a shape outline.
     * This is in hexadecimal argb.
     */
    private final int INITIAL_DRAWING_OUTLINE = 0xFF000000;

    /**
     * The default colour to fill a shape.
     * This is in hexadecimal argb.
     */
    private final int INITIAL_DRAWING_FILL = 0xFF000000;

    /**
     * This is how many milliseconds to wait between checking the current position
     * of the mouse and updating the selection box to reflect it's new position.
     * An interval of 8ms gives a refresh rate of 125hz which should be sufficient
     * to not be noticeable, assuming the user is using a 60hz or 120hz monitor.
     */
    private final int MOUSE_POLL_INTERVAL_MS = 8;

    /**
     * This is how many milliseconds to wait between blinking the macro recording indicator.
     */
    private final int RECORDING_BLINK_INTERVAL_MS = 800;

    /**
     * This is the size to draw the recording indicator in pixels.
     */
    private final int RECORDING_ICON_SIZE_PX = 15;

    /**
     * This is how far down from the top and how far left from the right of the screen to draw
     * the circle indicating that a macro is being recorded.
     */
    private final int RECORDING_ICON_EDGE_PADDING_PX = 10;

    /**
     * This is the initial size of the stroke for drawing.
     */
    private final int INITIAL_DRAWING_STROKE_SIZE = 1;

    /**
     * This is the minimum amount for the image zoom scale.
     */
    private final double MIN_SCALE = 0.1;

    /**
     * This is the maximum amount for the image zoom scale.
     */
    private final double MAX_SCALE = 10;

    /**
     * This is how much to scale the scale for each scroll unit of the mouse wheel. 
     */
    private final double SCROLL_SCALING_MULTIPLIER = 0.98;

    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;

    /**
     * <p>
     * The zoom-level of the current view.
     * A scale of 1.0 represents actual size; 0.5 is zoomed out to half size; 1.5 is
     * zoomed in to one-and-a-half size; and so forth.
     * </p>
     * 
     * <p>
     * Note that the scale is internally represented as a multiplier, but externally
     * as a percentage.
     * </p>
     */
    private double scale;

    /**
     * The currently selected pixels. In the format {{x1,y1},{x2,y2}} with (x1,y1)
     * as point at mouse click and (x2,y2) as the point at mouse release.
     * Columns of this array are set to null when there is not selection.
     * <b>Be careful when writing to this data field</b> as it is written to by
     * {@link MousePoller} periodically in a multithreaded manner. Ensure that a
     * MousePoller thread is not currently running, otherwise any changes will be
     * promptly overwritten.
     */
    private int[][] selection = new int[2][];
    
    /**
     * This stores the colour object that is created from the
     * {@link SELECTION_OUTLINE} data field in the constructor.
     */
    private Color selectionOutlineColour;

    /**
     * This stores the colour object that is created from the {@link SELECTION_FILL}
     * data field in the constructor.
     */
    private Color selectionFillColour;

    /**
     * This stores the colour object that is created from the {@link RECORDING_MACRO}
     * data field in the constructor.
     */
    private Color recordingMacroColour;

    /*
     * This stores whether the recording icon <i>is</i> currently painted.
     */
    private boolean recordingPainted;

    /**
     * This stores whether the recording icon <i>should</i> currently be painted.
     */
    private boolean paintRecording;

    /*
     * This stores a pointer to the thread which is keeping track of whether the recording icon should
     * be drawn or not. 
     */
    private Thread recordingClock;

    /**
     * This stores the current drawing setting for drawing shapes on the image.
     */
    private DrawingSettings currentDrawingSettings;

    /**
     * <p>
     * Create a new ImagePanel.
     * </p>
     * 
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * </p>
     */
    public ImagePanel() {

        image = new EditableImage();
        scale = 1.0;
        SelectionMouseListener selectionListener = new SelectionMouseListener();
        ScrollMouseListener scrollListener = new ScrollMouseListener();
        addMouseListener(selectionListener);
        addMouseWheelListener(scrollListener);
        selectionOutlineColour = new Color(SELECTION_OUTLINE, true);
        selectionFillColour = new Color(SELECTION_FILL, true);
        recordingMacroColour = new Color(RECORDING_MACRO, true);
        currentDrawingSettings = new DrawingSettings(true, false, new Color(INITIAL_DRAWING_OUTLINE),
                new Color(INITIAL_DRAWING_FILL), INITIAL_DRAWING_STROKE_SIZE);
        recordingPainted = false;
    }

    /**
     * This class listens for mouse events on the ImagePanel instance for the purposes of tracking
     * the selected region of the image.
     */
    private class SelectionMouseListener implements MouseListener {
        Thread mousePollerThread;

        public SelectionMouseListener() {
        }

        public void mousePressed(MouseEvent e) {
            if (!image.hasImage())
                return;
            if (e.getX() < 0 || e.getX() > (int)Math.round(((float)image.getSize()[0]) * scale) || e.getY() < 0 || e.getY() > (int)Math.round(((float)image.getSize()[1]) * scale))
                return;
            // store the coordinates at which the mouse was pressed, as the first point of
            // the current selection
            if (selection[0] == null)
                selection[0] = new int[2];
            selection[0][0] = (int)Math.round(((float)e.getX()) * (1.0f/scale));
            selection[0][1] = (int)Math.round(((float)e.getY()) * (1.0f/scale));
            // spawn a new thread to track the mouse movement and update the selection box,
            // to give the user live feedback on their selection
            if (selection[1] == null)
                selection[1] = new int[2];
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            mousePollerThread = new Thread(new MousePoller(selection,
                    new int[] { e.getX() - ((int) mouseLocation.getX()), e.getY() - ((int) mouseLocation.getY()) }));
            mousePollerThread.start();
        }

        public void mouseReleased(MouseEvent e) {
            // stop the thread from tracking the mouse movement, as the user is now finished
            // doing their selection
            if (mousePollerThread != null)
                mousePollerThread.interrupt();
        }

        public void mouseClicked(MouseEvent e) {
            // clear the user's selection on a single mouse click
            clearSelection();
            repaint();
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * This class awaits the scroll wheel being moved in order to zoom the image
     */
    private class ScrollMouseListener implements MouseWheelListener {

        public void mouseWheelMoved(MouseWheelEvent e) {
            scale *= Math.pow(SCROLL_SCALING_MULTIPLIER, e.getUnitsToScroll());
            scale = Math.min(Math.max(MIN_SCALE, scale), MAX_SCALE);
            repaint();
            getParent().revalidate();
        }
    }

    /**
     * This class is used as a worker thread that is started when the user clicks
     * and drags on the ImagePanel. The mouse position is polled periodically and
     * stored
     * in the {@link selection} data field. As the mouse position changes, the class
     * prompts the ImagePanel to redraw if the mouse moved in order to reflect the
     * new
     * mouse position in the selection box. When the user stops clicking and
     * dragging, this thread is interrupted.
     */
    private class MousePoller implements Runnable {
        // this stores a pointer to the array in which to store the current image
        // selection.
        int[][] selection;
        // the offset is needed because the MouseInfo coordinates are relative to the
        // screen, whereas the MouseEvent coordinates are relative to the window
        int[] offset;
        // stores what region of the ImagePanel was selected last time it was checked.
        int[][] previousSelection;
        // stores the size of the image stored in the ImagePanel for purposes of
        // clipping the selection to the edges of the image
        int[] imageSize;
        // stores the current location of the mouse. This is updated every time the
        // mouse position is polled
        Point mouseLocation;

        public MousePoller(int[][] selection, int[] offset) {
            this.selection = selection;
            this.offset = offset;
            previousSelection = new int[][] { Arrays.copyOf(selection[0], selection[0].length),
                    Arrays.copyOf(selection[1], selection[1].length) };
        }

        public void run() {
            imageSize = getImage().getSize();
            while (!Thread.currentThread().isInterrupted()) {
                mouseLocation = MouseInfo.getPointerInfo().getLocation();
                selection[1][0] = ((int) mouseLocation.getX()) + offset[0];
                selection[1][1] = ((int) mouseLocation.getY()) + offset[1];
                selection[1][0] = (int)Math.round(((float)selection[1][0]) * (1.0f/scale));
                selection[1][1] = (int)Math.round(((float)selection[1][1]) * (1.0f/scale));
                if (selection[1][0] < 0)
                    selection[1][0] = 0;
                if (selection[1][1] < 0)
                    selection[1][1] = 0;
                if (selection[1][0] > imageSize[0])
                    selection[1][0] = imageSize[0];
                if (selection[1][1] > imageSize[1])
                    selection[1][1] = imageSize[1];
                if (!Arrays.equals(selection[1], previousSelection[1])) { // doesn't check index 0 because that's the
                                                                          // first point and we know it won't change
                    previousSelection[1] = Arrays.copyOf(selection[1], selection[1].length);
                    repaint();
                }
                try {
                    Thread.sleep(MOUSE_POLL_INTERVAL_MS);
                } catch (InterruptedException e) {
                    break; // The thread has been interrupted by SelectionMouseListener, meaning the mouse
                           // has been released
                }
            }
        }
    }

    /**
     * This class acts as a clock, oscillating the value of the paintRecording datafield in order
     * to track whether the recording icon should currently be on screen.
     */
    private class RecordingClock implements Runnable {
        public void run() {
            paintRecording = true;
            while (!Thread.currentThread().isInterrupted()) {
                repaint();
                paintRecording = !paintRecording;
                try {
                    Thread.sleep(RECORDING_BLINK_INTERVAL_MS);
                } catch (InterruptedException e) {
                    break;
                }
            }
            paintRecording = false;
            repaint();
        }
    }

    /**
     * <p>
     * Get the currently displayed image
     * </p>
     *
     * @return the image currently displayed.
     */
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc.
     * </p>
     * 
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100 * scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the
     * original size, 50% is half-size, etc.
     * The zoom level is restricted to the range [50, 200].
     * </p>
     * 
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        scale = zoomPercent / 100;
        scale = Math.min(Math.max(MIN_SCALE, scale), MAX_SCALE);
    }

    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     * 
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a
     * default size if no image is present.
     * </p>
     * 
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            // System.out.println(image.getCurrentImage().getWidth() + " " +
            // image.getCurrentImage().getHeight());
            return new Dimension((int) Math.round(image.getCurrentImage().getWidth() * scale),
                    (int) Math.round(image.getCurrentImage().getHeight() * scale));
        } else {
            return new Dimension(450, 450);
        }
    }

    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     * 
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if (image.hasImage()) {
            g2.scale(scale, scale);
            g2.drawImage(image.getCurrentImage(), null, 0, 0);
        }
        if (selection[1] != null) { // if a region is currently selected
            // paint a selection box over it
            int topX = Math.min(selection[0][0], selection[1][0]);
            int topY = Math.min(selection[0][1], selection[1][1]);
            int botX = Math.max(selection[0][0], selection[1][0]);
            int botY = Math.max(selection[0][1], selection[1][1]);
            g2.setColor(selectionOutlineColour);
            g2.drawRect(topX, topY, botX - topX, botY - topY);
            g2.setColor(selectionFillColour);
            g2.fillRect(topX, topY, botX - topX, botY - topY);
        }

        if (paintRecording && !recordingPainted) {
            g2 = (Graphics2D) g.create();
            g2.setColor(recordingMacroColour);
            Dimension size = getSize();
            g2.fillOval((int) size.getWidth() - RECORDING_ICON_EDGE_PADDING_PX - RECORDING_ICON_SIZE_PX,
                    RECORDING_ICON_EDGE_PADDING_PX,
                    RECORDING_ICON_SIZE_PX, RECORDING_ICON_SIZE_PX);
            recordingPainted = true;
        } else {
            recordingPainted = false;
        }
        g2.dispose();
    }

    /**
     * Return coordinates of the currently selected region of the image
     * 
     * @return The coordinates of the currently selected region of the image. {null,
     *         null} if there is no selection.
     */
    public int[][] getSelection() {
        return selection;
    }

    /**
     * Sets the currently selected region to {null, null}, indicating that no region
     * is selected
     */
    public void clearSelection() {
        selection = new int[2][];
    }

    /**
     * Start the recording icon blinking on screen.
     */
    public void startRecordingBlink() {
        recordingClock = new Thread(new RecordingClock());
        recordingClock.start();
    }

    /**
     * Stop the recording icon blinking on screen.
     */
    public void stopRecordingBlink() {
        if (recordingClock != null)
            if (!recordingClock.isInterrupted())
                recordingClock.interrupt();
    }

    /**
     * @return The current drawing settings
     */
    public DrawingSettings getDrawingSettings() {
        return currentDrawingSettings;
    }

    /**
     * Change the current drawing settings
     * @param newSettings The new settings to set as the current drawing settings
     */
    public void setDrawingSettings(DrawingSettings newSettings) {
        currentDrawingSettings = newSettings;
    }

}
