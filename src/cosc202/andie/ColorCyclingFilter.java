package cosc202.andie;

import java.awt.image.BufferedImage;
import java.util.Hashtable;
/**
 * <p>
 * Cycles the Colour channels of an image
 * Default for an image being RBG, the channels are swapped respectively.
 * </p>
 * 
 * @author Jacob
 * @version 1.0
 */
public class ColorCyclingFilter implements ImageOperation, java.io.Serializable {
    String input;

    /**
     * Constructor to initialize data field
     * @param input The string input from the user
     */
    public ColorCyclingFilter(String input) {
        this.input = input;

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
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = input;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Initialize a hashtable where we can store the color values and then
                // get them based on a key value.
                Hashtable<String, Integer> hashtable = new Hashtable<>();
                // Then get the ARGB value
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >>> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);
                hashtable.put("r", r);
                hashtable.put("g", g);
                hashtable.put("b", b);

                argb = (a << 24)
                        | ((int) hashtable.get(this.input.substring(0, 1)) << 16)
                        | ((int) hashtable.get(this.input.substring(1, 2)) << 8)
                        | (int) hashtable.get(this.input.substring(2, 3));
                // Add these values to the hashtable with the corresponding character value
                // With the user's input, which we know is valid from the actionPerformed()
                // function for this action,
                // We are able to parse the string and get the correct order of color channels
                // by doing a lookup on the hash table.
                // We then have to cast this to an int to correctly store it, generating out new
                // RGB value.
                output.setRGB(x, y, argb);
            }
        }
        return output;
    }
}
