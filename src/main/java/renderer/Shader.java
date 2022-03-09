package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    
    int vertexID;
    int fragmentID;
    int shaderProgramID;
    
    String filepath;
    String vertexSource;
    String fragmentSource;
    
    public Shader(String filepath) {
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z])+");
            // careful ! can't put any space in between the regex expressions
            // + means al least one / [a-z] is characters a to z / ( ) is for a space
            
            // Find the first pattern after #type`
            int index = source.indexOf("#type") + 6; // +6 to get past the word after there is vertex or fragment
            int eol = source.indexOf("\n", index); // end of the line index
            String firstPattern = source.substring(index, eol).trim(); // trim removes any white space
            
            // Find the second pattern after #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();
            
            if(firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if(firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern);
            }
    
            if(secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if(secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern);
            }
            
            // the splitString[0] is garbage, in my test is just a \n
            
        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open the filepath for the shader in '" + filepath + "'";
        }
    }
    
    /**
     * Compiles and Link the vertex and fragment shaders
     */
    public void compile() {
        //==========================================================================
        // Compile the shaders
        //==========================================================================
        // Vertex Shader - load and compile
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code to the GPU - or something else (things are happening under the hood
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
    
        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS); // the letter i stands for info
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            // there is some C underneath, and C is a bit verbose for strings, you need the length of the string to
            // display it
            System.out.println("\nERROR :" +filepath+ "\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : ""; // will break out of the program
        }
    
        // Fragment Shader - load and compile
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code to the GPU - or something else (things are happening under the hood
        glShaderSource(fragmentID, fragmentSource); // we link the shader to its code
        glCompileShader(fragmentID);
    
        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS); // the letter i stands for info
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            // there is some C underneath, and C is a bit verbose for strings, you need the length of the string to
            // display it
            System.out.println("ERROR :" +filepath+ "\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : ""; // exits the program
        }
    
        //==========================================================================
        // Link the vertex and fragment shaders
        //==========================================================================
        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
    
        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR :" +filepath+ "\n\tlinking of shaders failed");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false: "";
        }
    }
    
    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);
    
    }
    
    public void detach() {
        glUseProgram(0);
    }
}