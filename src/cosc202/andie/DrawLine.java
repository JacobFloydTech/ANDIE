package cosc202.andie;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

/**
 * <p>
 * Draw a line onto an image.
 * </p>
 * 
 * @author Tim and Bill
 * @version 1.0
 */
public class DrawLine implements ImageOperation, java.io.Serializable {

    private int x1, y1, x2, y2;
    private Color colour;
    private int strokeSize;
    //Had to remove the following line as it made this object unserializable
    //private DrawingSettings settings;

    /**
     * Constructor for the line.
     * @param x1 Starting x position.
     * @param y1 Starting y position.
     * @param x2 Ending x position.
     * @param y2 Ending y position.
     * @param settings 
     * @see DrawingSettings
     */
    public DrawLine(int x1, int y1, int x2, int y2, DrawingSettings settings) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        colour = settings.fillColour;
        strokeSize = settings.strokeSize;
    }

    /**
     * <p>
     * Applies the changes to the specified image.
     * </p>
     * 
     * @param input The image to alter.
     * @return The changed image.
     */
    @Override
    public BufferedImage apply(BufferedImage input) {
        Graphics2D g2d = input.createGraphics();
        g2d.setColor(colour);
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.drawLine(x1, y1, x2, y2);
        g2d.dispose();
        return input;
    }
}