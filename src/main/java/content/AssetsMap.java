package content;

import com.esotericsoftware.jsonbeans.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Logger;
import util.ResourceLoader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetsMap {

    private Map<Integer, Asset> assetMap = new HashMap<>();

    private boolean addAsset(JSONObject jsonObject){
        try {
            Asset asset = new Asset(jsonObject);
            assetMap.put(asset.id, asset);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean init(){
        JSONObject assetsRootObject = ResourceLoader.getJsonObject("assets.json");
        if(assetsRootObject == null){
            Logger.error("assets.json missing");
            return false;
        }


        JSONArray assets = assetsRootObject.getJSONArray("files");
        if(assets == null){
            Logger.error("assets.json missing files arrayList files");
            return false;
        }

        assetMap.clear();
        for(int i=0 ; i < assets.length(); i++){
            if(addAsset(assets.getJSONObject(i)) == false){
                Logger.error("asset ["+i+"] failed to load");
            }
        }
        return true;
    }

    public Asset getAsset(int assetId){
        return assetMap.get(assetId);
    }

    public String getFilePath(int assetId){
        Asset asset = getAsset(assetId);
        if(asset == null) return null;
        return asset.filePath;
    }

    public void log(){
        for (Map.Entry<Integer,Asset> entry : assetMap.entrySet()){
            Logger.info(entry.getValue().name);
        }
    }
}
