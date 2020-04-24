package drawable.json;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import drawable.Drawable;
import drawable.DrawableColor;
import drawable.DrawableTexture;
import drawable.DrawableTextureArray;
import org.json.JSONObject;
import shape.Shape;
import shape.ShapeManager;
import ui.util.AttributeParser;
import util.FileLoader;
import util.Logger;
import util.Vector2Df;
import visualisation.Texture;
import visualisation.TextureManager;
import visualisation.TextureRegion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DrawableFactoryJson {

    private static final String SHAPE_KEY = "shape";

    private static final String TYPE_KEY = "type";
    private static final String COLOR_KEY = "color";
    private static final String TEXTURE_KEY = "texture";

    private static final String DRAWABLE_COLOR_KEY = "drawableColor";
    private static final String DRAWABLE_TEXTURE_KEY = "drawableTexture";
    private static final String DRAWABLE_TEXTURE_ARRAY_KEY = "drawableTextureArray";

    private ShapeManager shapeManager;
    private TextureManager textureManager;

    public DrawableFactoryJson(ShapeManager shapeManager, TextureManager textureManager) {
        this.shapeManager = shapeManager;
        this.textureManager = textureManager;
    }

    public Drawable loadRes(Context context, int resId) {
        File file = context.getResources().getAssetFile(resId);
        if (file == null) return null;
        String fileString = FileLoader.loadFileAsString(file);

        return loadRes(resId, new JSONObject(fileString));
    }

    private Drawable loadRes(int resId, JSONObject jsonObject) {
        Texture texture;
        Shape shape;

        try {
            switch (jsonObject.getString(TYPE_KEY)) {

                case DRAWABLE_COLOR_KEY:
                    Vec3f color = AttributeParser.colorFromString(jsonObject.getString(COLOR_KEY));
                    shape = shapeManager.getShape(jsonObject.getInt(SHAPE_KEY));
                    return new DrawableColor(color, shape);

                case DRAWABLE_TEXTURE_KEY:
                    texture = textureManager.getTexture(jsonObject.getInt(TEXTURE_KEY));
                    shape = shapeManager.getShape(jsonObject.getInt(SHAPE_KEY));
                    return new DrawableTexture(texture, new TextureRegion(texture, new Vector2Df(0, 0), new Vector2Df(texture.getWidth(), texture.getHeight())), shape);

                case DRAWABLE_TEXTURE_ARRAY_KEY:
                    texture = textureManager.getTexture(jsonObject.getInt(TEXTURE_KEY));
                    shape = shapeManager.getShape(jsonObject.getInt(SHAPE_KEY));
                    return new DrawableTextureArray(texture, createTextureRegionList(texture, jsonObject), shape);
            }
        }
        catch (Exception e){
            String errorMessage = "loadRes resId : "+resId+", "+e.getMessage();
            Logger.exception(new Exception(errorMessage, e), errorMessage);
        }
        return null;
    }

    private List<TextureRegion> createTextureRegionList(Texture texture, JSONObject jsonObject) throws Exception {
        int itemWidth = jsonObject.getInt("itemWidth");
        int itemHeight = jsonObject.getInt("itemHeight");
        int itemCount = jsonObject.getInt("itemCount");

        List<TextureRegion> regionList = new ArrayList();
        for (int i = 0; i < itemCount; i++) {
            regionList.add(new TextureRegion(texture, new Vector2Df(itemWidth * i, 0), new Vector2Df(itemWidth, itemHeight)));
        }
        return regionList;
    }
}
