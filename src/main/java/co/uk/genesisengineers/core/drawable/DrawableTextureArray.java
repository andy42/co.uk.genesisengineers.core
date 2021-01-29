package co.uk.genesisengineers.core.drawable;

import co.uk.genesisengineers.core.shape.Shape;
import co.uk.genesisengineers.core.util.Logger;
import co.uk.genesisengineers.core.util.Vector2Df;
import co.uk.genesisengineers.core.visualisation.Texture;
import co.uk.genesisengineers.core.visualisation.TextureRegion;
import co.uk.genesisengineers.core.visualisation.Visualisation;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DrawableTextureArray implements  DrawableArray{

    private int id;
    private Texture texture;
    private List<TextureRegion> textureRegionList;
    private Shape shape;
    private Vector2Df dimensions;

    public DrawableTextureArray(int id, Texture texture, List<TextureRegion> textureRegionList, Shape shape, Vector2Df dimensions){
        this.id = id;
        this.texture = texture;
        this.textureRegionList = textureRegionList;
        this.shape = shape;
        this.dimensions = dimensions;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation) {
        draw(coordinates, dimensions, rotation, 0);
    }

    @Override
    public int size(){
        return textureRegionList.size();
    }

    @Override
    public void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation, int index) {
        if(index <0 || index >= textureRegionList.size()){
            Logger.error("DrawableTextureArray draw out of bounds texture.id = "+texture.getId(), new Exception());
        }
        Visualisation visualisation = Visualisation.getInstance();
        visualisation.useTextureProgram();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        shape.preDraw(visualisation);

        glPushMatrix();
        glTranslatef(coordinates.x, coordinates.y, 0);
        glScalef(dimensions.x, dimensions.y, 0);
        glRotatef(rotation, 0f, 0f, 1f);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        textureRegionList.get(index).bind();
        shape.draw(visualisation);

        shape.postDraw(visualisation);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    @Override
    public Vector2Df getDimensions() {
        return dimensions;
    }
}
