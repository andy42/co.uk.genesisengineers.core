package system.types;

import entity.Entity;
import entity.EntityHandler;
import entity.component.ComponentBase;
import entity.component.types.Movement;
import input.KeyEvent;
import system.KeyEventListener;
import system.SystemBase;
import util.Vector2Df;
import java.util.ArrayList;

public class KeyboardControllerSystem extends SystemBase implements KeyEventListener {

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;

    private ArrayList<Entity> entityList = new ArrayList<>();

    public KeyboardControllerSystem () {
        this.type = SystemBase.Type.KEYBOARD_CONTROLLER;
    }

    @Override
    public void init (EntityHandler entityHandler) {
        for (Entity entity : entityHandler) {
            if (entity.hasComponent(ComponentBase.Type.KEYBOARD_CONTROLLER) && entity.hasComponent(ComponentBase.Type.MOVEMENT)) {
                entityList.add(entity);
            }
        }
    }

    private int axisUpdater (boolean positiveState, boolean negativeState, int maxValue) {
        if (positiveState == true && negativeState == true) {
            return 0;
        } else if (positiveState == false && negativeState == false) {
            return 0;
        } else if (positiveState == true) {
            return maxValue;
        } else {
            return -maxValue;
        }
    }

    @Override
    public void update () {
        Movement movement;

        for (Entity entity : entityList) {
            movement = (Movement) entity.getComponent(ComponentBase.Type.MOVEMENT);
            Vector2Df velocity = new Vector2Df();
            velocity.x = axisUpdater(moveRight, moveLeft, 100);
            velocity.y = axisUpdater(moveDown, moveUp, 100);
            movement.setStartVelocity(velocity);
        }
    }

    private boolean updateKey (KeyEvent keyEvent, int matchKey, boolean currentValue) {
        if (keyEvent.keyValue == matchKey && keyEvent.action == KeyEvent.ACTION_DOWN) {
            return true;
        } else if (keyEvent.keyValue == matchKey && keyEvent.action == KeyEvent.ACTION_UP) {
            return false;
        } else {
            return currentValue;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {

        this.moveUp = updateKey(keyEvent, 119, this.moveUp);
        this.moveLeft = updateKey(keyEvent, 97, this.moveLeft);
        this.moveDown = updateKey(keyEvent, 115, this.moveDown);
        this.moveRight = updateKey(keyEvent, 100, this.moveRight);

        return false;
    }
}
