package drawable;

import util.Vector2Df;

public interface DrawableArray {
    void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation, int index);
    int size();
}
