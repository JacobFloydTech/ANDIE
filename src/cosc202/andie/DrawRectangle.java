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
public class DrawRectangle implements ImageOperation, java.io.Serializable {

    private int x, y, width, height;
    private boolean outline;
    private boolean fill;
    private Color outlineColour;
    private Color fillColour;
    private int strokeSize;
    //Had to remove the following line as it made this object unserializable
    //private DrawingSettings settings;

    /**
     * Constructor for the rectangle.
     * @param x1 Starting x position.
     * @param y1 Starting y position.
     * @param x2 Ending x position.
     * @param y2 Ending y position.
     * @param settings 
     * @see DrawingSettings
     */
    public DrawRectangle(int x1, int y1, int x2, int y2, DrawingSettings settings) {
        this.x = Math.min(x1,x2);
        this.y = Math.min(y1,y2);
        width = Math.max(x1,x2) - Math.min(x1,x2);
        height = Math.max(y1,y2) - Math.min(y1,y2);
        outline = settings.outline;
        fill = settings.fill;
        outlineColour = settings.outlineColour;
        fillColour = settings.fillColour;
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
        g2d.setStroke(new BasicStroke(strokeSize));
        if(outline) {
            g2d.setColor(outlineColour);
            g2d.drawRect(x, y, width, height);
        }
        if(fill) {
            g2d.setColor(fillColour);
            g2d.fillRect(x, y, width, height);
        }
        g2d.dispose();
        return input;
    }
}