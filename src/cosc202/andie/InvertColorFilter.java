package cosc202.andie;

import java.awt.image.*;


/**
 * <p>
 * Invert the RGB colour channels of an image.
 * </p>
 * 
 * @author Jacob and Bill
 */
public class InvertColorFilter implements ImageOperation, java.io.Serializable {
    public InvertColorFilter() {
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
        BufferedImage output = input;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >>> 24;
                int r = 255 - ((argb & 0x00FF0000) >> 16);
                int g = 255 - ((argb & 0x0000FF00) >> 8);
                int b = 255 - (argb & 0x000000FF);
                int newPixel = ((int) a << 24) | ((int) r << 16) | ((int) g << 8) | (int) b;
                output.setRGB(x, y, newPixel);
            }
        }
        return output;
    }
}
