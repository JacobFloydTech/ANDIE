package cosc202.andie;

import java.awt.image.*;
/**
 * <p>
 * A gaussian filter to alter a given image.
 * </p>
 * 
 * @author Jacob
 * @version 1.0
 */
public class GaussianFilter implements ImageOperation, java.io.Serializable {
    private int input;

    /**
     * Constructor with parameter, setting the data field input to the parameter
     */
    public GaussianFilter(int input) {
        this.input = input;
    }

    /**
     * Default constructed which sets input to 1
     */
    public GaussianFilter() {
        this(1);
    }
    
    /**
     * Apply the gaussian filter to an image
     * @param input The image to apply the gaussian filter to
     * @return The image with the gaussian filter applied
     */
    public BufferedImage apply(BufferedImage input) {
        return new Convolution().apply(input, calculateGaussianKernel(this.input), new int[]{3,3}, false);
    }

    /**
     * Calculates the kernel needed for the gaussian filter based on the specified sigma value
     * @param sigma The sigma value to use for the calculation
     * @return The kernel to use for the gaussian filter
     */
    private float[] calculateGaussianKernel(int sigma) {
        // Create a new array of length 9 that will then be applied
        // in a kernel as a 3x3 matrix
        int size = 3;
        float[] kernelData = new float[size * size];
        int center = size / 2;
        float sum = 0;
        for (int i = 0; i < kernelData.length; i++) {
            // For every cell in the array, calculate the relative
            // x and y positions from the center, making it appear as a
            // 2d array
            int x = i % size - center;
            int y = i / size - center;
            // Using the center calculations, we can begin to form the gaussian
            // distribution by passing in x,y, and the sigma parameter provided by the user

            float data = GuassianBlurCalculation(x, y, sigma);
            kernelData[i] = data;
            sum += data;
        }
        // If the sigma becomes too large at a certain point, it affects the brightness
        // as a result of all the values in the array not adding up to one
        // Therefore, we need to normalize the array by dividing every value
        // by the total sum
        for (int x = 0; x < kernelData.length; x++) {
            kernelData[x] /= sum;
        }
        return kernelData;
    }

    /**
     * Calculate the value of a cell based on x,y,sigma using the equation
     * provided in the lab book
     * @param x The x value to use
     * @param y The y value to use
     * @param sigma The sigma value to use
     * @return the value to the cell
     */
    private float GuassianBlurCalculation(int x, int y, int sigma) {
        double base = (1 / (2 * Math.PI * (sigma * sigma))) * Math.E;
        double exponent = (x * x + y * y) / (2 * (sigma * sigma)) * -1;
        return (float) (base * Math.exp(exponent));
    }

}
