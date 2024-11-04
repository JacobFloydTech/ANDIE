package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * Rotates an image by 90, 180 or 270 degrees.
 * </p>
 * 
 * @author Tim
 * @version 1.0
 */
public class RotateTransform implements ImageOperation, java.io.Serializable {
    private int rotation;

    public RotateTransform(int rotation) {
        this.rotation = rotation;
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

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output;
        // If the user is rotating by 90 or 270 degrees, the dimensions of the new image
        // have to be changed
        // Meaning that width = height, height = width.
        if (this.rotation == 90 || this.rotation == 270) {
            output = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
        } else {
            output = input;
        }

        if (this.rotation == 90) {

            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    // For every pixel on the screen, set it with the coordinates flipped
                    // e.g the y goes in the x place, and the x goes in the y place
                    int rgb = input.getRGB(x, y);
                    output.setRGB(y, input.getWidth() - x - 1, rgb);
                }
            }
        } else if (this.rotation == 270) {

            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    int rgb = input.getRGB(x, y);
                    output.setRGB(input.getHeight() - y - 1, x, rgb);
                }
            }
        } else if (this.rotation == 180) {
            for (int y = 0; y < input.getHeight() / 2; y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    // We are looping only through half of the columns to not overwrite
                    // existing pixels we have already modified
                    // This is because for every pixel, we are setting 2,
                    // by swapping them around.
                    int topPixelRGB = input.getRGB(x, y);
                    int bottomPixelRGB = input.getRGB(x, input.getHeight() - 1 - y);

                    output.setRGB(x, y, bottomPixelRGB);
                    output.setRGB(x, input.getHeight() - 1 - y, topPixelRGB);
                }
            }

        }
        return output;
    }
}
