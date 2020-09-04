package co.uk.genesisengineers.core.shape;

import co.uk.genesisengineers.core.content.Asset;
import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.shape.json.ShapeFactoryJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeManager {
    private ShapeFactoryJson shapeFactoryJson = new ShapeFactoryJson();

    private Map<Integer, Shape> shapeMap = new HashMap<>();

    public void loadShapes(Context context, List<Asset> assets){
        for(Asset asset : assets){
            switch (asset.fileType){
                case "json":
                   Shape shape =  shapeFactoryJson.loadRes(context, asset.id);
                    addShape(asset.id, shape);
            }
        }
    }

    private void addShape(int shapeId, Shape shape){
        shapeMap.put(shapeId, shape);
    }

    public Shape getShape(int shapeId){
        return shapeMap.get(shapeId);
    }
}
