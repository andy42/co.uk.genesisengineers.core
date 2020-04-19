package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import drawable.DrawableArray;
import drawable.DrawableManager;
import entity.component.ComponentBase;
import util.Vector2Df;

import java.util.ArrayList;

public class MapSquare extends ComponentBase {

    private Vector2Df boardDimensions = new Vector2Df(1, 1);
    private Vector2Df tileDimensions = new Vector2Df(1, 1);
    private DrawableArray drawableArray;

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

    public MapSquare (DrawableArray drawableArray, Vector2Df boardDimensions, Vector2Df tileDimensions) {
        this();

        this.boardDimensions = boardDimensions;
        this.tileDimensions = tileDimensions;
        this.drawableArray = drawableArray;

        int tileCount = (int) (boardDimensions.x * boardDimensions.y);
        for (int i = 0; i < tileCount; i++) {
            mapArray.add(new MapTile(drawableArray, 0));
        }
    }

    public MapSquare (int drawableArrayId, Vector2Df boardDimensions, Vector2Df tileDimensions) {
        this();
        DrawableArray drawableArray = (DrawableArray)DrawableManager.getInstance().getDrawable(drawableArrayId);

        this.boardDimensions = boardDimensions;
        this.tileDimensions = tileDimensions;
        this.drawableArray = drawableArray;

        int tileCount = (int) (boardDimensions.x * boardDimensions.y);
        for (int i = 0; i < tileCount; i++) {
            mapArray.add(new MapTile(drawableArray, 0));
        }
    }

    public MapSquare (ComponentAttributes componentAttributes) {
        this(
                componentAttributes.getIntValue("drawableArrayId", -1),
                componentAttributes.getVector2Df("boardDimensions", new Vector2Df(1,1)),
                componentAttributes.getVector2Df("tileDimensions", new Vector2Df(1,1))
        );
    }

    @Override
    public ComponentBase clone() {
        return new MapSquare(drawableArray, boardDimensions.copy(),tileDimensions.copy());
    }

    public void setAllTileTextures (DrawableArray drawableArray, int drawableIndex) {
        for (MapTile tile : mapArray) {
            tile.drawableArray = drawableArray;
            tile.drawableIndex = drawableIndex;
        }
    }

    public void setTileTexture (int x, int y, DrawableArray drawableArray, int drawableIndex) {
        MapTile tile = getMapTile(x, y);
        if (tile == null) {
            return;
        }
        tile.drawableArray = drawableArray;
        tile.drawableIndex = drawableIndex;
    }

    public MapTile getMapTile (int x, int y) {
        return mapArray.get(x + y * (int) boardDimensions.x);
    }

    public static class MapTile {

        public DrawableArray drawableArray;
        public int drawableIndex = 0;

        public MapTile (DrawableArray drawableArray, int drawableIndex) {
            this.drawableArray = drawableArray;
            this.drawableIndex = drawableIndex;
        }
    }
}
