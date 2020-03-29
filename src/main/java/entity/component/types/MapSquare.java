package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

import java.util.ArrayList;

public class MapSquare extends ComponentBase {

    private Vector2Df boardDimensions = new Vector2Df(1, 1);
    private Vector2Df tileDimensions = new Vector2Df(1, 1);

    private ArrayList<MapTile> mapArray = new ArrayList<>();

    public MapSquare () {
        this.type = Type.MAP_SQUARE;
    }

    public Vector2Df getBoardDimensions () {
        return this.boardDimensions;
    }

    public Vector2Df getTileDimensions () {
        return this.tileDimensions;
    }

    public Vector2Df getHalfTileDimensions () {
        return this.tileDimensions.multiply(new Vector2Df(0.5f, 0.5f));
    }

    public MapSquare (Vector2Df boardDimensions, Vector2Df tileDimensions) {
        this();

        this.boardDimensions = boardDimensions;
        this.tileDimensions = tileDimensions;

        int tileCount = (int) (boardDimensions.x * boardDimensions.y);
        for (int i = 0; i < tileCount; i++) {
            //TODO these are temp values, will us passed defaults or entire map tile values
            mapArray.add(new MapTile(1, 3));
        }
    }

    public MapSquare (ComponentAttributes componentAttributes) {
        this(
                componentAttributes.getVector2Df("boardDimensions", new Vector2Df(1,1)),
                componentAttributes.getVector2Df("tileDimensions", new Vector2Df(1,1))
        );
    }

    @Override
    public ComponentBase clone() {
        return new MapSquare(boardDimensions.copy(),tileDimensions.copy());
    }

    public void setAllTileTextures (int textureId, int textureRegionIndex) {
        for (MapTile tile : mapArray) {
            tile.textureId = textureId;
            tile.textureRegionIndex = textureRegionIndex;
        }
    }

    public void setTileTexture (int x, int y, int textureId, int textureRegionIndex) {
        MapTile tile = getMapTile(x, y);
        if (tile == null) {
            return;
        }
        tile.textureId = textureId;
        tile.textureRegionIndex = textureRegionIndex;
    }

    public MapTile getMapTile (int x, int y) {
        return mapArray.get(x + y * (int) boardDimensions.x);
    }

    public static class MapTile {


        public int textureId = 0;
        public int textureRegionIndex = 0;

        public MapTile (int textureId, int textureRegionIndex) {
            this.textureId = textureId;
            this.textureRegionIndex = textureRegionIndex;
        }
    }
}
