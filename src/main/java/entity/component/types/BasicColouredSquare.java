package entity.component.types;

import com.sun.javafx.geom.Vec3f;
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
