package co.uk.genesisengineers.core.drawable;

import co.uk.genesisengineers.core.util.Vector2Df;

public interface Drawable {
    int getId();
    void draw(Vector2Df coordinates, Vector2Df dimensions, float rotation);
    Vector2Df getDimensions();
}
