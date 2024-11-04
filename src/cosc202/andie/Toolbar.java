package cosc202.andie;

import javax.swing.Action;
import javax.swing.JToolBar;

/**
 * <p>
 * Tool Bar to Quick Access to image Actions
 * </p>
 * 
 * <p>
 * Menus can be time consuming to sift through so having a toolbar users can move
 * around and rescale is always useful.
 * </p>
 * 
 * @author Blake Cooper and Bill Campbell
 * @version 1.0
 */
public class Toolbar {

    public static JToolBar toolBar = new JToolBar();
    
    /**
     * <p>
     * Adds actions to the toolbar.
     * </p>
     * 
     * @param action The action to add to the toolbar.
     */
    public static void add(Action action){
        toolBar.add(action);
    }
}
