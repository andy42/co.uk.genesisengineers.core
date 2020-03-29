package content;

import util.ResourceLoader;

import java.io.*;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }
    public String getAssetFilePath(int assetId){
        return assetsMap.getFilePath(assetId);
    }
}
