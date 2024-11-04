package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * @author Blake Cooper
 * 
 * <p>
 * ImageOperation to apply a block averaging filter to an image 
 * </p>
 * 
 * <p>
 * Block Averaging takes a x * y area and sets the Arbg value to the average, pixelating an image.
 * </p>
 * 
 */
public class BlockAveragingFilter implements ImageOperation, java.io.Serializable  {

    //Size of the block to average, not given as a square number as can be rectangular.
    private int blockHeight;
    private int blockWidth;

    /***
     * <p>
     * Constructor for the Block Averaging class.
     * </p>
     * @param blockHeight The height (y value) of the block to average.
     * @param blockWidth The width (x value) of the block to average.
    */
    BlockAveragingFilter(int blockHeight, int blockWidth){
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
    }

    /**
     * <p>
     * Applies the block averaging filter to a clones buffered image.
     * </p>
     * @return BufferedImage the cloned image with the filter applied.
     * @param input the Image in which to apply the filter.
     */
    public BufferedImage apply(BufferedImage input) {
        
        // assign image height and width variables.
        int imageHeight = input.getHeight();
        int imageWidth = input.getWidth();

        //image to output and change.
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

        for (int y = 0; y < imageHeight; y += blockHeight) { // Skip to the next block vertically.
            for (int x = 0; x < imageWidth; x += blockWidth) { // Skip to the next block horizontally.
                // Establish sums and totals for the average calculation.
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0; 
                int sumTrans = 0;
                int pixelCount = 0;

                // Calculate the sum of values in the block.
                for (int blockY = y; blockY < Math.min(y + blockHeight, imageHeight); blockY++) {
                    for (int blockX = x; blockX < Math.min(x + blockWidth, imageWidth); blockX++) {
                        int Argb = input.getRGB(blockX, blockY);
                        //total the rgb values within the block.
                        sumTrans += (Argb >> 24) & 0xFF;  
                        sumRed += (Argb >> 16) & 0xFF; 
                        sumGreen += (Argb >> 8) & 0xFF; 
                        sumBlue += (Argb) & 0xFF; 
                        pixelCount++;
                    }
                }

                // Calculate the average pixel value in the block.
                int averageRed = sumRed / pixelCount;
                int averageGreen = sumGreen / pixelCount;
                int averageBlue = sumBlue / pixelCount;
                int averageTrans = sumTrans / pixelCount;
                int averageRGB = (averageTrans << 24) | (averageRed << 16) | (averageGreen << 8) | averageBlue; // assign the average, (0xFF <<24) accounts for transparency.

                // Fill the block with the average.
                for (int blockY = y; blockY < Math.min(y + blockHeight, imageHeight); blockY++) {
                    for (int blockX = x; blockX < Math.min(x + blockWidth, imageWidth); blockX++) {
                        output.setRGB(blockX, blockY, averageRGB); // set rgb works within the scope of x and y.
                    }
                }
            }
        }
        return output;
    }
}
