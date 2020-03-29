package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

public class BasicTexturedSquare extends ComponentBase {
    private Vector2Df dimensions = new Vector2Df();
    private int textureId = 0;
    private int textureRegionIndex = 0;

    public BasicTexturedSquare (Vector2Df dimensions, int textureId, int textureRegionIndex) {
        this();
        this.dimensions = dimensions;
        this.textureId = textureId;
        this.textureRegionIndex = textureRegionIndex;

    }

    public BasicTexturedSquare (ComponentAttributes componentAttributes) {
        this();
        this.dimensions = componentAttributes.getVector2Df("dimensions", dimensions);
        this.textureId = componentAttributes.getIntValue("textureId", 0);
        this.textureRegionIndex = componentAttributes.getIntValue("textureRegionIndex", 0);
    }

    @Override
    public ComponentBase clone() {
        return new BasicTexturedSquare(
                dimensions.copy(),
                textureId,
                textureRegionIndex
        );
    }

    public BasicTexturedSquare () {
        this.type = ComponentBase.Type.BASIC_TEXTURED_SQUARE;
    }

    public Vector2Df getDimensions () {
        return dimensions;
    }

    public void setDimensions (Vector2Df dimensions) {
        this.dimensions = dimensions;
    }

    public int getTextureId () {
        return textureId;
    }

    public int getTextureRegionIndex () {
        return textureRegionIndex;
    }
}
