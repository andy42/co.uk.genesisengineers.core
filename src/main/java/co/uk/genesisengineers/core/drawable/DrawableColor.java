package co.uk.genesisengineers.core.drawable;

import com.sun.javafx.geom.Vec3f;
import co.uk.genesisengineers.core.shape.Shape;
import co.uk.genesisengineers.core.util.Vector2Df;
import co.uk.genesisengineers.core.visualisation.Visualisation;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;

public class DrawableColor implements Drawable {

    private int id;
    private Vec3f color;
    private Shape shape;
    private Vector2Df dimensions = new Vector2Df(1,1);

    public DrawableColor(int id, Vec3f color, Shape shape){
        this.id = id;
        this.color = color;
        this.shape = shape;
    }

    @Override
    public int getId() {
        return id;
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

        glColor3f(color.x, color.y, color.z);
        shape.draw(visualisation);

        shape.postDraw(visualisation);
        glPopMatrix();
    }

    @Override
    public Vector2Df getDimensions() {
        return dimensions;
    }
}
