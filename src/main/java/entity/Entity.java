package entity;

import entity.component.ComponentBase;

import java.util.ArrayList;

public class Entity implements Cloneable{


    private int id;
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

    public boolean hasComponent (ComponentBase.Type componentType) {
        return (getComponent(componentType) != null);
    }

    public boolean addComponent (ComponentBase component) {
        if (hasComponent(component.getType())) {
            return false;
        }

        componentList.add(component);
        return true;
    }

    public ComponentBase getComponent (ComponentBase.Type componentType) {
        for (ComponentBase component : componentList) {
            if (component.getType() == componentType) {
                return component;
            }
        }
        return null;
    }
}
