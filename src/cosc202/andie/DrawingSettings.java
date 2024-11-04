package cosc202.andie;

import java.awt.Color;

/**
 * <p>
 * Settings class for all drawing functions
 * </p>
 * 
 * @see DrawLine
 * @see DrawOval
 * @see DrawRectangle
 * 
 * @author Bill Campbell
 */
public class DrawingSettings {
    public boolean outline;
    public boolean fill;
    public Color outlineColour;
    public Color fillColour;
    public int strokeSize;

    /**
     * <p>
     * Instantiate based on passed in parameters.
     * </p>
     * 
     * @param outline whether to draw an outline
     * @param fill whether to fill the shape
     * @param outlineColour what colour to draw the outline
     * @param fillColour what colour to fill the shape
     * @param strokeSize what width of stroke to use for the outline
     */
    public DrawingSettings(boolean outline, boolean fill, Color outlineColour, Color fillColour, int strokeSize){
        this.outline = outline;
        this.fill = fill;
        this.outlineColour = outlineColour;
        this.fillColour = fillColour;
        this.strokeSize = strokeSize;
    }

    /**
     * <p>
     * Instantiate based on passed a pre-existing DrawingSettings object.
     * </p>
     * 
     * @param original The drawing settings object to copy
     */
    public DrawingSettings(DrawingSettings original){
        this.outline = original.outline;
        this.fill = original.fill;
        this.outlineColour = original.outlineColour;
        this.fillColour = original.fillColour;
        this.strokeSize = original.strokeSize;

    }
}
