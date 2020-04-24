package shape.json;

import content.Context;
import shape.Shape;
import shape.ShapeFactory;
import org.json.JSONObject;
import util.FileLoader;

import java.io.File;

public class ShapeFactoryJson implements ShapeFactory {

    private static final String VERTICES_KEY = "vertices";
    private static final String DIMENSIONS_KEY = "dimensions";
    private static final String DRAW_TYPE_KEY = "drawType";

    @Override
    public Shape loadRes(Context context, int resId) {


        File file =context.getResources().getAssetFile(resId);
        if(file == null) return null;
        String fileString = FileLoader.loadFileAsString(file);

        return loadRes(new JSONObject(fileString));
    }

    private Shape loadRes(JSONObject jsonObject) {
        String verticesString = jsonObject.getString(VERTICES_KEY);
        int drawType;
        switch (jsonObject.getString(DRAW_TYPE_KEY)){
            case "strip":
                drawType = Shape.DRAW_TYPE_STRIP;
                break;
            case "fan":
                drawType = Shape.DRAW_TYPE_FAN;
                break;
            default:
                drawType = Shape.DRAW_TYPE_STRIP;
        }

        String[] verticesStringArray = verticesString.split(",");

        float[] verticesFloatArray = new float[verticesStringArray.length];

        try {
            for(int i=0; i < verticesStringArray.length; i++){
                verticesFloatArray[i] = Float.parseFloat(verticesStringArray[i]);
            }
            return new Shape(verticesFloatArray, drawType);
        }
        catch (NumberFormatException e){
            return null;
        }
    }
}