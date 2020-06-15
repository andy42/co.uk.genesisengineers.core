package co.uk.genesisengineers.core.visualisation;

import co.uk.genesisengineers.core.input.KeyMapper;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;
import co.uk.genesisengineers.core.util.Logger;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MainWindow {

    // The window handle
    private long window;

    IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
    IntBuffer intBuffer2 = BufferUtils.createIntBuffer(1);

    private int width = 1;
    private int height = 1;

    private int monitorWidth = 1;
    private int monitorHeight = 1;

    private double dpi = 0;

    private OnWindowCloseListener onWindowCloseListener = null;
    private GLFWWindowSizeCallback windowSizeCallback;

    private void setWindowDimensions (int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth () {
        return this.width;
    }

    public int getHeight () {
        return this.height;
    }

    private long getWindowMonitor () {
        if (window != 0) {
            long monitor = glfwGetWindowMonitor(window);
            if (monitor != 0) return monitor;
        }
        return glfwGetPrimaryMonitor();
    }

    public boolean init (int width, int height) {

        this.width = width;
        this.height = height;

        // Setup an error callback. The default implementation
        // will print the error message in SystemBase.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        KeyMapper keyMapper = KeyMapper.getInstance();

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.


        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            //IntBuffer pWidth = stack.mallocInt(1); // int*
            //IntBuffer pHeight = stack.mallocInt(1); // int*

            intBuffer.clear();
            intBuffer2.clear();

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, intBuffer, intBuffer2);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window, (vidmode.width() - intBuffer.get(0)) / 2, (vidmode.height() - intBuffer2.get(0)) / 2);

            intBuffer.clear();
            intBuffer2.clear();
            glfwGetMonitorPhysicalSize(getWindowMonitor(), intBuffer, intBuffer2);
            monitorWidth = intBuffer.get(0);
            monitorHeight = intBuffer2.get(0);

            dpi = vidmode.width() / (monitorWidth / 25.4);

        } catch (NullPointerException e) {
            Logger.error("MainWindow: Null Pointer Exception " + e.getMessage());
            return false;
        }

        // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke (long window, int width, int height) {
                setWindowDimensions(width, height);
            }
        });

        return true;
    }

    public boolean shouldClose () {
        return glfwWindowShouldClose(window);
    }

    public long getWindow () {
        return window;
    }

    public void destroy () {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        if (this.onWindowCloseListener != null) {
            onWindowCloseListener.onWindowClose();
        }

    }

    public double getDpi(){
        return dpi;
    }

    public void setOnWindowCloseListener (OnWindowCloseListener onWindowCloseListener) {
        this.onWindowCloseListener = onWindowCloseListener;
    }

    public interface OnWindowCloseListener {
        void onWindowClose ();
    }
}
