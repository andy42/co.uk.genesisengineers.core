package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

public class Position extends ComponentBase {
    private Vector2Df coordinates = new Vector2Df();
    private float rotation = 0;

    public Position (float x, float y) {
        this();
        coordinates.x = x;
        coordinates.y = y;
    }

    public Position (float x, float y, float rotation) {
        this();
        this.coordinates.x = x;
        this.coordinates.y = y;
        this.rotation = rotation;
    }

    public Position (Vector2Df coordinates, float rotation) {
        this();
        this.coordinates = coordinates;
        this.rotation = rotation;
    }

    public Position (float rotation) {
        this();
        this.rotation = rotation;
    }

    public Position (ComponentAttributes componentAttributes) {
        this();
        this.coordinates = componentAttributes.getVector2Df("coordinates", coordinates);
        this.rotation = componentAttributes.getFloat("rotation", 0f);
    }

    @Override
    public ComponentBase clone() {
        return new Position(
                coordinates.copy(),
                rotation
        );
    }

    public Position () {
        this.type = Type.POSITION;
    }

    public void setCoordinates (Vector2Df coordinates) {
        this.coordinates = coordinates;
    }

    public void setRotation (float rotation) {
        this.rotation = rotation;
    }

    public float getRotation () {
        return this.rotation;
    }

    public Vector2Df getCoordinates () {
        return coordinates;
    }

}
