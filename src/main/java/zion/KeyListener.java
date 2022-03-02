package zion;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener keyListener;
    private static boolean[] keyPressed = new boolean[GLFW_KEY_LAST];
    
    private KeyListener(){
    
    }
    
    private static KeyListener get() {
        if(keyListener == null) {
            keyListener = new KeyListener();
        }
        
        return keyListener;
    }
    
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if(action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if(action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }
    
    public static boolean isKeyPressed(int key) {
        return keyPressed[key];
    }
}
