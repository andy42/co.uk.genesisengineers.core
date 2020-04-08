package system.types;

import com.sun.javafx.geom.Vec3f;
import entity.Entity;
import entity.EntityHandler;
import entity.component.ComponentBase;
import entity.component.types.BasicColouredSquare;
import entity.component.types.Collision;
import entity.component.types.Position;
import entity.component.types.Select;
import input.MotionEvent;
import system.MotionEventListener;
import system.SystemBase;
import util.CollisionBox;
import visualisation.Visualisation;

import java.util.ArrayList;

public class MouseSelectSystem extends SystemBase implements MotionEventListener {

    private ArrayList<Entity> entityList = new ArrayList<>();

    @Override
    public void init(EntityHandler entityHandler) {
        for (Entity entity : entityHandler) {
            if (entity.hasComponent(ComponentBase.Type.POSITION)
                    && entity.hasComponent(ComponentBase.Type.SELECT)
                    && entity.hasComponent(ComponentBase.Type.COLLISION)) {
                entityList.add(entity);
            }
        }
    }

    @Override
    public void update() {
        Visualisation visualisation = Visualisation.getInstance();
        visualisation.useColourProgram();

        Position position = null;
        BasicColouredSquare basicSquare = null;
        Select select = null;

        for (Entity entity : entityList) {
            position = (Position) entity.getComponent(ComponentBase.Type.POSITION);
            select =  (Select) entity.getComponent(ComponentBase.Type.SELECT);

            if(select.isSelected()){
                visualisation.drawColouredSquare(new Vec3f(0f,0f,0f), position.getCoordinates(),select.getDimensions(),0 );
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {

        Position position;
        Select select;
        Collision collision;

        for (Entity entity : entityList) {
            position = (Position) entity.getComponent(ComponentBase.Type.POSITION);
            select = (Select) entity.getComponent(ComponentBase.Type.SELECT);
            collision = (Collision) entity.getComponent(ComponentBase.Type.COLLISION);

            CollisionBox entityCollisionBox = new CollisionBox();
            entityCollisionBox.init(position.getCoordinates(), collision.getHalfDimensions());
            if(entityCollisionBox.pointCollisionTest(motionEvent.getPosition())){
                select.setSelected(true);
            } else {
                select.setSelected(false);
            }
        }

        return false;
    }
}
