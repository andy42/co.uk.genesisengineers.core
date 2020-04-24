package visualisation;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import sun.plugin2.util.ColorUtil;
import sun.rmi.runtime.Log;
import util.Logger;
import util.Vector2Df;
import visualisation.font.Font;
import visualisation.font.FontInfo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetMonitorPhysicalSize;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Visualisation {

    private long window;
    private Square square = new Square();
    private SquareTopLeft squareTopLeft = new SquareTopLeft();

    // shader variables
    private int vsId = 0;
    private int fsId = 0;
    private int pId = 0;

    private int windowWidth = 1;
    private int windowHeight = 1;

    private int monitorWidth = 1;
    private int monitorHeight = 1;

    private ShaderProgram shaderProgramTexture = new ShaderProgram();
    private ShaderProgram shaderProgramColour = new ShaderProgram();
    private ShaderProgram shaderProgramFont = new ShaderProgram();

    Font font = new Font();

    public void setWindowDimensions (int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public int getWindowWidth () {
        return this.windowWidth;
    }

    public int getWindowHeight () {
        return this.windowHeight;
    }


    private float[] colorRGBA = new float[4];

    private static Visualisation s_instance = null;

    public static Visualisation getInstance () {
        if (s_instance == null) {
            s_instance = new Visualisation();
        }
        return s_instance;
    }

    private Visualisation () {
    }

    public int getShaderProgramTextureId () {
        return this.shaderProgramTexture.getProgrammeId();
    }

    public int getShaderProgramColourId () {
        return this.shaderProgramColour.getProgrammeId();
    }

    public void init (long window) {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        this.window = window;
        GL.createCapabilities();

        // Set the window background color
        glClearColor(1.0f, 0.5f, 0.0f, 0.0f);



        this.initShaders();
    }

    public void loadFont(Context context, int fontTextureAssetId, int fontInfoAssetId){
        font.init(context, fontTextureAssetId, fontInfoAssetId);
    }

    private boolean initShaders () {

        if (shaderProgramTexture.createProgramme("shaders/textured.vert", "shaders/textured.frag") == false) {
            Logger.error("initShaders == false : textured");
            return false;
        }

        if (shaderProgramColour.createProgramme("shaders/coloured.vert", "shaders/coloured.frag") == false) {
            Logger.error("initShaders == false : coloured");
            return false;
        }

        if (shaderProgramFont.createProgramme("shaders/font.vert", "shaders/font.frag") == false) {
            Logger.error("initShaders == false : font");
            return false;
        }

        shaderProgramTexture.useProgram();

        return true;
    }

    private int loadShader (String filename, int type) {

        StringBuilder shaderSource = new StringBuilder();
        int shaderId = glCreateShader(type);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            Logger.error("Could not read file");
            System.exit(-1);
        }
        //FileUtils
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);

        return shaderId;
    }

    private void camera () {
        //glViewport(0, 0, windowWidth, windowHeight);
    }

    public void initProjection () {
        camera();
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
    }

    public void finalise () {
        glFlush();
        glfwSwapBuffers(window);
    }

    public void clearMatrixStack () {
        glLoadIdentity();
    }

    public void useTextureProgram () {
        this.shaderProgramTexture.useProgram();
    }

    public void useColourProgram () {
        this.shaderProgramColour.useProgram();
    }

    public void useFontProgram () {
        this.shaderProgramFont.useProgram();
    }

    public void setColor (Vec3f rgb) {
        glColor3f(rgb.x, rgb.y, rgb.z);
    }

    public void setColor (Color color) {
        colorRGBA = color.getColorComponents(colorRGBA);
        glColor3f(colorRGBA[0], colorRGBA[1], colorRGBA[2]);
    }

    public void drawColouredSquareTopLeft (Vec3f colour) {
        squareTopLeft.drawColour(colour);
    }

    public void drawColouredSquareTopLeft (Vector2Df coordinates, Vector2Df dimensions, TextureRegion textureRegion) {
        glPushMatrix();
        glTranslatef(coordinates.x, coordinates.y, 0);
        glScalef(dimensions.x, dimensions.y, 0);
        squareTopLeft.drawTexture(textureRegion);
        glPopMatrix();
    }


    public void drawColouredSquare (Vec3f colour, Vector2Df coordinates, Vector2Df dimensions, float rotation) {
        glPushMatrix();
        glTranslatef(coordinates.x, coordinates.y, 0);
        glScalef(dimensions.x, dimensions.y, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        square.drawColour(colour);
        glPopMatrix();
    }

    public void drawTexturedSquare (int textureId, int textureRegionIndex, Vector2Df coordinates, Vector2Df dimensions, float rotation) {
        glPushMatrix();
        glTranslatef(coordinates.x, coordinates.y, 0);
        glScalef(dimensions.x, dimensions.y, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        square.drawTexture(textureId, textureRegionIndex);
        glPopMatrix();
    }

    public Font getFont () {
        return this.font;
    }
}