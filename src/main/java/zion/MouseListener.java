package zion;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener mouseListener;
    private static double x;
    private static double y;
    private static double lastX;
    private static double lastY;
    private static double scrollX;
    private static double scrollY;
    private boolean[] mouseButtonPressed = new boolean[GLFW_MOUSE_BUTTON_LAST];
    private boolean isDragging;
    
    private MouseListener() {
    
    }
    
    public static MouseListener get() {
        if (mouseListener == null) {
            mouseListener = new MouseListener();
        }
        
        return mouseListener;
    }
    
    public static void mousePosCallback(long window, double x, double y) {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = x;
        get().y = y;
        boolean isPressed = get().mouseButtonPressed[0];
        for(int i=1; i<GLFW_MOUSE_BUTTON_LAST; i++) {
            isPressed = isPressed || get().mouseButtonPressed[i];
        }
        get().isDragging = isPressed;
    }
    
    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS) {
            get().mouseButtonPressed[button] = true;
        } else if(action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }
    
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }
    
    public static void enfFrame() {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = 0;
        get().y = 0;
        get().scrollX = 0;
        get().scrollY = 0;
    }
    
    public static float getX() {
        return (float)(get().x);
    }
    
    public static float getY() {
        return (float)(get().y);
    }
    
    public static float getDx() {
        return (float)(get().lastX - get().x);
    }
    
    public static float getDy() {
        return (float)(get().lastY - get().y);
    }
    
    public static float getScrollX() {
        return (float)(get().scrollX);
    }
    
    public static float getScrollY() {
        return (float)(get().scrollY);
    }
    
    public static boolean isDragging() {
        return get().isDragging;
    }
    
    public static boolean mouseButtonDown(int button) {
        return get().mouseButtonPressed[button];
    }
}
