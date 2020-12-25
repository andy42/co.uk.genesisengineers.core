package co.uk.genesisengineers.core.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class EntityPool {
    private Stack<Entity> entityStack = new Stack<>();
    private List<Entity> activeEntityList = new LinkedList<>();

    public EntityPool(){

    }

    public EntityPool(List<Entity> newEntityList){
        addNewEntites(newEntityList);
    }

    public void addNewEntity(Entity entity){
        entityStack.add(entity);
        entity.setActive(false);
    }

    public void addNewEntites(List<Entity> newEntityList){
        this.entityStack.addAll(newEntityList);
        for(Entity entity : newEntityList){
            entity.setActive(false);
        }
    }

    public Entity getNewEntity(){
        if(entityStack.size() == 0){
            return null;
        }

        Entity entity = entityStack.pop();
        activeEntityList.add(entity);
        entity.setActive(true);
        return entity;
    }

    public void discardEntity(Entity entity){
        entity.setActive(false);
        activeEntityList.remove(entity);
        entityStack.push(entity);
    }

    public void discardAll(){
        for(Entity entity : activeEntityList){
            entity.setActive(false);
        }
        entityStack.addAll(activeEntityList);
        activeEntityList.clear();
    }
}
