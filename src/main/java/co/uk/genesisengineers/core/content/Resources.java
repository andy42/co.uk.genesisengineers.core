package co.uk.genesisengineers.core.content;

import co.uk.genesisengineers.core.content.colors.Color;
import co.uk.genesisengineers.core.content.colors.Colors;
import co.uk.genesisengineers.core.util.ResourceLoader;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Resources {

    AssetsMap assetsMap = new AssetsMap();
    private Colors colors = new Colors();
    private String theme = "default";

    public Resources(){
    }

    public boolean init(){
        return assetsMap.init();
    }

    public File getAssetFile(int assetId){
        String filePath = assetsMap.getFilePath(assetId);
        return ResourceLoader.getFile(filePath);
    }

    public String getAssetFileAsString(int assetId){
        File file = getAssetFile(assetId);

        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            while (line != null) {
                sb.append(line);
                line = buf.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }
    public String getAssetFilePath(int assetId){
        return assetsMap.getFilePath(assetId);
    }

    public List<Integer>  getAssetIdsOfType(int typeId){
        return assetsMap.getAssetsOfType(typeId).stream().
                collect(Collectors.mapping(Asset::getId, Collectors.toList()));
    }

    public List<Asset>  getAssetsOfType(int typeId){
        return assetsMap.getAssetsOfType(typeId);
    }

    public Asset  getAsset(int id){
        return assetsMap.getAsset(id);
    }
    
    public int getAssetId(String assetId){
        return assetsMap.getAssetId(assetId);
    }
    
    public String getAssetIdString(int id){
        Asset asset =  assetsMap.getAsset(id);
        if(asset == null){
            return null;
        }
        return asset.assetId;
    }

    public void loadColors(Context context, int assetId){
        colors.loadRes(context, assetId);
    }
    
    public Color getColor(int colorId, String themeName){
        return colors.getColor(colorId, themeName);
    }

    public Color getColor(int colorId){
        return getColor(colorId, theme);
    }

    public Collection<Color> getColorList(String themeName){
        return colors.getColorList(themeName);
    }

    public Collection<Color> getColorList(){
        return getColorList(theme);
    }

}
