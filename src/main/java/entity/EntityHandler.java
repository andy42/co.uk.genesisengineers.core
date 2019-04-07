package entity;

import java.util.ArrayList;
import java.util.Iterator;
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
