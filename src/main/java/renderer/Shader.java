package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader {


    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;


    /*
    * Lecture et extraction des données dans le fichier GLSL
    *
    */
    public Shader(String filePath){
        this.filePath = filePath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] stringSplit = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after "#type" pattern
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);

            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after "#type" pattern
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);

            String secondPattern = source.substring(index, eol).trim();

            if(firstPattern.equals("vertex")){
                vertexSource = stringSplit[1];
            } else if(firstPattern.equals("fragment")) {
                fragmentSource = stringSplit[1];
            } else {
                throw new IOException("Unexcpected token '" + firstPattern + "'");
            }

            if(secondPattern.equals("vertex")){
                vertexSource = stringSplit[2];
            } else if(secondPattern.equals("fragment")) {
                fragmentSource = stringSplit[2];
            } else {
                throw new IOException("Unexcpected token '" + secondPattern + "'");
            }

        }catch(IOException e){

            System.out.println("Impossible de lire le fichier");
            e.printStackTrace();
            assert false: "Error: could not open the file " + this.filePath;
        }
    }

    public void compile(){

        // ============================================================
        // Compile and link shaders
        // ============================================================

        int vertexID, fragmentID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);


        //check error
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error : '" + filePath + "' vertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);


        //check error
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error : '" + filePath + "' fragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
        }

        // Link
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);


        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error : '" + filePath + "' shagerprogram shader compilation failed");
            System.out.println(glGetProgramInfoLog(fragmentID, len));
        }
    }

    public void use(){

        glUseProgram(shaderProgramID);

    }

    public void detach(){

        glUseProgram(0);

    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);

    }
}
