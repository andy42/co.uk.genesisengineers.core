package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

public class Select extends ComponentBase {
    private Vector2Df dimensions = new Vector2Df();
    private int textureId = 0;
    private int textureRegionIndex = 0;
    private boolean selected = false;

    public Select(){
        this.type = Type.SELECT;
    }

    public Select(Vector2Df dimensions, int textureId, int textureRegionIndex, boolean selected){
        this();
        this.dimensions = dimensions;
        this.textureId = textureId;
        this.textureRegionIndex = textureRegionIndex;
        this.selected =selected;
    }

    public Select (ComponentAttributes componentAttributes) {
        this(
                componentAttributes.getVector2Df("dimensions", new Vector2Df(1,1)),
                componentAttributes.getIntValue("textureId", 0),
                componentAttributes.getIntValue("textureRegionIndex", 0),
                componentAttributes.getBoolean("selected", false)
        );
    }

    @Override
    public ComponentBase clone() {
        return new Select(
                dimensions.copy(),
                textureId,
                textureRegionIndex,
                selected
        );
    }

    public Vector2Df getDimensions() {
        return dimensions;
    }


    public int getTextureId() {
        return textureId;
    }

    public int getTextureRegionIndex() {
        return textureRegionIndex;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
