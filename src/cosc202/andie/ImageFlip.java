package cosc202.andie;

import java.awt.image.BufferedImage;


/**
 * <p>
 * Image operation to flip the image horizontally or vertically.
 * </p>
 * 
 * @author Tim
 **/

public class ImageFlip implements ImageOperation, java.io.Serializable {

    /** Flag to determine if horizontal flip or vertical flip */
    private boolean horizontal;

    /**
     * <p>
     * Create a new ImageFlip operation.
     * </p>
     *
     * @param horizontal true for horizontal flip, false for vertical flip
     */
    public ImageFlip(boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * <p>
     * Apply the flipping operation to the image.
     * </p>
     *
     * @param input the image to be flipped
     * @return the flipped image
     */
    public BufferedImage apply(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, input.getType());
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalX = horizontal ? width - 1 - x : x;
                int originalY = horizontal ? y : height - 1 - y;
                int rgb = input.getRGB(originalX, originalY);
                flippedImage.setRGB(x, y, rgb);
            }
        }
    
        return flippedImage;
    }
    
}