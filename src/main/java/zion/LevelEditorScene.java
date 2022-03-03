package zion;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

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
    private float[] vertexArray = { // this is for the VBO I think
             // position x,y,z      // color r,g,b,a
             0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     // bottom right 0
            -0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     // top left     1
             0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,     // top right    2
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     // bottom left  3
    };
    
    // IMPORTANT : Must be in counter clock-wise oder !
    private int[] elementArray = { // this is for the EBO I think
            /*
                1x           2x



                3x           0x
            */
            2, 1, 0, // top triangle
            0, 1, 3 // bottom triangle
    };
    
    private int vaoID, vboID, eboID;
    
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
    
        //==========================================================================
        // Generating VAO, VBO, and EBO buffer objects, and send to GPU
        //==========================================================================
        vaoID = glGenVertexArrays(); // creating a new vertex array in the GPL
        glBindVertexArray(vaoID); // makes sure everything we are going to do after is on the vao with vaoID
        
        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); // /!\ Don't forget the flip ! won't work otherwise
        // flip() method makes a buffer ready for a new sequence of channel-write or relative get operations: It sets the
        // limit to the current position and then sets the position to zero. Buffer keeps track of the data written into
        // it. Post writing, flip() method is called to switch from writing to reading mode.
        
        // Create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID); // ok so now focus on this buffer
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW); // we won't change anything in the buffer so static
        
        // Create the indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW); // careful here element buffer
        
        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSize = 4; // the size of a float is 4 bytes
        int vertexSize = (positionSize + colorSize) * floatSize;
        
        // The position attribute
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSize,0);
        glEnableVertexAttribArray(0);
        // Why 0 ? Because in the shader we put the position in location = 0
        
        // The color attribute
        glVertexAttribPointer(1,colorSize,GL_FLOAT, false,vertexSize,positionSize * floatSize);
        // the pointer is the offset in bytes to the beginning of the array
        glEnableVertexAttribArray(1);
        
        
    }
    
    @Override
    public void update(float dt) {
        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);
        
        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0); // enable the position
        glEnableVertexAttribArray(1); // enable the color (would be interesting to try to comment out this line
        
        glDrawElements(GL_TRIANGLES,elementArray.length, GL_UNSIGNED_INT,0);
        
        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }
}
