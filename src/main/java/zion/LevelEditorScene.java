package zion;

import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene{
    
    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location = 0) in vec3 aPos; // letter a in front is for attribute\n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor; // letter f is for fragment shader\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}\n";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}";
    
    private int vertexID, fragmentID, shaderProgram; // the shader program is the combination fo the vertex and fragment code
    
    public LevelEditorScene() {
        System.out.println("Inside Level editor scene");
    }
    
    @Override
    public void init() {
        //==========================================================================
        // Compile and Link the shaders
        //==========================================================================
        
        // Vertex Shader - load and compile
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code to the GPU - or something else (things are happening under the hood
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        
        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS); // the letter i stands for info
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            // there is some C underneath, and C is a bit verbose for strings, you need the length of the string to
            // display it
            System.out.println("\nERROR : 'defaultShader.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : ""; // will break out of the program
        }
        
        // Fragment Shader - load and compile
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code to the GPU - or something else (things are happening under the hood
        glShaderSource(fragmentID, fragmentShaderSrc); // we link the shader to its code
        glCompileShader(fragmentID);
    
        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS); // the letter i stands for info
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            // there is some C underneath, and C is a bit verbose for strings, you need the length of the string to
            // display it
            System.out.println("ERROR : 'defaultShader.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : ""; // exits the program
        }
        
        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);
        
        // Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'defaultShader.glsl'\n\tlinking of shaders failed");
            System.out.println(glGetShaderInfoLog(shaderProgram, len));
            assert false: "";
        }
    }
    
    @Override
    public void update(float dt) {

    }
}
