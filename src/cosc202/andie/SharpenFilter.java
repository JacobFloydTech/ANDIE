package cosc202.andie;

import java.awt.image.BufferedImage;;


/**
 * <p>
 * A filter to sharpen an image.
 * </p>
 * 
 * @author Jacob
 * @version 1.0
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {

    private double input;

    /**
     * <p>
     * Constructor of the Sharpening filter.
     * </p>
     * 
     * <p>
     * Sets the intensity of the filter.
     * </p>
     * 
     * @param input the 'intensity' of the sharpening.
     */
    public SharpenFilter(int input) {
        double x = (double) input;
        this.input = x * 0.2 > 1 ? x * 0.2 : 1.0;
    }

    /** Default constructor */
    public SharpenFilter() {
        // Call the constructor with parameter and pass a default value
        this(1);
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
        float[] kernelData = {
                0.0f, -0.5f, 0.0f,
                -0.5f, 3.0f, -0.5f,
                0.0f, -0.5f, 0.0f
        };
        // Multiply the matrix by the user's input, affecting the strength of the filter
        for (var i = 0; i < kernelData.length; i++) {
            kernelData[i] *= this.input;
        }

        // Specify the dimensions based on the size of the kernel matrix (3x3)
        int kernelWidth = 3;
        int kernelHeight = 3;

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null),
                input.isAlphaPremultiplied(), null);
        output = new Convolution().apply(input, kernelData, new int[]{kernelWidth,kernelHeight}, false);

        return output;
    }
}
