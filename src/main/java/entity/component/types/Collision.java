package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

public class Collision extends ComponentBase {
    private Vector2Df halfDimensions = new Vector2Df(1f, 1f);
    private boolean screenCollision = true;
    private boolean offScreenCulling = true;
    private boolean objectCollision = true;

    public Collision (Vector2Df dimensions) {
        this();
        halfDimensions = Vector2Df.multiply(dimensions, 0.5f);
    }

    public Collision (ComponentAttributes componentAttributes) {
        this();
        Vector2Df dimensions = componentAttributes.getVector2Df("dimensions", new Vector2Df(1,1));
        halfDimensions = Vector2Df.multiply(dimensions, 0.5f);
    }

    public Collision () {
        this.type = Type.COLLISION;
    }

    @Override
    public ComponentBase clone() {
        return new Collision(Vector2Df.multiply(halfDimensions, 2f));
    }

    public Vector2Df getCollisionBoxDimension () {
        return Vector2Df.multiply(this.halfDimensions, 2);
    }

    public void setCollisionBoxDimension (Vector2Df dimensions) {
        this.halfDimensions = Vector2Df.multiply(dimensions, 0.5f);
    }
    private void setHalfDimensions(Vector2Df halfDimensions){
        this.halfDimensions = halfDimensions;
    }

    public Vector2Df getHalfDimensions () {
        return this.halfDimensions;
    }

    public boolean isScreenCollision () {
        return screenCollision;
    }

    public void setScreenCollision (boolean screenCollision) {
        this.screenCollision = screenCollision;
    }

    public boolean isOffScreenCulling () {
        return offScreenCulling;
    }

    public void setOffScreenCulling (boolean offScreenCulling) {
        this.offScreenCulling = offScreenCulling;
    }

    public boolean isObjectCollision () {
        return objectCollision;
    }

    public void setObjectCollision (boolean objectCollision) {
        this.objectCollision = objectCollision;
    }
}
