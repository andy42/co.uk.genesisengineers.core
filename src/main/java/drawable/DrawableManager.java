package drawable;

import com.sun.javafx.geom.Vec3f;
import drawable.shape.Shape;
import drawable.shape.ShapeManager;
import util.Vector2Df;

import java.util.HashMap;
import java.util.Map;

public class DrawableManager {

    private Map<Integer, Drawable> drawableMap = new HashMap<>();

    public void load(ShapeManager shapeManager){
//        Drawable newDrawable = new DrawableColor(new Vec3f(0,0,1), shapeManager.getShape(R.s));
//        addDrawable(0, newDrawable);
    }

    public void addDrawable(int drawableId, Drawable drawable){
        drawableMap.put(drawableId, drawable);
    }

    private Drawable getDrawable(int drawableId){
        return drawableMap.get(drawableId);
    }

    public void draw(int drawableId, Vector2Df coordinates, Vector2Df dimensions, float rotation){
        Drawable drawable = getDrawable(drawableId);
        if(drawable == null) return;

        drawable.draw(coordinates, dimensions, rotation);
    }
}
