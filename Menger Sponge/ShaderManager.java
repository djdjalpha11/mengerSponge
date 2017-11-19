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
        this.vertexID = GL20.glCreateShader(35633);
        this.vertexShaderCode = vertexCode;
        this.fragmentID = GL20.glCreateShader(35632);
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
            this.vertexID = GL20.glCreateShader(35633);
            this.vertexShaderCode = fullText;
        }
        else if (shaderType == 1) {
            this.fragmentID = GL20.glCreateShader(35632);
            this.fragmentShaderCode = fullText;
        }
    }
    
    public void createShaderProgram() {
        GL20.glShaderSource(this.vertexID, this.vertexShaderCode);
        GL20.glCompileShader(this.vertexID);
        final int[] success = { 0 };
        GL20.glGetShaderiv(this.vertexID, 35718, success);
        System.out.println(success[0]);
        GL20.glGetShaderiv(this.vertexID, 35716, success);
        final ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
        buffer.order(ByteOrder.nativeOrder());
        final ByteBuffer tmp = ByteBuffer.allocateDirect(4);
        tmp.order(ByteOrder.nativeOrder());
        final IntBuffer intBuffer = tmp.asIntBuffer();
        GL20.glGetShaderInfoLog(this.vertexID, intBuffer, buffer);
        final int numBytes = intBuffer.get(0);
        final byte[] bytes = new byte[numBytes];
        buffer.get(bytes);
        System.out.println(new String(bytes));
        GL20.glShaderSource(this.fragmentID, this.fragmentShaderCode);
        GL20.glCompileShader(this.fragmentID);
        GL20.glGetShaderiv(this.fragmentID, 35713, success);
        System.out.println(success[0]);
        System.out.println(GL20.glGetShaderInfoLog(this.fragmentID));
        GL20.glAttachShader(this.shaderID = GL20.glCreateProgram(), this.vertexID);
        GL20.glAttachShader(this.shaderID, this.fragmentID);
        GL20.glLinkProgram(this.shaderID);
        GL20.glDeleteShader(this.vertexID);
        GL20.glDeleteShader(this.fragmentID);
    }
}
