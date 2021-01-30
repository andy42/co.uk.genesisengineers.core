package co.uk.genesisengineers.core.drawable;

import co.uk.genesisengineers.core.shape.Shape;
import co.uk.genesisengineers.core.util.Vector2Df;
import co.uk.genesisengineers.core.visualisation.Texture;
import co.uk.genesisengineers.core.visualisation.TextureRegion;
import co.uk.genesisengineers.core.visualisation.Visualisation;

import static org.lwjgl.opengl.GL11.*;

public class DrawableTexture implements Drawable {

    private int id;
    private Texture texture;
    private TextureRegion textureRegion;
    private Shape shape;
    private Vector2Df dimensions;

    public DrawableTexture(int id, Texture texture, TextureRegion textureRegion, Shape shape, Vector2Df dimensions){
        this.id = id;
        this.texture = texture;
        this.textureRegion = textureRegion;
        this.shape = shape;
        this.dimensions = dimensions;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation) {
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

        textureRegion.bind();
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
