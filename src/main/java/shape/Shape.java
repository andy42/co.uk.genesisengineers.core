package shape;

import visualisation.Visualisation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Shape {

    public static final int DRAW_TYPE_FAN = GL_TRIANGLE_FAN;
    public static final int DRAW_TYPE_STRIP = GL_TRIANGLE_STRIP;

    private int verticesCount;
    private FloatBuffer vertexBuffer; // to hold the vertices
    private int drawType; // ie GL_TRIANGLE_STRIP


    public Shape(float vertices[], int drawType){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        this.verticesCount = vertices.length;

        // allocates memory from the byte buffer
        this.vertexBuffer = byteBuffer.asFloatBuffer();
        this.vertexBuffer.put(vertices);
        this.vertexBuffer.position(0);

        this.drawType= drawType;
    }

    public void preDraw(Visualisation visualisation){
        glEnableClientState(GL_VERTEX_ARRAY);
        // set the face rotation
        glFrontFace(GL_CW);
        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
    }

    public void draw(Visualisation visualisation){
        glDrawArrays(drawType, 0, verticesCount / 3);
    }

    public void postDraw(Visualisation visualisation){
        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
