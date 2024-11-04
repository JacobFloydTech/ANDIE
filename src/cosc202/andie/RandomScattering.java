package cosc202.andie;

import java.awt.image.*;
import java.util.Random;

/**
 * <p>
 * Image operation to apply random scattering effect.
 * </p>
 * 
 * @author Tim
 **/
public class RandomScattering implements ImageOperation, java.io.Serializable {

    private int radius;

    /**
     * <p>
     * Create a new RandomScattering operation with the given radius.
     * </p>
     *
     * @param radius The radius of scattering.
     */
    public RandomScattering(int radius) {
        this.radius = radius;
    }

    /**
     * <p>
     * Apply the random scattering effect to the image.
     * </p>
     *
     * @param input The input image.
     * @return The image after applying random scattering.
     */
    public BufferedImage apply(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage scatteredImage = new BufferedImage(width, height, input.getType());
        Random rand = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newX = x + rand.nextInt(2 * radius + 1) - radius;
                int newY = y + rand.nextInt(2 * radius + 1) - radius;
                // Ensure new coordinates are within the image bounds
                newX = Math.max(0, Math.min(newX, width - 1));
                newY = Math.max(0, Math.min(newY, height - 1));
                int rgb = input.getRGB(newX, newY);
                scatteredImage.setRGB(x, y, rgb);
            }
        }
        return scatteredImage;
    }
}