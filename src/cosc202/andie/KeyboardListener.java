package cosc202.andie;

import java.util.HashMap;
import java.awt.event.*;
import java.util.*;
import javax.swing.Action;
import javax.swing.KeyStroke;


/**
 * <p>
 * Keyboard Listener for Keyboard shortcuts.
 * </p>
 * 
 * @author Bill and Blake
 * @version 1.0
 */
public class KeyboardListener implements KeyListener{
    /** Stores which keystrokes should do which actions, in other words, the keybinds */
    private static HashMap<KeyStroke, Action> keyBinds = new HashMap<KeyStroke, Action>();

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public KeyboardListener(){}
    
    /**
     * <p>
     * Performs an action when a key event occurs.
     * </p>
     * 
     * <p>
     * If the key event which triggered this method matches any key bind, trigger that keybind.
     * </p>
     * 
     * @param e KeyEvent to call this method.
     */
    public void keyPressed(KeyEvent e){
        for(Map.Entry<KeyStroke, Action> keyBind : keyBinds.entrySet()){
            if(KeyStroke.getKeyStrokeForEvent(e).equals(keyBind.getKey())){
                keyBind.getValue().actionPerformed(null);
            }
        }
    }

    /**
     * <p>
     * Redundant.
     * </p>
     */
    public void keyReleased(KeyEvent e){

    }

    /**
     * <p>
     * Redundant.
     * </p>
     */
    public void keyTyped(KeyEvent e){

    }

    /**
     * <p>
     * Add Key Bind to the map.
     * </p>
     * 
     * <p>
     * Assigns a Keybinds to the hashmap with an associated action.
     * </p>
     * 
     * @param stroke
     * @param action
     */
    public static void add(KeyStroke stroke, Action action){
        keyBinds.put(stroke, action);
    }
}
