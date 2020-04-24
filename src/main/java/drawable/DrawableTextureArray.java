package drawable;

import shape.Shape;
import util.Logger;
import util.Vector2Df;
import visualisation.Texture;
import visualisation.TextureRegion;
import visualisation.Visualisation;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class DrawableTextureArray implements Drawable, DrawableArray{

    private Texture texture;
    private List<TextureRegion> textureRegionList;
    private Shape shape;

    public DrawableTextureArray(Texture texture, List<TextureRegion> textureRegionList, Shape shape){
        this.texture = texture;
        this.textureRegionList = textureRegionList;
        this.shape = shape;
    }

    @Override
    public void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation) {
        draw(coordinates, dimensions, rotation, 0);
    }

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
        glPopMatrix();
    }
}
