package co.uk.genesisengineers.core.drawable;

import co.uk.genesisengineers.core.util.Vector2Df;

public interface DrawableArray extends Drawable{
    void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation, int index);
    int size();
}
