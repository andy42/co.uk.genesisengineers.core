package visualisation;

import content.Asset;
import content.Context;
import drawable.Drawable;
import util.Logger;

import java.util.*;

public class TextureManager {

    private Map<Integer, Texture> textureMap = new HashMap<>();
    private Texture activeTexture = new Texture();

    private static TextureManager s_instance = null;

    public static TextureManager getInstance () {
        if (s_instance == null) {
            s_instance = new TextureManager();
        }
        return s_instance;
    }

    private TextureManager () {
    }

    public void load(Context context, List<Asset> assets){
        for(Asset asset : assets){
            switch (asset.fileType){
                case "png":
                    addPngTexture(context, asset);
                    break;
                default:
                    Logger.error("TextureManager Asset type not supported : "+asset.name);
            }
        }
    }

    public Texture addPngTexture (Context context, Asset asset) {
        Texture texture = new Texture();
        texture.setId(asset.id);
        if (texture.initPng(context, asset.id) == true) {
            textureMap.put(asset.id, texture);
            return texture;
        }
        return null;
    }

    public Texture getTexture (int resId) {
        return textureMap.get(resId);
    }

    public void bind (int textureId) {
        Texture texture = textureMap.get(textureId);
        if(texture != null){
            texture.bind();
        } else {
            Logger.exception(new Exception("bindTexture Error"),"bindTexture not found, textureId : "+textureId);
        }
    }


    public void bind (int textureId, int textureRegionIndex) {
        Texture texture = textureMap.get(textureId);
        if(texture != null){
            texture.bind();
            texture.bindTextureRegion(textureRegionIndex);
        } else {
            Logger.exception(new Exception("bindTexture Error"), "bindTexture not found, textureId :"+textureId+ ", textureRegionIndex : "+textureRegionIndex);
        }
    }

    public void deleteTextures () {
        for (Texture texture : textureMap.values()) {
            texture.delete();
        }
    }

    public Collection<Texture> getTextures(){
        return textureMap.values();
    }
}


