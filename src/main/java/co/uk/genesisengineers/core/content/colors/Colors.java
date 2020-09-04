package co.uk.genesisengineers.core.content.colors;

import co.uk.genesisengineers.core.content.Asset;
import co.uk.genesisengineers.core.content.Context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Colors {

    private ColorsFactoryJson colorsFactoryJson = new ColorsFactoryJson();

    private Map<String, ColorTheme> colorThemeMap = new HashMap<>();

    public void loadRes(Context context, int resId) {
        Asset asset = context.getResources().getAsset(resId);
        switch (asset.fileType){
            case "json":
                colorsFactoryJson.loadRes(context, this, resId);
        }
    }

    private ColorTheme getColorTheme(String themeName){
        ColorTheme theme = colorThemeMap.get(themeName);
        if(theme == null){
            colorThemeMap.put(themeName, new ColorTheme());
            theme = colorThemeMap.get(themeName);
        }
        return theme;
    }

    public Color getColor(int colorId, String themeName){
        return getColorTheme(themeName).getColor(colorId);
    }

    public void addColor(int id, String colorHex, String themeName){
        getColorTheme(themeName).addColor(id, colorHex);
    }

    public Collection<Color> getColorList(String themeName){
        return colorThemeMap.get(themeName).getColorList();
    }

    private class ColorTheme{
        private Map<Integer, Color> colorMap = new HashMap<>();

        public Color getColor(int colorId){
            return colorMap.get(colorId);
        }

        public void addColor(int colorId, String colorHex){
            colorMap.put(colorId, new Color(colorId, colorHex));
        }

        public Collection<Color> getColorList(){
            return colorMap.values();
        }
    }
}
