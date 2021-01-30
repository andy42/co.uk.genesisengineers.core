package co.uk.genesisengineers.core.drawable;

import co.uk.genesisengineers.core.content.Asset;
import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.content.colors.Color;
import co.uk.genesisengineers.core.drawable.json.DrawableFactoryJson;
import co.uk.genesisengineers.core.shape.Shape;
import co.uk.genesisengineers.core.shape.ShapeManager;
import co.uk.genesisengineers.core.ui.util.AttributeParser;
import co.uk.genesisengineers.core.util.Logger;
import co.uk.genesisengineers.core.util.Vector2Df;
import co.uk.genesisengineers.core.visualisation.Texture;
import co.uk.genesisengineers.core.visualisation.TextureManager;
import co.uk.genesisengineers.core.visualisation.TextureRegion;

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
            addDrawable(color.id, new DrawableColor(color.id, AttributeParser.colorFromString(color.hexValue), shape));
        }
    }

    //used to create drawables from Textures so you do not have to define them all individually
    public void createTextureDrawables(Collection<Texture> textures, Shape shape){
        for(Texture texture : textures){
            try {
                Vector2Df dimensions= new Vector2Df(texture.getWidth(), texture.getHeight());
                addDrawable(texture.getId(), new DrawableTexture(texture.getId(), texture, new TextureRegion(texture, new Vector2Df(0, 0), dimensions), shape, dimensions));
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
