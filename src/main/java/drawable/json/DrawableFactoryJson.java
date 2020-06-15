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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrawableFactoryJson {

    private static final String SHAPE_KEY = "shape";
    private static final String DIMENSIONS_KEY = "dimensions";

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

    private String getStringFromJsonObject(JSONObject jsonObject, String key){
        try {
            return jsonObject.getString(key);
        }
        catch (Exception e){
            return null;
        }
    }

    private Drawable loadRes(int resId, JSONObject jsonObject) {
        Texture texture;
        Shape shape;
        Vector2Df dimensions;
        int itemWidth;
        int itemHeight;

        try {
            switch (jsonObject.getString(TYPE_KEY)) {

                case DRAWABLE_COLOR_KEY:
                    Vec3f color = AttributeParser.colorFromString(jsonObject.getString(COLOR_KEY));
                    shape = shapeManager.getShape(jsonObject.getInt(SHAPE_KEY));
                    return new DrawableColor(color, shape);

                case DRAWABLE_TEXTURE_KEY:
                    texture = textureManager.getTexture(jsonObject.getInt(TEXTURE_KEY));
                    shape = shapeManager.getShape(jsonObject.getInt(SHAPE_KEY));
                    dimensions= new Vector2Df(texture.getWidth(), texture.getHeight());
                    return new DrawableTexture(texture, new TextureRegion(texture, new Vector2Df(0, 0), dimensions), shape, dimensions);

                case DRAWABLE_TEXTURE_ARRAY_KEY:
                    texture = textureManager.getTexture(jsonObject.getInt(TEXTURE_KEY));
                    shape = shapeManager.getShape(jsonObject.getInt(SHAPE_KEY));
                    itemWidth = jsonObject.getInt("itemWidth");
                    itemHeight = jsonObject.getInt("itemHeight");
                    return new DrawableTextureArray(texture, createTextureRegionList(texture, jsonObject), shape, new Vector2Df(itemWidth, itemHeight));
            }
        }
        catch (Exception e){
            String errorMessage = "loadRes resId : "+resId+", "+e.getMessage();
            Logger.exception(new Exception(errorMessage, e), errorMessage);
        }
        return null;
    }

    private Set<Integer> returnEmptyIndexes(JSONObject jsonObject){
        Set<Integer> emptyIndexSet = new HashSet<>();
        try{
            String stringValue = jsonObject.getString("emptyIndexes");
            String[] ranges = stringValue.split(",");
            for(String range : ranges){
                String[] points = range.trim().split("-");
                if(points.length != 2) continue;

                int startPoint =Integer.parseInt(points[0]);
                int endPoint =Integer.parseInt(points[1]);

                for(int index =startPoint; index <= endPoint; index++){
                    emptyIndexSet.add(index);
                }
            }
        }
        catch (Exception e){
            return new HashSet<>();
        }
        return emptyIndexSet;
    }

    private List<TextureRegion> createTextureRegionList(Texture texture, JSONObject jsonObject) throws Exception {
        int itemWidth = jsonObject.getInt("itemWidth");
        int itemHeight = jsonObject.getInt("itemHeight");
        int itemCount = jsonObject.getInt("itemCount");
        int columns = jsonObject.getInt("columns");

        Set<Integer> emptyIndexSet = returnEmptyIndexes(jsonObject);

        List<TextureRegion> regionList = new ArrayList();
        for (int i = 0; i < itemCount; i++) {
            if(emptyIndexSet.contains(i)) continue;
            regionList.add(new TextureRegion(texture, new Vector2Df(itemWidth * (i%columns), itemHeight*(int)(i/columns)), new Vector2Df(itemWidth, itemHeight)));
        }
        return regionList;
    }
}
