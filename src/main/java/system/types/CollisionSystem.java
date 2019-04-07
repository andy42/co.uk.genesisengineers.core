package system.types;

import entity.Entity;
import entity.EntityHandler;
import entity.component.ComponentBase;
import entity.component.types.Collision;
import entity.component.types.Movement;
import entity.component.types.Position;
import system.SystemBase;
import util.CollisionBox;

import java.util.ArrayList;

public class CollisionSystem extends SystemBase {

    private ArrayList<Entity> entityList = new ArrayList<>();

    public CollisionSystem () {
        this.type = Type.COLLISION;
    }

    @Override
    public void init (EntityHandler entityHandler) {
        for (Entity entity : entityHandler) {
            if (entity.hasComponent(ComponentBase.Type.POSITION) && entity.hasComponent(ComponentBase.Type.COLLISION)) {
                entityList.add(entity);
            }
        }
    }

    @Override
    public void update () {
        Position position;
        Collision collision;

        for (Entity entity : entityList) {
            position = (Position) entity.getComponent(ComponentBase.Type.POSITION);
            collision = (Collision) entity.getComponent(ComponentBase.Type.COLLISION);

            if (position == null || collision == null) {
                continue;
            }

            CollisionBox entityCollisionBox = new CollisionBox();
            entityCollisionBox.init(position.getCoordinates(), collision.getHalfDimensions());
            if (objectCollision(entity, entityCollisionBox) == true) {
                Movement movement = (Movement) entity.getComponent(ComponentBase.Type.MOVEMENT);
                if (movement != null) {
                    movement.resetPosition();
                }
            }
        }
    }

    private boolean objectCollision (Entity entity, CollisionBox entityCollisionBox) {
        Position position;
        Collision collision;

        CollisionBox secondCollisionBox = new CollisionBox();

        for (Entity secondEntity : entityList) {
            if (entity.getId() == secondEntity.getId()) {
                continue;
            }
            position = (Position) secondEntity.getComponent(ComponentBase.Type.POSITION);
            collision = (Collision) secondEntity.getComponent(ComponentBase.Type.COLLISION);
            if (position == null || collision == null) {
                continue;
            }

            secondCollisionBox.init(position.getCoordinates(), collision.getHalfDimensions());
            if (entityCollisionBox.boxCollisionTest(secondCollisionBox) == true) {
                return true;
            }
        }
        return false;
    }
}
