package zion;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector2f position;
    
    public Camera(Vector2f position) {
        this.position = position;
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }
    
    /**
     * If the window size has changed we want to be able to adjust the projection matrix
     */
    public void adjustProjection() {
        projectionMatrix.identity(); // gives us an identity matrix
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0,100);
        // gives us a projection matrix with 0 x and y coordinates on the bottom left, the 40 tiles 32 pixel in width, 21 tiles 32 pixel in height
    }
    
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f); // the direction it's looking at
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f,0.0f); //  the y-axis is the up axis
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);
        return this.viewMatrix;
        // I don't really get the cameraFront.add(position...
    }
    
    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}
