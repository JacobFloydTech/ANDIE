package cosc202.andie;

import java.awt.image.BufferedImage;
//import java.awt.image.Kernel;
import java.util.Arrays;

/**
 * @author Blake Cooper
 * 
 * <p>
 * ImageOperation to apply noise reduction (median filter) to an image 
 * </p>
 * 
 * <p>
 * Median filters take the median of the rgb values of surrounding pixels and set the central value
 * to mean, reducing "noise" in an image
 * </p>
 * 
 */
public class MedianFilter implements ImageOperation, java.io.Serializable {
    
    //The radius of the center of the 2d array mask
    private int radius;

    /**
     * Constructor for the median filter object
     */
    MedianFilter(int radius){
        this.radius = radius;
    }

    /**
     * Apply a median filter to the passed in image.
     * @param input The image to apply the filter to.
     * @return The image with noise reductions.
     */
    public BufferedImage apply(BufferedImage input) {
        
        //assign height and width variables from the given image
        int width = input.getWidth();
        int height = input.getHeight();

        //create a copy of the buffered image to output
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        
        //set the image filter size taken from user
        int filterSize = (2 * radius + 1);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                
                // Get the color values in the filter window for each channel
                int[][] alphaChannel = new int[filterSize][filterSize];
                int[][] redChannel = new int[filterSize][filterSize];
                int[][] greenChannel = new int[filterSize][filterSize];
                int[][] blueChannel = new int[filterSize][filterSize];

                //iterate through the array and assign the colour channels
                for (int i = -filterSize / 2; i <= filterSize / 2; i++) {
                    for (int j = -filterSize / 2; j <= filterSize / 2; j++) {
                        int pixelX = x + i;
                        int pixelY = y + j;

                        // Check if the pixel is within the image size
                        if (pixelX >= 0 && pixelX < width && pixelY >= 0 && pixelY < height) {
                            int pixel = input.getRGB(pixelX, pixelY);
                            alphaChannel[i + filterSize / 2][j + filterSize / 2] = (pixel >>> 24) & 0xFF;
                            redChannel[i + filterSize / 2][j + filterSize / 2] = (pixel >> 16) & 0xFF;
                            greenChannel[i + filterSize / 2][j + filterSize / 2] = (pixel >> 8) & 0xFF;
                            blueChannel[i + filterSize / 2][j + filterSize / 2] = pixel & 0xFF;
                        }
                    }
                }
                // Sort the pixel values and get the median
                int medianAlpha = getMedian(alphaChannel);
                int medianRed = getMedian(redChannel);
                int medianGreen = getMedian(greenChannel);
                int medianBlue = getMedian(blueChannel);

                // assign the 'corrected' colour pixels within the mask to the radius
                int outputPixel = (medianAlpha << 24) | (medianRed << 16) | (medianGreen << 8) | medianBlue;
                output.setRGB(x, y, outputPixel);
            }
        }
        return output;
    }
    
    
    /**
     * <p>
     * This was written with guidance from chatGPT 
     * </p>
     * 
     * <p>
     * Method to sort and flatted a 2d array of colour values and get the median value.
     * </p>
     * @param array the colour chanel array to sort
     * @return the median of the sorted array.
     */
    private static int getMedian(int[][] array) {
        //instantiate new arrays 
        int[] flattenedArray = new int[array.length * array[0].length];
        int index = 0;
        
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                flattenedArray[index++] = array[i][j];
            }
        }
        //sort and return median
        Arrays.sort(flattenedArray);
        int middle = flattenedArray.length / 2;
        return (flattenedArray.length % 2 == 0) ? (flattenedArray[middle - 1] + flattenedArray[middle]) / 2 : flattenedArray[middle];
    }

}


