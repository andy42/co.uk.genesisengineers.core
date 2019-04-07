package system.types;

import entity.Entity;
import entity.EntityHandler;
import entity.component.ComponentBase;
import entity.component.types.Movement;
import input.KeyMapper;
import system.SystemBase;
import util.Vector2Df;

import java.util.ArrayList;

public class KeyboardControllerSystem extends SystemBase {

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
        KeyMapper keyMapper = KeyMapper.getInstance();

        for (Entity entity : entityList) {
            movement = (Movement) entity.getComponent(ComponentBase.Type.MOVEMENT);
            Vector2Df velocity = new Vector2Df();
            velocity.x = axisUpdater(keyMapper.isMoveRight(), keyMapper.isMoveLeft(), 100);
            velocity.y = axisUpdater(keyMapper.isMoveDown(), keyMapper.isMoveUp(), 100);
            movement.setStartVelocity(velocity);
        }
    }
}
