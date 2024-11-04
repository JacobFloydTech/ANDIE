package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * Adjustments to the contrast of an Image
 * </p>
 * 
 * <p>
 * Increments an images Contrast by a specified amount.
 * </p>
 * 
 * @author Jacob
 * @version 1.0
 */

public class BrightnessContrast implements ImageOperation, java.io.Serializable {
    private float brightness;
    private float contrast;

    public BrightnessContrast(int brightness, int contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    public BrightnessContrast() {
        this(0, 0);
    }

    /**
     *  <p>
     *  Applies the changes to a copy of the buffered image.
     *  </p>
     *  
     *  @return The altered BufferedImage.
     *  @param input The image to be altered.
     */
    public BufferedImage apply(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = input;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = input.getRGB(x, y);
                int alpha = (argb & 0xFF000000) >>> 24;
                int r = BrightnessContrastEquation((argb & 0x00FF0000) >> 16);
                int g = BrightnessContrastEquation((argb & 0x0000FF00) >> 8);
                int b = BrightnessContrastEquation((argb & 0x000000FF));
                int newPixel = ((int) alpha << 24 | (int) r << 16) | ((int) g << 8) | (int) b;
                output.setRGB(x, y, newPixel);

            }
        }
        return output;
    }

    /**
     * <p>
     * The equation to accurately alter the image.
     * </p>
     * 
     * @param pixelValue Value of the pixel to alter.
     * @return The new value of the specified pixel.
    */
    private int BrightnessContrastEquation(int pixelValue) {
        double leftHalf = (1 + (this.contrast / 100)) * (pixelValue - 127.5);
        double rightHalf = 127.5 * (1 + (this.brightness / 100));
        int value = (int) (leftHalf + rightHalf);
        value = Math.max(Math.min(255, value), 0);
        return value;
    }

}
