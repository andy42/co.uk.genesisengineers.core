package co.uk.genesisengineers.core.visualisation.font;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.javafx.geom.Vec3f;
import co.uk.genesisengineers.core.content.Context;
import org.lwjgl.opengl.GL11;
import co.uk.genesisengineers.core.util.Logger;
import co.uk.genesisengineers.core.util.Vector2Df;
import co.uk.genesisengineers.core.visualisation.Texture;
import co.uk.genesisengineers.core.visualisation.TextureRegion;
import co.uk.genesisengineers.core.visualisation.Visualisation;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Font {

    private FontInfo fontInfo = new FontInfo();

    private final Map<Integer, TextureRegion> characterTextureRegions = new HashMap<>();
    private final Map<Integer, Character> characters = new HashMap<>();

    private Texture texture = null;


    public Font () {
    }

    private void createCharTextureRegion (CharacterInfo characterInfo) throws Exception {
        Vector2Df position = new Vector2Df(characterInfo.getX() + fontInfo.getPaddingLeft(), characterInfo.getY() + fontInfo.getPaddingTop());

        Vector2Df dimensions = new Vector2Df(characterInfo.getWidth() - fontInfo.getPaddingWidth(), characterInfo.getHeight() - fontInfo.getPaddingHeight());


        TextureRegion charTextureRegion = new TextureRegion(texture, position, dimensions);

        characterTextureRegions.put(characterInfo.getId(), charTextureRegion);
    }

    private void createChar (CharacterInfo characterInfo) {
        Character character = new Character(this.fontInfo, characterInfo, 20);
        this.characters.put(characterInfo.getId(), character);
    }

    public boolean init (Context context, int fontTextureAssetId, int fontInfoAssetId) {


        File fontInfoFile = context.getResources().getAssetFile(fontInfoAssetId);

        if (fontInfo.init(fontInfoFile) == false) {
            return false;
        }

        this.texture = new Texture();
        if (texture.initPng(context, fontTextureAssetId) == false) {
            return false;
        }

        Set<Integer> charSet = fontInfo.getCharacterSet();
        for (Integer c : charSet) {
            CharacterInfo characterInfo = fontInfo.getCharacterInfo(c);
            if (characterInfo == null) {
                continue;
            }
            try{
                createCharTextureRegion(characterInfo);
            }
            catch (Exception e){
                Logger.exception(e,e.getMessage());
                return false;
            }
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
