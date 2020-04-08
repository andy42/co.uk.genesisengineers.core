package entity.component;

import content.entityPrototypeFactory.ComponentAttributes;

public abstract class ComponentBase{

    public enum Type {
        POSITION,
        BASIC_COLOURED_SQUARE,
        MOVEMENT,
        KEYBOARD_CONTROLLER,
        COLLISION,
        MAP_SQUARE,
        BASIC_TEXTURED_SQUARE,
        SELECT
    }

    protected Type type;

    public Type getType () {
        return type;
    }

    abstract public ComponentBase clone();

    public ComponentBase(){}
    public ComponentBase(ComponentAttributes componentAttributes) throws NoSuchMethodException{
        System.out.println( this.getClass().getName() ) ;
        this.getClass(). getConstructor (ComponentAttributes.class ) ;
    }
}
