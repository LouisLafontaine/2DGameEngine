package zion;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    float r = 1.0f;
    float g = 1.0f;
    float b = 1.0f;
    float a = 1.0f;
    private String title;
    private long window;
    
    private static Window windowObject = null;
    private static Scene currentScene = null;
    
    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }
    
    public static Window get() {
        if (Window.windowObject == null) {
            Window.windowObject = new Window();
        }
        
        return Window.windowObject;
    }
    
    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
            default:
                assert false : "Unknown scene '" + newScene + "'";
        }
    }
    
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + " !");
        
        init();
        loop();
    
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    
        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
    
    public void init() {
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
    
        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        
        // Create the window
        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }
    
        // Mouse callback
        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
        
        // Keyboard callback
        glfwSetKeyCallback(window, KeyListener::keyCallback);
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        
        // Make the window visible
        glfwShowWindow(window);
    
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        
        // Set the scene to the
        Window.changeScene(0);
    }
    
    public void loop() {
    
    
        // dt management
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
    
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);
            
            // Scenes
            if(dt >= 0) {
                currentScene.update(dt);
            }
    
            glfwSwapBuffers(window); // swap the color buffers
    
            // Poll for window events.
            glfwPollEvents();
            
            // Time elapsed on last frame
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
//            System.out.println(beginTime + " - " + endTime + " - " + dt);
            // won't it end up loosing precision ? Each float has only 7 digit precision, so how is the difference so precise ?
        }
    }
}