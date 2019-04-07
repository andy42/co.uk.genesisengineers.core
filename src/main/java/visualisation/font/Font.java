package visualisation.font;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.FontFormatException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.javafx.geom.Vec3f;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;
import util.Logger;
import util.ResourceLoader;
import util.Vector2Df;
import visualisation.Texture;
import visualisation.TextureRegion;
import visualisation.Visualisation;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static java.awt.Font.TRUETYPE_FONT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Font {

    private FontInfo fontInfo = new FontInfo();

    private final Map<Integer, TextureRegion> characterTextureRegions = new HashMap<>();
    private final Map<Integer, Character> characters = new HashMap<>();

    private Texture texture = null;


    public Font () {
    }

    private void createCharTextureRegion (CharacterInfo characterInfo) {
        Vector2Df position = new Vector2Df(characterInfo.getX() + fontInfo.getPaddingLeft(), characterInfo.getY() + fontInfo.getPaddingTop());

        Vector2Df dimensions = new Vector2Df(characterInfo.getWidth() - fontInfo.getPaddingWidth(), characterInfo.getHeight() - fontInfo.getPaddingHeight());


        TextureRegion charTextureRegion = new TextureRegion(texture, position, dimensions);

        characterTextureRegions.put(characterInfo.getId(), charTextureRegion);
    }

    private void createChar (CharacterInfo characterInfo) {
        Character character = new Character(this.fontInfo, characterInfo, 20);
        this.characters.put(characterInfo.getId(), character);
    }


    //filename is the file path of the font without file extension
    public boolean init (String filename) {
        if (fontInfo.init(filename + ".fnt") == false) {
            return false;
        }

        this.texture = new Texture();
        if (texture.init(filename + ".png") == false) {
            return false;
        }

        Set<Integer> charSet = fontInfo.getCharacterSet();
        for (Integer c : charSet) {
            CharacterInfo characterInfo = fontInfo.getCharacterInfo(c);
            if (characterInfo == null) {
                continue;
            }
            createCharTextureRegion(characterInfo);
            createChar(characterInfo);
        }
        return true;
    }

    public TextureRegion getCharTextureRegion (int c) {
        return this.characterTextureRegions.get(c);
    }

    public void bind () {
        if (this.texture == null) {
            return;
        }
        GL11.glBindTexture(GL_TEXTURE_2D, this.texture.getTextureId());
    }

    public int getTextWidth (String text, float size) {
        float width = 0;
        for (int i = 0; i < (text.length() - 1); i++) {
            int c = text.charAt(i);

            Character character = characters.get(c);
            if (character == null) {
                continue;
            }
            width += character.getxAdvance() * size;
        }

        if (text.length() > 0) {

            int c = text.charAt(text.length() - 1);
            Character character = characters.get(c);
            if (character != null) {
                width += character.getWidth() * size;
            }

        }
        return (int) width;
    }

    public int getTextHeight (String text, float size) {
        return (int) (this.fontInfo.getLineHeight() * size);
    }

    public void drawText (String text, Vec3f colour, float size, Vector2Df coordinates) {

        Visualisation.getInstance().useFontProgram();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int xoffSet = 0;
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i);

            Character character = characters.get(c);
            if (character == null) {
                continue;
            }

            glPushMatrix();
            glTranslatef(coordinates.x + xoffSet, coordinates.y, 0);
            glScalef(size, size, 0);
            glColor3f(colour.x, colour.y, colour.z);
            character.drawTexture(getCharTextureRegion(c));
            glPopMatrix();

            xoffSet += character.getxAdvance() * size;
        }
        glDisable(GL_BLEND);
    }
}
