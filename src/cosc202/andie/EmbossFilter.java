package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * Applies an emboss filter to the image
 * </p>
 * 
 * @author Bill and Tim
 * @version 1.0
 */
public class EmbossFilter implements ImageOperation, java.io.Serializable{
    //there are some helper constants to help with readability
    public static int OR_N = 0;
    public static int OR_NE = 1;
    public static int OR_E = 2;
    public static int OR_SE = 3;
    public static int OR_S = 4;
    public static int OR_SW = 5;
    public static int OR_W = 6;
    public static int OR_NW = 7;

    private int orientation;

    /**
     * Initialise the emboss filter with an orientation (0-7)
     * @param orientation The orientation of the emboss
     */
    public EmbossFilter(int orientation){
        this.orientation = orientation;
    }
    
    /**
     * Applies the emboss to the image
     * @param input The image to apply the emboss to
     * @return The image with the emboss applied
     */
    public BufferedImage apply (BufferedImage input) {
        float[][] embossKernels = {
            { 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f },
            { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f },
            { 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f },
            { -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f },
            { 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f },
            { 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f },
            { 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f },
            { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f }
        };
        float[] kernel = embossKernels[orientation];

        return new Convolution().apply(input, kernel, new int[] { 3, 3 }, true);
    }
}
