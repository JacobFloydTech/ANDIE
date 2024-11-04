package cosc202.andie;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;

/**
 * <p>
 * Transforms an images size by a certain scale.
 * </p>
 * 
 * @author Tim
 * @version 1.0
 */
public class ScaleTransform implements ImageOperation, java.io.Serializable {
    private double scaleX;
    private double scaleY;

    /**
     * <p>
     * The amount by which to scale the image.
     * </p>
     * 
     * @param scaleX The x-axis scale amount
     * @param scaleY The y-axis scale amount
     */
    public ScaleTransform(int scaleX, int scaleY) {
        this.scaleX = (double) scaleX / 100.0;
        this.scaleY = (double) scaleY / 100.0;

    }

    /**
     * <p>
     * Applies the changes to the specified image.
     * </p>
     * 
     * @param input The image to alter.
     * @return The changed image.
     */
    public BufferedImage apply(BufferedImage input) {
        // Now we times the percentage by the width, giving us the new value
        // Then casting the result of Math.floor() - rounding it down and converting to
        // int
        int newWidth = (int) Math.floor(this.scaleX * input.getWidth());
        int newHeight = (int) Math.floor(this.scaleY * input.getHeight());
        Image output = input.getScaledInstance(newWidth, newHeight, 0);
        BufferedImage outputConversion = new BufferedImage(output.getWidth(null), output.getHeight(null),
                input.getType());
        Graphics2D drawGraphics = outputConversion.createGraphics();
        drawGraphics.drawImage(output, 0, 0, null);
        drawGraphics.dispose();
        return outputConversion;
    }
}
