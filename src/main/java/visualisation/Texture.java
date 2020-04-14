package visualisation;

import content.Context;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import util.Logger;
import util.Vector2Df;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGenTextures;

public class Texture {

    private Vector<TextureRegion> regionList = new Vector<>();

    private int id = -1;
    private int textureId = 0;

    private int width = 0;
    private int height = 0;
    private ByteBuffer byteBuffer = null;


    public Texture () {
        this.textureId = glGenTextures();
    }

    public Texture (int width, int height, ByteBuffer buffer) {
        this.textureId = glGenTextures();
        this.width = width;
        this.height = height;
        this.byteBuffer = buffer;
        initGl();
    }

    public TextureRegion addTextureRegion (Vector2Df position, Vector2Df dimensions) {
        try {
            TextureRegion textureRegion = new TextureRegion(this, position, dimensions);
            this.regionList.add(textureRegion);
            return textureRegion;
        }
        catch (Exception e){
            Logger.exception(new Exception(e), e.getMessage());
            return null;
        }
    }

    public TextureRegion getTextureRegion (int index) {
        return this.regionList.get(index);
    }

    public void bindTextureRegion (int index) {
        TextureRegion textureRegion = getTextureRegion(index);
        if (textureRegion != null) {
            textureRegion.bind();
        }
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return this.id;
    }

    public int getTextureId () {
        return textureId;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public void delete () {
        GL11.glDeleteTextures(textureId);
    }

    public void bind () {
        GL11.glBindTexture(GL_TEXTURE_2D, this.textureId);
    }

    private void initGl () {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    }

    public boolean initPng (Context context, int resId) {
        if (loadPng(context, resId) == false) {
            return false;
        }
        initGl();
        return true;
    }

    private boolean loadPng (Context context, int resId) {
        try {
            File file =context.getResources().getAssetFile(resId);

            InputStream inputStream = new FileInputStream(file);

            PNGDecoder pngDecoder = new PNGDecoder(inputStream);

            // set texture width and height
            this.width = pngDecoder.getWidth();
            this.height = pngDecoder.getHeight();

            // decode png in a ByteBuffer
            this.byteBuffer = ByteBuffer.allocateDirect(4 * this.width * this.height);

            pngDecoder.decode(byteBuffer, width * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();

            inputStream.close();

            inputStream.close();
        } catch (FileNotFoundException e) {
            Logger.exception(e, "Texture %s not found.", resId);
            return false;
        } catch (IOException e) {
            Logger.exception(e, "Texture failed to load, couldn't read or write to stream.");
            return false;
        }

        return true;
    }
}
