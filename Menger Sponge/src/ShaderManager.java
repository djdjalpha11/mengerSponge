import org.lwjgl.opengl.*;
import java.io.*;
import java.nio.*;

public class ShaderManager
{
    public static final int SM_VERTEX = 0;
    public static final int SM_FRAGMENT = 1;
    String shaderProgramName;
    String vertexShaderCode;
    String fragmentShaderCode;
    int shaderID;
    int vertexID;
    int fragmentID;
    
    public ShaderManager(final String name) {
        this.shaderProgramName = name;
    }
    
    public ShaderManager(final String name, final String vertexCode, final String fragmentCode) {
        this.shaderProgramName = name;
        this.vertexID = GL20.glCreateShader(GL20.VERTEX_SHADER);
        this.vertexShaderCode = vertexCode;
        this.fragmentID = GL20.glCreateShader(GL20.FRAGMENT_SHADER);
        this.fragmentShaderCode = fragmentCode;
    }
    
    public void loadShader(final File filePath, final int shaderType) {
        String line = null;
        String fullText = "";
        try {
            final FileReader fileReader = new FileReader(filePath.getPath());
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                fullText = String.valueOf(fullText) + line + "\n";
            }
            bufferedReader.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '");
        }
        catch (IOException ex2) {
            System.out.println("Error reading file");
        }
        if (shaderType == 0) {
            this.vertexID = GL20.glCreateShader(GL20.VERTEX_SHADER);
            this.vertexShaderCode = fullText;
        }
        else if (shaderType == 1) {
            this.fragmentID = GL20.glCreateShader(GL20.FRAGMENT_SHADER);
            this.fragmentShaderCode = fullText;
        }
    }
    
    public void createShaderProgram() {
        GL20.glShaderSource(this.vertexID, this.vertexShaderCode);
        GL20.glCompileShader(this.vertexID);
        int[] success = { 0 };
        GL20.glGetShaderiv(this.vertexID, GL20.COMPILE_STATUS, success);
        System.out.println(success[0]);
        GL20.glGetShaderInfoLog(this.vertexID);
        GL20.glShaderSource(this.fragmentID, this.fragmentShaderCode);
        GL20.glCompileShader(this.fragmentID);
        GL20.glGetShaderiv(this.fragmentID, GL20.COMPILE_STATUS, success);
        System.out.println(success[0]);
        System.out.println(GL20.glGetShaderInfoLog(this.fragmentID));
        GL20.glAttachShader(this.shaderID = GL20.glCreateProgram(), this.vertexID);
        GL20.glAttachShader(this.shaderID, this.fragmentID);
        GL20.glLinkProgram(this.shaderID);
        GL20.glDeleteShader(this.vertexID);
        GL20.glDeleteShader(this.fragmentID);
    }
}
