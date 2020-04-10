package drawable.shape;

import content.Asset;
import content.Context;
import drawable.shape.json.ShapeFactoryJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeManager {
    private ShapeFactoryJson shapeFactoryJson = new ShapeFactoryJson();

    private Map<Integer, Shape> shapeMap = new HashMap<>();
    private int shapeIndex = 0;//TODO: this should be changed to R.SHAPE.shapeId ref

    public void loadShapes(Context context, List<Asset> assets){

        for(Asset asset : assets){
            switch (asset.fileType){
                case "json":
                   Shape shape =  shapeFactoryJson.loadRes(context, asset.id);
                    addShape(asset.id, shape);
            }
        }
//
//        //TODO make this load from File
//
//        float vertices[] = {-0.5f, -0.5f, 0.0f,     // v1 bl
//                -0.5f, 0.5f, 0.0f,      // v2 tl
//                0.5f, -0.5f, 0.0f,      // v3 br
//                0.5f, 0.5f, 0.0f,       // v4 tr
//        };
//        addShape(shapeIndex++, new Shape(vertices));
    }

    private void addShape(int shapeId, Shape shape){
        shapeMap.put(shapeId, shape);
    }

    public Shape getShape(int shapeId){
        return shapeMap.get(shapeId);
    }
}
