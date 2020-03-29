package system.types;

import com.sun.javafx.geom.Vec3f;
import entity.Entity;
import entity.EntityHandler;
import entity.component.ComponentBase;
import entity.component.types.BasicTexturedSquare;
import entity.component.types.Position;
import entity.component.types.Select;
import system.SystemBase;
import visualisation.Visualisation;

import java.util.ArrayList;

public class RenderTextureSystem extends SystemBase {

    private ArrayList<Entity> entityList = new ArrayList<Entity>();

    public RenderTextureSystem () {
        this.type = SystemBase.Type.RENDER_TEXTURE;
    }

    @Override
    public void init (EntityHandler entityHandler) {
        for (Entity entity : entityHandler) {
            if (entity.hasComponent(ComponentBase.Type.POSITION) && entity.hasComponent(ComponentBase.Type.BASIC_TEXTURED_SQUARE)) {
                entityList.add(entity);
            }
        }
    }

    @Override
    public void update () {
        Visualisation visualisation = Visualisation.getInstance();
        visualisation.useTextureProgram();


        Position position = null;
        BasicTexturedSquare basicSquare = null;
        for (Entity entity : entityList) {
            position = (Position) entity.getComponent(ComponentBase.Type.POSITION);
            basicSquare = (BasicTexturedSquare) entity.getComponent(ComponentBase.Type.BASIC_TEXTURED_SQUARE);

            visualisation.drawTexturedSquare(basicSquare.getTextureId(), basicSquare.getTextureRegionIndex(), position.getCoordinates(), basicSquare.getDimensions(), position.getRotation());
        }
    }
}
