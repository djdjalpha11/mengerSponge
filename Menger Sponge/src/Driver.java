import java.nio.*;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import java.io.*;
import org.joml.*;
import org.lwjgl.glfw.*;
import java.util.*;

public class Driver
{
    static FloatBuffer buffe r= BufferUtils.createFloatBuffer(16);;
    static boolean keyUP = false;
    static boolean keyDown = false;
    static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        GLFW.glfwWindowHint(131076, 0);
        GLFW.glfwWindowHint(131075, 1);
        final int WIDTH = 300;
        final int HEIGHT = 300;
        final long window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Menger Sponge", 0L, 0L);
        if (window == 0L) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwSetKeyCallback(window, (Window, key, scancode, action, mods) -> {
            if (key == 256 && action == 0) {
                GLFW.glfwSetWindowShouldClose(Window, true);
            }
            if (key == 265 && action == 1) {
                Driver.keyUP = true;
            }
            if (key == 264 && action == 1) {
                Driver.keyDown = true;
            }
            return;
        });
        final GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);
        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);
        GL.createCapabilities();
        System.out.println(GL11.glGetString(GL11.GL_VERSION));
        GL11.glViewport(0, 0, 300, 300);
        final ShaderManager shader = new ShaderManager("BasicShader");
        shader.loadShader(new File("src/vertexShader.vsh"), ShaderManager.SM_VERTEX);
        shader.loadShader(new File("src/fragmentShader.fsh"), ShaderManager.SM_FRAGMENT);
        shader.createShaderProgram();
        GL11.glClearColor(0.5f, 0.3f, 0.7f, 1.0f);
        final ArrayList<ArrayList<Box>> container = new ArrayList<ArrayList<Box>>();
        final Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
        final ArrayList<Box> mengerSponge = new ArrayList<Box>();
        final Box menger = new Box(1.0f, position, shader.shaderID);
        mengerSponge.add(menger);
        container.add(mengerSponge);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
            if (Driver.keyUP) {
                final ArrayList<Box> next = new ArrayList<Box>();
                for (final Box b : container.get(Driver.counter)) {
                    final ArrayList<Box> newBoxes = b.generate();
                    next.addAll(newBoxes);
                    newBoxes.removeAll(newBoxes);
                }
                container.add(next);
                ++Driver.counter;
                Driver.keyUP = false;
            }
            if (Driver.keyDown) {
                if (Driver.counter > 0) {
                    container.remove(Driver.counter);
                    --Driver.counter;
                }
                Driver.keyDown = false;
            }
            for (final Box b2 : container.get(Driver.counter)) {
                b2.render();
            }
            GLFW.glfwSwapBuffers(window);
        }
        GLFW.glfwTerminate();
    }
}
