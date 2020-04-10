package drawable;

import drawable.shape.Shape;
import util.Vector2Df;
import visualisation.TextureRegion;
import visualisation.Visualisation;

import static org.lwjgl.opengl.GL11.*;

public class DrawableTexture implements Drawable {

    private TextureRegion textureRegion;
    private Shape shape;

    public DrawableTexture(TextureRegion textureRegion, Shape shape){
        this.textureRegion = textureRegion;
        this.shape = shape;
    }

    @Override
    public void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation) {
        Visualisation visualisation = Visualisation.getInstance();
        visualisation.useColourProgram();

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
        glPopMatrix();
    }
}
