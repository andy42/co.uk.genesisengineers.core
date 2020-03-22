package content;

import util.Logger;
import util.ResourceLoader;

import java.io.File;
import java.net.URL;

public class Resources {

    AssetsMap assetsMap = new AssetsMap();

    public Resources(){
    }

    public boolean init(){
        return assetsMap.init();
    }

    public File getAssetFile(int assetId){
        String filePath = assetsMap.getFilePath(assetId);
        return ResourceLoader.getFile(filePath);
    }
    public String getAssetFilePath(int assetId){
        return assetsMap.getFilePath(assetId);
    }
}
