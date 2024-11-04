package cosc202.andie;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Class to apply a convolution to an image. Redesigned to for the extended filters and negative values tasks
 * 
 * @author Bill Campbell
 * @version 1.0
 */
public class Convolution {
    /**
     * Stores the kernel for the convolution in the case that it was passed into the constructor
     */
    float[] kernel;
    /**
     * Stores the dimensions of the kernel for the convolution in the case that they were passed into the constructor
     * In the format {width, height}
     */
    int[] kernelDimensions;
    
    /**
     * Default constructor, initialises the data fields to null
     */
    public Convolution(){
        this(null,null);
    }

    /**
     * Constructor to give the data fields set values
     * @param kernel The kernel to use for the convolution
     * @param kernelDimensions The dimensions of the passed in kernel
     */
    public Convolution(float[] kernel, int[]kernelDimensions){
        this.kernel = kernel;
        this.kernelDimensions = kernelDimensions;
    }

    /**
     * Apply the convolution to the image, using the information stored in the data fields
     * @param input The image to apply the convolution to
     * @return The output image with the convolution applied
     */
    public BufferedImage apply(BufferedImage input){
        return apply(input, kernel, kernelDimensions, false);
    }

    /**
     * Apply the convolution to the image using the passed in information
     * This may very well break for a kernel of even dimensions.
     *
     * @param input The image to apply the convolution to
     * @param kernel The kernel to use for the convolution
     * @param kernelDimensions The dimensions of the kernel to use for the convolution in the format {width, height}
     * @return The output image with the convolution applied
     */
    public BufferedImage apply(BufferedImage input, float[] kernel, int[] kernelDimensions, boolean expectNegatives){
        if(kernel == null || kernelDimensions == null) return input; //rudimentary check to make sure the method is being used properly
        int iw = input.getWidth(); //image width
        int ih = input.getHeight(); //image height
        int kw = kernelDimensions[0]; //kernel width
        int kh = kernelDimensions[1]; //kernel height
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        Color argb; //holds temporary argb values
        float a,r,g,b; //holds the argb values as they are calculated for each pixel
        int fa,fr,fg,fb; //the final argb values for each pixel
        int kx, ky; //holds the current coordinates of where the kernel overlaid
        float kv; //holds the current value of the kernel that is being handled
        for(int y = 0; y < ih; y++){ //for each row in the image
            for(int x = 0; x < iw; x++){ //for each column in the image
                a = 0; r = 0; g = 0; b = 0; //reset the colour channel counters
                for(int dy = -kh/2; dy <= kh/2; dy++){ //for each row in the kernel
                    for(int dx = -kw/2; dx <= kw/2; dx++){ //for each column in the kernel
                        // calculate where the point on the kernel is overlaid on the image
                        kx = x+dx;
                        ky = y+dy;
                        // clip the point to the outer bounds of the image
                        if(kx < 0) kx = 0;
                        else if(kx >= iw) kx = iw-1;
                        if(ky < 0) ky = 0;
                        else if(ky >= ih) ky = ih-1;
                        //get the colour at the point in question
                        argb = new Color(input.getRGB(kx,ky), true);
                        //get the kernel value which is currently overlaid on the point
                        kv = kernel[(dx+kw/2)+(dy+kh/2)*kh];
                        //multiply the point's channels by the kernel value
                        a += kv * argb.getAlpha();
                        r += kv * argb.getRed();
                        g += kv * argb.getGreen();
                        b += kv * argb.getBlue();
                    }
                }
                //if needed, map -255 - 255 values to 0 - 255
                if(expectNegatives){
                    a = (a + 255)/2;
                    r = (r + 255)/2;
                    g = (g + 255)/2;
                    b = (b + 255)/2;
                }
                //clip the resulting channels from 0-255 and convert them to integers
                fa = Math.max(Math.min(Math.round(a), 255), 0);
                fr = Math.max(Math.min(Math.round(r), 255), 0);
                fg = Math.max(Math.min(Math.round(g), 255), 0);
                fb = Math.max(Math.min(Math.round(b), 255), 0);
                //write the new pixel value to the corresponding location on the output 
                output.setRGB(x, y, new Color(fr,fg,fb,fa).getRGB());
            }
        }
        return output;
    }
    
}
