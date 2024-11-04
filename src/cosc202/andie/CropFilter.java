package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * Filter to crop an image to a specified size.
 * </p>
 * 
 * @author Jacob and Bill
 * @version 1.0
 */
public class CropFilter implements ImageOperation {
    private int[][] selection;

    public CropFilter(int[][] selection) {
        this.selection = selection;
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
        int startX = Math.min(selection[0][0], selection[1][0]);
        int endX = Math.max(selection[0][0], selection[1][0]);
        int startY = Math.min(selection[0][1], selection[1][1]);
        int endY = Math.max(selection[0][1], selection[1][1]);

        int differenceX = endX - startX;
        int differenceY = endY - startY;
        BufferedImage output = new BufferedImage(differenceX, differenceY, BufferedImage.TYPE_INT_ARGB);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int argb = input.getRGB(x, y);
                // Normalize x and y to start to 0,0
                output.setRGB(x - startX, y - startY, argb);
            }
        }
        return output;

    }
}
