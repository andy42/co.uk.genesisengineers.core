package system.types;

import clock.ClockHandler;
import entity.Entity;
import entity.EntityHandler;
import entity.component.ComponentBase;
import entity.component.types.Movement;
import entity.component.types.Position;
import system.SystemBase;
import util.Vector2Df;

import java.util.ArrayList;

public class MovementSystem extends SystemBase {

    private ArrayList<Entity> entityList = new ArrayList<Entity>();

    public MovementSystem () {
        this.type = SystemBase.Type.MOVEMENT;
    }

    @Override
    public void init (EntityHandler entityHandler) {
        for (Entity entity : entityHandler) {
            if (entity.hasComponent(ComponentBase.Type.POSITION) && entity.hasComponent(ComponentBase.Type.MOVEMENT)) {
                entityList.add(entity);
            }
        }
    }

    @Override
    public void update () {
        double time = ClockHandler.getInstance().getClock(ClockHandler.Type.GAME_CLOCK).getTime();
        Position position;
        Movement movement;
        double elapseTime;
        double x;
        double y;

        for (Entity entity : entityList) {
            position = (Position) entity.getComponent(ComponentBase.Type.POSITION);
            movement = (Movement) entity.getComponent(ComponentBase.Type.MOVEMENT);
            movement.setLastPosition(movement.getStartPosition());

            elapseTime = time - movement.getStartTime();

            x = movement.getStartPosition().x + movement.getStartVelocity().x * elapseTime + 0.5d * movement.getAcceleration().x * elapseTime * elapseTime;
            y = movement.getStartPosition().y + movement.getStartVelocity().y * elapseTime + 0.5d * movement.getAcceleration().y * elapseTime * elapseTime;

            position.setCoordinates(new Vector2Df((float) x, (float) y));
            movement.setStartPosition(position.getCoordinates());
            movement.setStartTime(time);

            x = movement.getAcceleration().x * elapseTime + movement.getStartVelocity().x;
            y = movement.getAcceleration().y * elapseTime + movement.getStartVelocity().y;
            movement.setStartVelocity(new Vector2Df(x, y));
        }
    }
}
