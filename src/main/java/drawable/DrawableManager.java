package drawable;

import content.Asset;
import content.Context;
import content.colors.Color;
import drawable.json.DrawableFactoryJson;
import shape.Shape;
import shape.ShapeManager;
import ui.util.AttributeParser;
import util.Logger;
import util.Vector2Df;
import visualisation.Texture;
import visualisation.TextureManager;
import visualisation.TextureRegion;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawableManager {

    private DrawableFactoryJson drawableFactoryJson;

    private static DrawableManager s_instance = null;

    public static DrawableManager createInstance (ShapeManager shapeManager, TextureManager textureManager) {
        if (s_instance == null) {
            s_instance = new DrawableManager(shapeManager,textureManager);
        }
        return s_instance;
    }

    public static DrawableManager getInstance () {
        if (s_instance == null) {
            throw new RuntimeException("DrawableManager getInstance called without calling createInstance");
        }
        return s_instance;
    }


    private DrawableManager(ShapeManager shapeManager, TextureManager textureManager){
        drawableFactoryJson = new DrawableFactoryJson(shapeManager,textureManager );
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

    //used to create drawables from colors so you do not have to define them all individually
    public void createColorDrawables(Collection<Color> colors, Shape shape){
        for(Color color : colors){
            addDrawable(color.id, new DrawableColor(AttributeParser.colorFromString(color.hexValue), shape));
        }
    }

    //used to create drawables from Textures so you do not have to define them all individually
    public void createTextureDrawables(Collection<Texture> textures, Shape shape){
        for(Texture texture : textures){
            try {
                Vector2Df dimensions= new Vector2Df(texture.getWidth(), texture.getHeight());
                addDrawable(texture.getId(), new DrawableTexture(texture, new TextureRegion(texture, new Vector2Df(0, 0), dimensions), shape, dimensions));
            }
            catch (Exception e){
                Logger.exception(e, e.getMessage());
            }
        }
    }

    public void addDrawable(int drawableId, Drawable drawable){
        drawableMap.put(drawableId, drawable);
    }

    public Drawable getDrawable(Integer drawableId){
        return drawableMap.get(drawableId);
    }

    public void draw(int drawableId, Vector2Df coordinates, Vector2Df dimensions, float rotation){
        Drawable drawable = getDrawable(drawableId);
        if(drawable == null) return;

        drawable.draw(coordinates, dimensions, rotation);
    }
}
