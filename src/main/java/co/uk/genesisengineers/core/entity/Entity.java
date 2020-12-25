package co.uk.genesisengineers.core.entity;

import co.uk.genesisengineers.core.entity.component.ComponentBase;

import java.util.ArrayList;

public class Entity implements Cloneable{


    private int id;
    private boolean active = true;
    private ArrayList<ComponentBase> componentList = new ArrayList<>();

    public Entity (int id) {
        this.id = id;
    }

    public Entity clone(int id) {
        Entity clone = new Entity(id);
        for (ComponentBase component : componentList) {
            clone.addComponent(component.clone());
        }
        return clone;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasComponent (int componentType) {
        return (getComponent(componentType) != null);
    }

    public boolean hasComponents (int...  componentTypes) {
        for(int componentType : componentTypes){
            if(hasComponent(componentType) == false)return false;
        }
        return true;
    }

    public boolean addComponent (ComponentBase component) {
        if (hasComponent(component.getType())) {
            return false;
        }

        componentList.add(component);
        return true;
    }

    public ComponentBase getComponent (int componentType) {
        for (ComponentBase component : componentList) {
            if (component.getType() == componentType) {
                return component;
            }
        }
        return null;
    }
}
