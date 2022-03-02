package util;

public class Time {
    public static float timeStarted = System.nanoTime();
    
    /**
     * @return time elapsed since the application started in seconds
     */
    public static float getTime() {
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
