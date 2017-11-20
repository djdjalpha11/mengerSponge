import java.nio.*;
import org.lwjgl.*;
import org.joml.*;
import org.lwjgl.opengl.*;

public class Quad
{
    float angle;
    float size;
    FloatBuffer buffer;
    int VBO;
    int shaderProgram;
    int posLocation;
    
    public Quad(final float length, final int shader) {
        this.angle = 0.0f;
        this.buffer = BufferUtils.createFloatBuffer(16);
        this.size = length;
        this.shaderProgram = shader;
        this.init();
    }
    
    public void init() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO = GL15.glGenBuffers());
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TextureManager.Quadvertices, GL15.GL_STATIC_DRAW);
        GL20.glEnableVertexAttribArray(this.posLocation = GL20.glGetAttribLocation(this.shaderProgram, "position"));
        GL20.glVertexAttribPointer(this.posLocation, 3, GL20.GL_FLOAT, false, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public void render() {
        this.angle += 1.0E-4f;
        GL20.glUseProgram(this.shaderProgram);
        final int model = GL20.glGetUniformLocation(this.shaderProgram, "modelView");
        final Matrix4f modelView = new Matrix4f().rotate(this.angle, 0.0f, 1.0f, 0.0f);
        GL20.glUniformMatrix4fv(model, false, modelView.get(this.buffer));
        final int proj = GL20.glGetUniformLocation(this.shaderProgram, "proj");
        final Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0), 1.0f, 0.1f, 100.0f).lookAt(0.0f, 0.0f, -3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        GL20.glUniformMatrix4fv(proj, false, projection.get(this.buffer));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO);
        GL11.glDrawArrays(4, 0, 6);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public void clean() {
        GL15.glDeleteBuffers(this.VBO);
    }
}
