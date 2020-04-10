package drawable.shape.json;

import content.Context;
import drawable.shape.Shape;
import drawable.shape.ShapeFactory;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import util.FileLoader;
import util.Logger;

import java.io.File;
import java.io.StringReader;

public class ShapeFactoryJson implements ShapeFactory {

    private static final String VERTICES_KEY = "vertices";
    private static final String DIMENSIONS_KEY = "dimensions";

    @Override
    public Shape loadRes(Context context, int resId) {


        File file =context.getResources().getAssetFile(resId);
        if(file == null) return null;
        String fileString = FileLoader.loadFileAsString(file);

        return loadRes(new JSONObject(fileString));
    }

    private Shape loadRes(JSONObject jsonObject) {
        String verticesString = jsonObject.getString(VERTICES_KEY);
        String[] verticesStringArray = verticesString.split(",");

        float[] verticesFloatArray = new float[verticesStringArray.length];

        try {
            for(int i=0; i < verticesStringArray.length; i++){
                verticesFloatArray[i] = Float.parseFloat(verticesStringArray[i]);
            }
            return new Shape(verticesFloatArray);
        }
        catch (NumberFormatException e){
            return null;
        }
    }
}