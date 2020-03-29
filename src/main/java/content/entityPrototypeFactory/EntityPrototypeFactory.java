package content.entityPrototypeFactory;

import entity.Entity;

import java.io.File;
import java.util.List;

public interface EntityPrototypeFactory {
    boolean loadEntities(String data);
    Entity getEntityId(String id);
    Entity cloneEntity(String id);
    List<Entity> cloneEntity(String id, int count);
}
