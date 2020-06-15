package co.uk.genesisengineers.core.visualisation;

import com.sun.javafx.geom.Vec3f;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Square {

    private FloatBuffer vertexBuffer; // to hold the vertices
    private float vertices[] = {-0.5f, -0.5f, 0.0f,     // v1 bl
            -0.5f, 0.5f, 0.0f,      // v2 tl
            0.5f, -0.5f, 0.0f,      // v3 br
            0.5f, 0.5f, 0.0f,       // v4 tr
    };


    //    NOTE: Original Mapping coordinates for the vertices order
    //    0.0f, 0.0f,		// top left		(V2)
    //    0.0f, 1.0f,		// bottom left	(V1)
    //    1.0f, 0.0f,		// top right	(V4)
    //    1.0f, 1.0f		// bottom right	(V3)

    public Square () {
        // float has 4 bytes, allocate 4 bytes per coordinate
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(this.vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        // allocates memory from the byte buffer
        this.vertexBuffer = byteBuffer.asFloatBuffer();
        this.vertexBuffer.put(this.vertices);
        this.vertexBuffer.position(0);

    }

    public void drawColour (Vec3f colour) {
        glEnableClientState(GL_VERTEX_ARRAY);

        // set the face rotation
        glFrontFace(GL_CW);

        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);

        glColor3f(colour.x, colour.y, colour.z);

        // draw vertices as triangle strips (2 make a square)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        // tidy up
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public void drawColour (Color colour) {
        glEnableClientState(GL_VERTEX_ARRAY);

        // set the face rotation
        glFrontFace(GL_CW);

        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);

        Visualisation.getInstance().setColor(colour);

        // draw vertices as triangle strips (2 make a square)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        // tidy up
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public void drawTexture (int textureId, int textureRegionIndex) {
        // point to the buffer
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // set the face rotation
        glFrontFace(GL_CW);

        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);

        TextureManager.getInstance().bind(textureId, textureRegionIndex);

        // draw vertices as triangle strips (2 make a square)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        // tidy up
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void drawTexture (TextureRegion textureRegion) {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // set the face rotation
        glFrontFace(GL_CW);

        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);

        textureRegion.bind();

        // draw vertices as triangle strips (2 make a square)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        // tidy up
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }
}
