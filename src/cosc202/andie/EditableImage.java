package cosc202.andie;

import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.imageio.*;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original image 
 * and the result of applying the current set of operations to it. 
 * The operations themselves are stored on a {@link Stack}, with a second {@link Stack} 
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Bill, Blake, Jacob and Tim
 * @version 1.0
 */
class EditableImage {

    /** The amount of pixels to offset the image from the top left when it is printed */
    private final int PRINT_MARGIN_WIDTH = 10;

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** This is where the memory address of the last op that was saved is stored */
    private ImageOperation currentlySavedOp;
    /** Stores the memory address of the operation which should precede the beginning of the macro (not inclusive) */
    private ImageOperation macroStart;
    /** Stores the memory address of the operation which should end the macro (inclusive) */
    private ImageOperation macroEnd;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;

    /**
     * Used for printing the image.
     * Draws the image with a passed in graphics object.
     */
    private class PrintableForm implements Printable{
        public int print(Graphics g, PageFormat pf, int page) {
            if(page > 0) return NO_SUCH_PAGE;
            Graphics2D g2d = (Graphics2D)g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            g2d.drawImage(current, null, PRINT_MARGIN_WIDTH, PRINT_MARGIN_WIDTH);
            return PAGE_EXISTS;
        }
    }

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        imageFilename = null;
        opsFilename = null;
        macroStart = null;
        macroEnd = null;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage. 
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so the 
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knowledge of some details about the internals of the BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to 
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code> added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * <p>
     * If the passed in filePath ends in ".ops" then the file will be treated as a macro
     * </p>
     * 
     * @param filePath The file to open the image from.
     * @throws Exception If something goes wrong.
     */
    public void open(String filePath) throws Exception {
        if("ops".equals(extractExtension(filePath))){
            loadMacro(filePath);
            return;
        }
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        Path path = Path.of(imageFilename);
        File imageFile = path.toFile();
        original = ImageIO.read(imageFile);

        System.out.println(imageFile.toString());
        System.out.println(original.toString());
    
        current = deepCopy(original);

        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            ops = opsFromFile;
            redoOps.clear();
            objIn.close();
            fileIn.close();
            currentlySavedOp = (ops.empty() ? null : ops.peek());
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
            ops.clear();
            redoOps.clear();
        }
        this.refresh();
    }

    /**
     * <p>
     * Loads a macro from a given file path.
     * </p>
     * @param filePath The path to the macro file
     * @throws Exception If any issues arose from opening the macro
     */
    public void loadMacro(String filePath) throws Exception{ //throws an exception when the selected file is not a .ops file
        if(!"ops".equals(extractExtension(filePath))) throw new Exception();
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream objIn = new ObjectInputStream(fileIn);

        @SuppressWarnings("unchecked")
        Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();

        Stack<ImageOperation> tempStack = new Stack<ImageOperation>();
        while(!opsFromFile.empty()) tempStack.push(opsFromFile.pop());
        if(ops == null) ops = new Stack<ImageOperation>();
        while(!tempStack.empty()) ops.push(tempStack.pop());
        redoOps.clear();
        objIn.close();
        fileIn.close();
        currentlySavedOp = (ops.empty() ? null : ops.peek());
        this.refresh();
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @throws Exception If something goes wrong.
     */
    public void save() throws Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
        ImageIO.write(original, extension, new File(imageFilename));
        // Write operations file
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
        currentlySavedOp = (ops.empty() ? null : ops.peek());
    }


    /**
     * <p>
     * Save an image to a specified file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws Exception {
        final int CHARACTER_NOT_FOUND = -1;
        
        // Write image file based on file extension
        int extensionSeparatorIndex = imageFilename.lastIndexOf(".");
        if (extensionSeparatorIndex == 0) throw new Exception(); //if a period is the first character, throw an error
        String originalExtension = this.imageFilename.substring(1+this.imageFilename.lastIndexOf(".")).toLowerCase();
        String filename, extension;
        if(extensionSeparatorIndex == CHARACTER_NOT_FOUND) { //if no extension is specified, add the current one
            extension = originalExtension;
            filename = imageFilename + "." + originalExtension;
        }
        else{ //otherwise extract the extension
            filename = imageFilename;
            extension = imageFilename.substring(1+extensionSeparatorIndex).toLowerCase();
            if (!extension.equals(originalExtension)) throw new Exception(); // make sure that they have asked for the same extension
        }
        this.imageFilename = filename;
        this.opsFilename = filename + ".ops";
        save();
    }

    /**
     * <p>
     * Export an image to file.
     * </p>
     * 
     * <p>
     * Exports the image to a new file.
     * Does not save a set of operations from the file with <code>.ops</code> added, instead 
     * exports the image as it appears on the screen to a new image file.
     * </p>
     * 
     * @throws Exception If something goes wrong.
     */
    public void export(String imageFilename) throws Exception {
        final int CHARACTER_NOT_FOUND = -1;
        
        // Write image file based on file extension
        int extensionSeparatorIndex = imageFilename.lastIndexOf(".");
        if (extensionSeparatorIndex == 0) throw new Exception(); //if a period is the first character, throw an error
        String originalExtension = this.imageFilename.substring(1+this.imageFilename.lastIndexOf(".")).toLowerCase();
        String filename, extension;
        if(extensionSeparatorIndex == CHARACTER_NOT_FOUND) { //if no extension is specified, add the current one
            extension = originalExtension;
            filename = imageFilename + "." + originalExtension;
        }
        else{ //otherwise extract the extension
            filename = imageFilename;
            extension = imageFilename.substring(1+extensionSeparatorIndex).toLowerCase();
            if (extension != originalExtension) throw new Exception(); // make sure that they have asked for the same extension
        }
        
        ImageIO.write(current, extension, new File(filename));
    }

    /**
     * <p>
     * Prints the current state of the image to a printer
     * </p>
     * 
     * @throws Exception If something went wrong with printing
     */
    public void print() throws Exception {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new PrintableForm());
        if(job.printDialog()) job.print();
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        current = op.apply(current);
        ops.push(op);
        redoOps.clear();
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() {
        if(ops.empty()) return; //Don't allow the user to undo from an empty stack
        redoOps.push(ops.pop());
        refresh();
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo()  {
        if(redoOps.empty()) return; //Don't allow the user to redo from an empty stack
        ImageOperation op = redoOps.pop();
        current = op.apply(current);
        ops.push(op); 
    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * @return true if the last saved operation is the last done operation, otherwise false
     */
    public boolean isSaved(){
        return currentlySavedOp == (ops.empty() ? null : ops.peek());
    }

    /**
     * @return The size of the current image as an int array of format {width, height}, or null if no image is present
     */
    public int[] getSize(){
        return (hasImage() == true ? new int[]{ current.getWidth(), current.getHeight() } : null);
    }

    /**
     * Start recording a macro
     */
    public void startMacro(){
        macroStart = (ops.empty() ? null : ops.peek());
        macroEnd = null;
    }

    /**
     * Stop recording a macro
     */
    public void endMacro(){
        macroEnd = (ops.empty() ? null : ops.peek());
    }

    /**
     * Cancel recording a macro
     */
    public void cancelMacro(){
        macroStart = null;
        macroEnd = null;
    }

    /**
     * Save the current macro to a file
     * @param macroFilename The filename to save the macro to
     * @throws Exception If anything went wrong with saving the macro to a file
     */
    public void saveMacro(String macroFilename) throws Exception {
        final int CHARACTER_NOT_FOUND = -1;
        
        int extensionSeparatorIndex = macroFilename.lastIndexOf(".");
        if (extensionSeparatorIndex == 0) throw new Exception(); //if a period is the first character, throw an error
        String filename, extension;
        if(extensionSeparatorIndex == CHARACTER_NOT_FOUND) { //if no extension is specified, add the current one
            filename = macroFilename + ".ops";
        }
        else{ //otherwise make sure it is "ops"
            filename = macroFilename;
            extension = imageFilename.substring(1+extensionSeparatorIndex).toLowerCase();
            if (extension != "ops") throw new Exception();
        }
        Stack<ImageOperation> macroOperations = generateMacro();
        if(macroOperations == null) throw new Exception();
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(macroOperations);
        objOut.close();
        fileOut.close();
    }

    /**
     * Generates a stack of operations from the current states of the macroStart and macroEnd datafields.
     * @return A stack containing the operations in the macro, or null if the macro could not be created
     */
    private Stack<ImageOperation> generateMacro(){
        @SuppressWarnings("unchecked")
        Stack<ImageOperation> opsCopy = (Stack<ImageOperation>)ops.clone();
        Stack<ImageOperation> macroOperations = new Stack<ImageOperation>();
        Stack<ImageOperation> tempStack = new Stack<ImageOperation>();
        if(macroEnd == null) endMacro();
        if(macroEnd == null) return null;
        ImageOperation currentOp;
        do{
            currentOp = (opsCopy.empty() ? null : opsCopy.pop());
            if(currentOp == null){
                return null; //could not find end
            }
        }while(currentOp != macroEnd);
        do{
            tempStack.push(currentOp);
            currentOp = (opsCopy.empty() ? null : opsCopy.pop());
        }while(currentOp != macroStart && currentOp != null);
        if(currentOp == null && macroStart != null) return null; //could not find start
        while(!tempStack.empty()) macroOperations.push(tempStack.pop());
        return macroOperations;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in sequence.
     * This is useful when undoing changes to the image, or in any other case where {@link current}
     * cannot be easily incrementally updated. 
     * </p>
     */
    private void refresh()  {
        current = deepCopy(original);
        for (ImageOperation op: ops) {
            current = op.apply(current);
        }
    }

    /**
     * Helped method to retrieve the extension of a filename, if present
     * @param filename The filename to use
     * @return The extension of the filename or null if there was none
     */
    private String extractExtension(String filename){
        int extensionSeparatorIndex = filename.lastIndexOf(".");
        if (extensionSeparatorIndex == 0) return null; //if a period is the first character
        return filename.substring(1+extensionSeparatorIndex).toLowerCase();
    }
}
