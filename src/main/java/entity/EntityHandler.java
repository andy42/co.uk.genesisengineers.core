package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class EntityHandler implements Iterable<Entity> {
    private int index = 0;
    private ArrayList<Entity> entityList = new ArrayList<Entity>();

    //this  returns null for  out of bounds
    public Entity getEntity (int index) {
        return entityList.get(index);
    }

    public int getSize () {
        return entityList.size();
    }

    public Entity createEntity () {
        Entity entity = new Entity(index++);
        entityList.add(entity);
        return entity;
    }

    public void addEntity(Entity entity){
        if(entity == null) return;
        entity.setId(index++);
        entityList.add(entity);
    }

    public void addEntities(List<Entity> entities){
        if(entities == null) return;
        for(Entity entity : entities){
            entity.setId(index++);
            entityList.add(entity);
        }
    }

    @Override
    public Iterator<Entity> iterator () {
        return entityList.listIterator();
    }

    @Override
    public void forEach (Consumer<? super Entity> action) {
        for (Entity entity : entityList)
            action.accept(entity);
    }
}
