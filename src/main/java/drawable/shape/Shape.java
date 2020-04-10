package drawable.shape;

import visualisation.Visualisation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Shape {

    private int verticesCount;
    private FloatBuffer vertexBuffer; // to hold the vertices

    public Shape(float vertices[]){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        this.verticesCount = vertices.length;

        // allocates memory from the byte buffer
        this.vertexBuffer = byteBuffer.asFloatBuffer();
        this.vertexBuffer.put(vertices);
        this.vertexBuffer.position(0);
    }

    public void preDraw(Visualisation visualisation){
        glEnableClientState(GL_VERTEX_ARRAY);
        // set the face rotation
        glFrontFace(GL_CW);
        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
    }

    public void draw(Visualisation visualisation){
        glDrawArrays(GL_TRIANGLE_STRIP, 0, verticesCount / 3);
    }

    public void postDraw(Visualisation visualisation){
        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
