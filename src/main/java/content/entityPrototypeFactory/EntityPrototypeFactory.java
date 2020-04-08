package content.entityPrototypeFactory;

import entity.Entity;
import util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class EntityPrototypeFactory {

    protected HashMap<String, Entity> entityMap = new HashMap<>();
    protected int idIndex = 0;

    public abstract boolean loadEntities(String data);

    public Entity getEntityId(String id) {
        return entityMap.get(id);
    }

    public Entity cloneEntity(String id) {
        Entity entity = getEntityId(id);
        if(entity == null) return null;
        return entity.clone(0);
    }

    public List<Entity> cloneEntity(String id, int count) {
        Entity entity = getEntityId(id);
        if(entity == null) {
            Logger.error("EntityPrototypeFactory cloneEntity Entity id "+id+" not found");
            return new ArrayList<>();
        }

        List<Entity> entities = new ArrayList<>();
        for(int i = 0; i<count; i++){
            entities.add(entity.clone(i));
        }
        return entities;
    }
}
