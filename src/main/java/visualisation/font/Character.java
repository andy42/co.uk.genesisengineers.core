package visualisation.font;

import visualisation.TextureRegion;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Character {

    private FloatBuffer vertexBuffer; // to hold the vertices
    private int vertexCount = 0;
    private int xAdvance = 0;
    private int width = 0;

    public Character (FontInfo fontInfo, CharacterInfo charInfo, int size) {

        float vertices[] = {charInfo.getXoffset(), charInfo.getYoffset(), 0.0f,     // v1 tl
                charInfo.getXoffset(), charInfo.getYoffset() + charInfo.getHeight(), 0.0f,      // v2 bl
                charInfo.getXoffset() + charInfo.getWidth(), charInfo.getYoffset(), 0.0f,      // v3 tr
                charInfo.getXoffset() + charInfo.getWidth(), charInfo.getYoffset() + charInfo.getHeight(), 0f      // v4 br
        };

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        this.vertexBuffer = byteBuffer.asFloatBuffer();
        this.vertexBuffer.put(vertices);
        this.vertexBuffer.position(0);

        this.vertexCount = vertices.length;

        this.xAdvance = charInfo.getXadvance();
        this.width = charInfo.getXoffset() + charInfo.getWidth();
    }

    public void drawTexture (TextureRegion textureRegion) {
        // point to the buffer
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // set the face rotation
        glFrontFace(GL_CW);

        glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);

        textureRegion.bind();

        // draw vertices as triangle strips (2 make a square)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, this.vertexCount / 3);

        // tidy up
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public int getxAdvance () {
        return xAdvance;
    }

    public int getWidth () {
        return width;
    }
}
