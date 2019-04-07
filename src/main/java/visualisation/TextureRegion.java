package visualisation;

import org.lwjgl.stb.STBTTAlignedQuad;
import util.Logger;
import util.Vector2Df;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class TextureRegion {
    private int textureId = -1;
    private FloatBuffer textureCoordBuffer;
    private Vector2Df dimensions = new Vector2Df();

    public TextureRegion (Texture texture, STBTTAlignedQuad quad) {
        this.textureId = texture.getTextureId();

        float left = quad.s0();
        float right = quad.s1();
        float top = quad.t0();
        float bottom = quad.t1();


        final float[] coords = {left, top, left, bottom, right, top, right, bottom};

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        this.textureCoordBuffer = byteBuffer.asFloatBuffer();
        this.textureCoordBuffer.put(coords);
        this.textureCoordBuffer.position(0);

        this.dimensions = new Vector2Df(quad.x1() - quad.x0(), quad.y1() - quad.y0());
    }


    public TextureRegion (Texture texture, Vector2Df position, Vector2Df dimensions) {
        this.textureId = texture.getTextureId();
        generateTextureCoordBuffer(texture, position, dimensions);
        this.dimensions = dimensions;


    }

    //Vector2Df position : postion in pixels of the region in the texture
    //int width : the width of the region in pixels
    //int height : the height of the region in pixels
    private void generateTextureCoordBuffer (Texture texture, Vector2Df position, Vector2Df dimensions) {
        float textureWidth = texture.getWidth();
        float textureHeight = texture.getHeight();
        //Logger.info("textureWidth =" + textureWidth + ", textureHeight = " + textureHeight);

        if (textureWidth == 0 || textureHeight == 0) {
            Logger.error("generateTextureCoordBuffer textureWidth or textureHeight == 0");
            return;
        }

        if ((position.x + dimensions.x) > textureWidth || (position.y + dimensions.y) > textureHeight) {
            Logger.error("generateTextureCoordBuffer Region out side of texture");
            return;
        }

        float left = position.x / textureWidth;
        float right = left + dimensions.x / textureWidth;
        float top = position.y / textureHeight;
        float bottom = top + dimensions.y / textureHeight;

        final float[] coords = {left, top, left, bottom, right, top, right, bottom};


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        this.textureCoordBuffer = byteBuffer.asFloatBuffer();
        this.textureCoordBuffer.put(coords);
        this.textureCoordBuffer.position(0);

        this.dimensions = dimensions;
    }

    public void bind () {
        glBindTexture(GL_TEXTURE_2D, this.textureId);
        glTexCoordPointer(2, GL_FLOAT, 0, this.textureCoordBuffer);
    }

    public int getWidth () {
        return (int) this.dimensions.x;
    }

    public int getHeight () {
        return (int) this.dimensions.y;
    }
}
