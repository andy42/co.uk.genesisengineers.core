package visualisation;

import util.Logger;

import java.util.Vector;

public class TextureManager {

    private Vector<Texture> textureList = new Vector<>();
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

    public Texture addTexture (String fileName) {
        Texture texture = new Texture();
        if (texture.init(fileName) == true) {
            texture.setId(this.textureList.size());
            this.textureList.add(texture);
            return texture;
        }
        return null;
    }

    public Texture getTexture (int index) {
        return textureList.get(index);
    }

    public void bind (int textureId) {
        for (Texture texture : textureList) {
            if (texture.getId() == textureId) {
                texture.bind();
                return;
            }
        }
        Logger.error("bindTexture not found.");
    }

    public void bind (int textureId, int textureRegionIndex) {
        for (Texture texture : textureList) {
            if (texture.getId() == textureId) {
                texture.bindTextureRegion(textureRegionIndex);
                texture.bind();
                return;
            }
        }
        Logger.error("bindTexture not found.");
    }

    public void deleteTextures () {
        for (Texture texture : textureList) {
            texture.delete();
        }
    }
}


