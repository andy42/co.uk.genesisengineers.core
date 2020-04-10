package drawable.json;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import drawable.Drawable;
import drawable.DrawableColor;
import org.json.JSONObject;
import shape.Shape;
import shape.ShapeManager;
import ui.util.AttributeParser;
import util.FileLoader;

import java.io.File;

public class DrawableFactoryJson {

    private static final String SHAPE_KEY = "shape";

    private static final String TYPE_KEY = "type";
    private static final String COLOR_KEY = "color";

    private static final String DRAWABLE_COLOR_KEY = "drawableColor";

    private ShapeManager shapeManager;

    public DrawableFactoryJson(ShapeManager shapeManager){
        this.shapeManager= shapeManager;
    }

    public Drawable loadRes(Context context, int resId) {
        File file =context.getResources().getAssetFile(resId);
        if(file == null) return null;
        String fileString = FileLoader.loadFileAsString(file);

        return loadRes(new JSONObject(fileString));
    }

    private Drawable loadRes(JSONObject jsonObject) {
        switch (jsonObject.getString(TYPE_KEY)){
            case DRAWABLE_COLOR_KEY:
                Vec3f color = AttributeParser.colorFromString(jsonObject.getString(COLOR_KEY));
                Shape shape = shapeManager.getShape( jsonObject.getInt(SHAPE_KEY));
                return new DrawableColor(color, shape);
        }
        return null;
    }
}
