# ZSHadow Wizard Money Gang's fork of ANDIE

## Contribution Attribution
*Names here are in rough order of size of contribution, from most to least*  
|Item|Contributor(s)|
|---|---|
|*Sharpen Filter*|Jacob|
|*Gaussian Blur Filter*|Jacob|
|*Median Filter*|Blake|
|*Image Inversion*|Jacob|
|*Colour Channel Cycling*|Jacob|
|*Multilingual Support*|Blake, with some extension upon Blake's initial work done by Jacob and then the remainder of the translations completed and then organized by Bill to accommodate newly added items|
|*Image Resize*|Tim|
|*Image Rotations*|Jacob|
|*Image Flip*|Tim|
|*Image Export*|Bill|
|*Exception Handling*|Bill|
|*Other Error Avoidance*|Bill, Blake, Jacob and Tim (order is alphabetical, not an indication of size of contribution)|
|*Extended Filters*|Bill|
|*Filters with Negative Results*|Bill|
|*Emboss and Edge Detection Filters*|Tim, Bill|
|*Brightness Adjustment*|Jacob|
|*Contrast Adjustment*|Jacob|
|*Block Averaging*|Blake|
|*Random Scattering*|Tim|
|*Toolbar for Common Operations*|Blake, Bill|
|*Keyboard Shortcuts*|Blake, Jacob, Bill|
|*Mouse Selection of Rectangular Regions*|Bill|
|*Crop to Selection*|Jacob|
|*Drawing Functions*|Tim, Bill|
|*Macros for Record and Replay of Operations*|Bill|
|*Show Us Something*|Bill, Jacob|
|*README*|Bill, Tim|

## How the code was tested
Our code has been tested by hand, not using unit tests. There are two types of testing that have been carried out:

### Intra-program Testing
This type of testing is testing to ensure that the program does not cause exceptions by carrying out regular behavior.
Each member of the team is responsible for carrying out these tests on the code that they contribute the the codebase.
Types of tests that this includes:
- Ensuring that code is syntactically correct through use of the compiler and linter.
- Closely reading code to make sure that it semantically makes sense - the right classes, methods, variables and types are being used in order to process data in a way that is expected.
- Using breakpoints and other debugger features to investigate the internal state of the program as it executes.

### User Interaction Testing
This type of testing is testing to ensure that the user cannot cause exceptions in their interactions with the program.
Bill is responsible for carrying out these tests, as we believe it comes under the *Exception Handling* and *Other Error Avoidance* tasks which he is assigned to. 
Types of tests that this includes:
- Entering negative, decimal or zero values where only natural numbers are expected.
- Entering invalid strings where specific strings are expected (specifically in the colour cycle filter).
- Trying to use actions which should not work when there is no open image, when there is no open image (eg. filters, save or export).
- Trying to open file types which are not images or are unsupported image formats.
- Trying to save images with filenames which should not work.
- Using operations together that may cause weird behavior eg. greyscale then colour cycle.
- Endeavouring to test both expected and unexpected use cases for all parts of the program.

In the case of the user trying to use the program in this way, most of the time we have opted to simply ignore their action rather than raise an error message. Error messages are generally only raised when things are deemed to have failed beyond the user's control or in a way that is not otherwise clear to the user.

## User Guide
Welcome to Andie! This user guide will help you navigate through its features.

#### Selecting Regions of the Image
A few of the tools in ANDIE require selecting a region of the image with the mouse. This can be done by clicking and holding on a section of the image and then dragging the mouse across the image until an acceptable region of the image is selected. The current selection is indicated by a light blue box drawn over the image. To clear the current selection, simply click somewhere on the image.

#### File Menu
*This is where you can open, save and export images - as well as exit ANDIE.*  
- **Open**: Open a file dialog to choose an image file to edit in ANDIE.  
- **Save**: Saves the current image and it's operations.
- **Save As**: Saves the current image and it's operations with a new file name.  
- **Export**: Incorporates the operations into the image and saves it in a non-reversible format.  
- **Print**: Sends the current state of the image to a printer for printing.
- **License**: Shows the license under which ANDIE is released.
- **Exit**: Exits ANDIE.  

#### Edit Menu
*This is where you can carry out common editing operations on the image*  
- **Undo**: Reverts the last action performed.  
- **Redo**: Reapplies the last undone action.  
- **Horizontal Flip**: Flips the image across its horizontal axis.  
- **Vertical Flip**: Flips the image across its vertical axis.  
- **Resize**: Resizes the image by a specified percentage.  
- **Rotation Filter**: Applies rotation to the image around its centre.
- **Crop to Selection**: Crops the image to the currently selected region. For instructions on how to select a region see [Selecting Regions of the Image](#selecting-regions-of-the-image).  
- **Draw Rectangle**: Draws a rectangle over the currently selected region. A popup will appear asking you to choose how you want the rectangle to look. This popup will remember your previous drawing choices. For instructions on how to select a region see [Selecting Regions of the Image](#selecting-regions-of-the-image).
- **Draw Oval**: Draws an oval bounded by the currently selected region. A popup will appear asking you to choose how you want the oval to look. This popup will remember your previous drawing choices. For instructions on how to select a region see [Selecting Regions of the Image](#selecting-regions-of-the-image).
- **Draw Line**: Draws a line from the point where the selection was started to the point where the selection stopped. A popup will appear asking you to choose how you want the line to look. This popup will remember your previous drawing choices. For instructions on how to select a region see [Selecting Regions of the Image](#selecting-regions-of-the-image).

#### View Menu
*This is where you can adjust how the image is shown in the window, without actually changing the image*  
- **Zoom In**: Zooms in on the image.  
- **Zoom Out**: Zooms out of the image.  
- **Zoom Full**: Fits the image to the screen.  

Note that you can also control the zoom of the program by using the scroll wheel.

#### Filter Menu
*This is where you can apply operations classed as filters, which are operations which are applied to each pixel independently*  
- **Mean Filter**: Changes each pixel to the mean value of its neighbors within a specified radius.  
- **Median Filter**: Changes each pixel to the mean value of its neighbors within a specified radius.  
- **Sharpen Filter**: Sharpens the image by a specified amount.  
- **Gaussian Filter**: Applies a gaussian blur of a specified radius to each pixel.  
- **Block Average Filter**: Applies a pixelation of specified width and height to the image.
- **Random Scatter Filter**: Randomly scatters pixels within a specified radius.
- **Emboss Filter**: Embosses edges in the image in a specified orientation.
- **Horizontal Sobel Filter**: Accentuates edges in the image in the horizontal direction
- **Vertical Sobel Filter**: Accentuates edges in the image in the vertical direction

#### Colour Menu
*This is where you can carry out operations that adjust the colour properties of the image as a whole*  
- **Colour Cycle**: Allows you to rearrange the colour channels of the image.  
- **Colour Invert**: Inverts the colors of the image.  
- **Greyscale**: Converts the image to greyscale.  
- **Brightness/Contrast**: Adjusts the brightness and the contrast of the image.

#### Macro Menu
*This is where you can manage macros, which are sets of operations saved to a file which can be replayed*
- **Start**: Start recording a macro.
- **End & Save**: If a macro is currently being recorded, stop recording it and save the macro to a file.
- **Cancel**: If a macro is currently being recorded, cancel recording it.
- **Load**: Load a macro from a file and apply it to the current image.

#### Language Menu
Here you can choose the preferred language for ANDIE's interface. The current options are:
- English
- Japanese

### Keyboard Shortcuts
- **Ctrl/⌘ + A**: Sharpen Filter
- **Ctrl/⌘ + B**: Block Averaging Filter
- **Ctrl/⌘ + Shift + B**: Start Macro
- **Ctrl/⌘ + C**: Colour Cycle
- **Ctrl/⌘ + Shift + C**: Cancel Macro
- **Ctrl/⌘ + D**: Brightness/Contrast
- **Ctrl/⌘ + E**: Export
- **Ctrl/⌘ + Shift + E**: End&Save Macro
- **Ctrl/⌘ + F**: Scale
- **Ctrl/⌘ + G**: Gaussian Filter
- **Ctrl/⌘ + H**: Horizontal Flip
- **Ctrl/⌘ + I**: Colour Invert
- **Ctrl/⌘ + J**: Greyscale
- **Ctrl/⌘ + L**: License
- **Ctrl/⌘ + Shift + L**: Load Macro
- **Ctrl/⌘ + M**: Mean Filter
- **Ctrl/⌘ + N**: Median Filter
- **Ctrl/⌘ + O**: Open
- **Ctrl/⌘ + P**: Print
- **Ctrl/⌘ + Q**: Exit
- **Ctrl/⌘ + R**: Rotate
- **Ctrl/⌘ + S**: Save
- **Ctrl/⌘ + Shift + S**: Save As
- **Ctrl/⌘ + T**: Random Scatter Filter
- **Ctrl/⌘ + U**: Emboss Filter
- **Ctrl/⌘ + V**: Vertical Flip
- **Ctrl/⌘ + W**: Horizontal Sobel Filter
- **Ctrl/⌘ + X**: Vertical Sobel Filter
- **Ctrl/⌘ + Y**: Redo
- **Ctrl/⌘ + Z**: Undo
- **Ctrl/⌘ + 1**: Draw Rectangle
- **Ctrl/⌘ + 2**: Draw Oval
- **Ctrl/⌘ + 3**: Draw Line
- **Ctrl/⌘ + ,**: Set language to English
- **Ctrl/⌘ + .**: Set language to Japanese
- **Ctrl/⌘ + -**: Zoom out
- **Ctrl/⌘ + =**: Zoom in

### Toolbar
The toolbar makes common operations more accessible. The items available in the toolbar, from left to right, are:
- Save
- Undo
- Redo
- Draw Oval
- Draw Rectangle
- Zoom In
- Zoom Out

### Show us something
For the show us something task, we implemented two things:
- Scroll to zoom
- Print to a printer

## Build Instructions
Our version of ANDIE is known to successfully build with Gradle, using the build.gradle file included. It may also build fine with the VSCode Java Extension, but there have been issues using this in the past.

## Known issues
- We know that you can zoom in and out even when there isn't an image open in ANDIE. Our judgement is that this isn't harmful behavior to leave in, and there could perhaps be very odd cases where users might want to do this.  
- The error messages currently being provided to the user are sub-par, and sometimes do not explain the error adequately.  
- We are aware that the selection box is drawn on the same panel as the image, and thus redrawing the selection box means also redrawing the image, which is inefficient. Investigation into using a JLayeredPane to achieve an overlay of two panels, one for the selection box and one for the image, was carried out but the lack of layout in the JLayeredPane presented too much of an obstacle to devote more time to, and the selection redrawing is still smooth on modern hardware. Due to the ImagePanel's double buffering there is not flickering that arises from this redrawing.  
- We are aware that the recording icon for macro recording can appear out of view of the user in cases where they have the window smaller than the image and have scrolled either left or down.  
- When abused, the language changing behavior is slightly sub-optimal.
- When clicking buttons in the toolbar focus from the frame is lost, and the keyboard shortcuts can no longer be used. Focus can be returned by dragging the toolbar off the frame to make it a separate window, but this is not ideal.
- The scroll to zoom functionality does not centre the point at which the scrolling occurs on the mouse pointer, as would normally be expected.