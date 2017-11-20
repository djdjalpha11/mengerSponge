import java.nio.*;
import java.util.*;
import org.lwjgl.*;
import org.joml.*;
import org.lwjgl.opengl.*;

public class Box
{
    float angle;
    float scale;
    Vector3f position;
    FloatBuffer buffer;
    int VBO;
    int shaderProgram;
    int posLocation;
    ArrayList<Box> mengerSponge;
    
    public Box(final float size, final Vector3f pos, final int shader) {
        this.angle = 0.0f;
        this.buffer = BufferUtils.createFloatBuffer(16);
        this.mengerSponge = new ArrayList<Box>();
        this.scale = size;
        this.position = pos;
        this.shaderProgram = shader;
        this.init();
    }
    
    public void init() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO = GL15.glGenBuffers());
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TextureManager.vertices, GL15.GL_STATIC_DRAW);
        GL20.glEnableVertexAttribArray(this.posLocation = GL20.glGetAttribLocation(this.shaderProgram, "position"));
        GL20.glVertexAttribPointer(this.posLocation, 3, GL20.GL_FLOAT, false, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public void render() {
        this.angle += 0.5f;
        GL20.glUseProgram(this.shaderProgram);
        final int model = GL20.glGetUniformLocation(this.shaderProgram, "modelView");
        final Matrix4f modelView = new Matrix4f().rotate((float)Math.toRadians(this.angle), 0.0f, 1.0f, 0.0f).translate(this.position).scale(this.scale);
        GL20.glUniformMatrix4fv(model, false, modelView.get(this.buffer));
        final int proj = GL20.glGetUniformLocation(this.shaderProgram, "proj");
        final Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(45.0), 1.0f, 0.1f, 100.0f).lookAt(0.0f, 0.0f, -2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        GL20.glUniformMatrix4fv(proj, false, projection.get(this.buffer));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO);
        GL11.glDrawArrays(6, 0, 36);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public ArrayList<Box> generate() {
        for (int x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                for (int z = -1; z < 2; ++z) {
                    final int sum = Math.abs(x) + Math.abs(y) + Math.abs(z);
                    if (sum <= 1) {
                        final float scaleDown = this.scale / 3.0f;
                        final Vector3f positionUpdate = new Vector3f().add(this.position.x + x * scaleDown, this.position.y + y * scaleDown, this.position.z + z * scaleDown);
                        final Box cubes = new Box(scaleDown, positionUpdate, this.shaderProgram);
                        this.mengerSponge.add(cubes);
                    }
                }
            }
        }
        return this.mengerSponge;
    }
    
    public void clean() {
        GL15.glDeleteBuffers(this.VBO);
    }
}
