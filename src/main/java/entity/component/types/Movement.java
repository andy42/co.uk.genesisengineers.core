package entity.component.types;

import content.entityPrototypeFactory.ComponentAttributes;
import entity.component.ComponentBase;
import util.Vector2Df;

public class Movement extends ComponentBase {

    private Vector2Df startPosition = new Vector2Df(); //this will override position.coordinates
    private Vector2Df lastPosition = new Vector2Df(); //TODO: removed & update collision system to use a better collision resolution
    private Vector2Df startVelocity = new Vector2Df();
    private Vector2Df acceleration = new Vector2Df();
    private double startTime = 0;

    public Movement (Vector2Df startPosition) {
        this();
        this.startPosition = startPosition;
    }

    public Movement (Vector2Df startPosition, Vector2Df startVelocity, Vector2Df acceleration) {
        this();
        this.lastPosition = startPosition;
        this.startPosition = startPosition;
        this.startVelocity = startVelocity;
        this.acceleration = acceleration;
        this.startTime = 0;
    }

    public Movement (ComponentAttributes componentAttributes) {
        this(
                componentAttributes.getVector2Df("startPosition", new Vector2Df(1,1)),
                componentAttributes.getVector2Df("startVelocity", new Vector2Df()),
                componentAttributes.getVector2Df("acceleration", new Vector2Df())
        );
    }

    @Override
    public ComponentBase clone() {
        return new Movement(
                startPosition.copy(),
                startVelocity.copy(),
                acceleration.copy()
        );
    }

    public Movement () {
        this.type = Type.MOVEMENT;
    }

    public void resetPosition () {
        this.startPosition = this.lastPosition;
    }

    public Vector2Df getLastPosition () {
        return lastPosition;
    }

    public void setLastPosition (Vector2Df lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Vector2Df getStartPosition () {
        return startPosition;
    }

    public void setStartPosition (Vector2Df startPosition) {
        this.startPosition = startPosition;
    }

    public Vector2Df getStartVelocity () {
        return startVelocity;
    }

    public void setStartVelocity (Vector2Df startVelocity) {
        this.startVelocity = startVelocity;
    }

    public Vector2Df getAcceleration () {
        return acceleration;
    }

    public void setAcceleration (Vector2Df acceleration) {
        this.acceleration = acceleration;
    }

    public double getStartTime () {
        return startTime;
    }

    public void setStartTime (double startTime) {
        this.startTime = startTime;
    }
}
