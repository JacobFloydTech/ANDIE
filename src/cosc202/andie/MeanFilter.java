package cosc202.andie;

import java.awt.Color;
import java.awt.image.*;
import java.util.*;

/**
 * <p>
 * ImageOperation to apply a Mean (simple blur) filter.
 * </p>
 * 
 * <p>
 * A Mean filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighborhood, and can be implemented by a
 * convolution.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills and Bill Campbell
 * @version 1.0
 */
public class MeanFilter implements ImageOperation, java.io.Serializable {

    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a
     * 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Mean filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MeanFilter
     */
    MeanFilter(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Construct a Mean filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Mean filter has radius 1.
     * </p>
     * 
     * @see MeanFilter(int)
     */
    MeanFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Mean filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Mean filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.
     * Larger radii lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Mean filter to.
     * @return The resulting (blurred)) image.
     */
    public BufferedImage apply(BufferedImage input) {
        int size = (2 * radius + 1) * (2 * radius + 1);
        float[] kernel = new float[size];
        Arrays.fill(kernel, 1.0f / size);
        return new Convolution().apply(input, kernel, new int[]{2*radius+1,2*radius+1}, false);
    }

    public BufferedImage test(BufferedImage input) {
        BufferedImage output = input;
        int width = input.getWidth();
        int height = input.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sumRed = 0, sumBlue = 0, sumGreen = 0, sumAlpha = 0;
                int count = 0;
                for (int offsetY = -radius; offsetY <= radius; offsetY++) {
                    for (int offsetX = -radius; offsetX <= radius; offsetX++) {
                        int newY = Math.min(Math.max(y + offsetY, 0), height - 1);
                        int newX = Math.min(Math.max(x + offsetX, 0), width - 1);
                        Color color = new Color(input.getRGB(newX, newY));
                        sumRed += color.getRed();
                        sumBlue += color.getBlue();
                        sumGreen += color.getGreen();
                        sumAlpha += color.getAlpha();
                        count++;
                    }
                }
                sumRed /= count;
                sumBlue /= count;
                sumGreen /= count;
                sumAlpha /= count;
                int newPixel = ((int) sumAlpha << 24) | ((int) sumRed << 16) | ((int) sumGreen << 8) | (int) sumBlue;
                output.setRGB(x, y, newPixel);
            }
        }
        return output;
    }

}
