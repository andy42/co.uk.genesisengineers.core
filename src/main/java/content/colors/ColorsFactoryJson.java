package content.colors;

import content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import util.FileLoader;
import util.Logger;

import java.io.File;

public class ColorsFactoryJson {

    public ColorsFactoryJson(){
    }

    public void loadRes(Context context, Colors colors, int assetId) {

        File file = context.getResources().getAssetFile(assetId);
        if (file == null) return;
        String fileString = FileLoader.loadFileAsString(file);

        parseJson(colors, assetId, new JSONArray(fileString));
    }

    private void parseJson(Colors colors, int assetId, JSONArray jsonArray) {
        try {
            for(int i=0; i < jsonArray.length(); i++){
                parseTheme(colors, assetId,  jsonArray.getJSONObject(i));
            }
        }
        catch (Exception e){
            String errorMessage = "parseJson resId : "+assetId+", "+e.getMessage();
            Logger.exception(new Exception(errorMessage, e), errorMessage);
        }
    }

    private void parseTheme(Colors colors, int assetId, JSONObject jsonObject){
        try {
            String themeName = jsonObject.getString("theme");
            JSONArray colorArray = jsonObject.getJSONArray("colors");
            for(int i=0; i< colorArray.length(); i++){
                parseColor(colors, assetId, themeName, colorArray.getJSONObject(i));
            }
        }
        catch (Exception e){
            String errorMessage = "parseTheme resId : "+assetId+", "+e.getMessage();
            Logger.exception(new Exception(errorMessage, e), errorMessage);
        }
    }

    private void parseColor(Colors colors, int assetId, String theme, JSONObject jsonObject){
        try {
            int id = jsonObject.getInt("id");
            String value = jsonObject.getString("value");
            colors.addColor(id, value, theme);
        }
        catch (Exception e){
            String errorMessage = "parseColor resId : "+assetId+", "+e.getMessage();
            Logger.exception(new Exception(errorMessage, e), errorMessage);
        }
    }
}
