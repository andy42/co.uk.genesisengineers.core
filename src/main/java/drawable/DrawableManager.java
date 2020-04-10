package drawable;

import content.Asset;
import content.Context;
import drawable.json.DrawableFactoryJson;
import shape.Shape;
import shape.ShapeManager;
import util.Vector2Df;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawableManager {

    private DrawableFactoryJson drawableFactoryJson;

    public DrawableManager(ShapeManager shapeManager){
        drawableFactoryJson = new DrawableFactoryJson(shapeManager);
    }

    private Map<Integer, Drawable> drawableMap = new HashMap<>();

    public void load(Context context, List<Asset> assets){

        for(Asset asset : assets){
            switch (asset.fileType){
                case "json":
                    Drawable drawable =  drawableFactoryJson.loadRes(context, asset.id);
                    addDrawable(asset.id, drawable);
            }
        }
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
