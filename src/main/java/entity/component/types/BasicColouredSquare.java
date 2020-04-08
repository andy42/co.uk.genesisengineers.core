package entity.component.types;

import com.sun.javafx.geom.Vec3f;
import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

public class BasicColouredSquare extends ComponentBase {

    private Vector2Df dimensions = new Vector2Df();
    private Vec3f rgb = new Vec3f();

    public BasicColouredSquare (Vector2Df dimensions, Vec3f rgb) {
        this();
        this.dimensions = dimensions;
        this.rgb = rgb;

    }

    public BasicColouredSquare () {
        this.type = Type.BASIC_COLOURED_SQUARE;
    }

    public BasicColouredSquare (ComponentAttributes componentAttributes) {
        this();
        this.dimensions = componentAttributes.getVector2Df("dimensions", dimensions);

        int r = componentAttributes.getIntValue("r", 1);
        int g = componentAttributes.getIntValue("g", 1);
        int b = componentAttributes.getIntValue("b", 1);
        this.rgb = new Vec3f(r,g,b);
    }

    @Override
    public ComponentBase clone() {
        return new BasicColouredSquare(
                dimensions.copy(),
                new Vec3f(rgb)
        );
    }

    public Vec3f getRgb () {
        return rgb;
    }

    public void setRgb (Vec3f rgb) {
        this.rgb = rgb;
    }

    public Vector2Df getDimensions () {
        return dimensions;
    }

    public void setDimensions (Vector2Df dimensions) {
        this.dimensions = dimensions;
    }
}
