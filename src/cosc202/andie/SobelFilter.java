package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * Object to apply a Sobel filter to an image
 * 
 * @author Bill and Tim
 */
public class SobelFilter implements ImageOperation, java.io.Serializable {
    /** Helper constant to help with readability */
    public static boolean HORIZONTAL = false;
    public static boolean VERTICAL = true;

    private boolean orientation;

    /** 
     * Constructor 
     * @param orientation The orientation of the sobel filter
     */
    public SobelFilter(boolean orientation){
        this.orientation = orientation;
    }

    /**
     * <p>
     * Apply the sobel filter to the passed in image
     * </p>
     * @param input The image to apply the sobel filter to
     * @return The image with the sobel filter applied
     */
    public BufferedImage apply(BufferedImage input) {
        float[] hKernel = {
            -0.5f, 0.0f, 0.5f,
            -1.0f, 0.0f, 1.0f,
            -0.5f, 0.0f, 0.5f
        };
        float[] vKernel = {
            -0.5f, -1.0f, -0.5f,
            0.0f, 0.0f, 0.0f,
            0.5f, 1.0f, 0.5f
        };
        return new Convolution().apply(input, (orientation == HORIZONTAL ? hKernel : vKernel), new int[] { 3, 3 }, true);
    }
}