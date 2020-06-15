package co.uk.genesisengineers.core.entity.component;

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes;

public abstract class ComponentBase{

    public enum Type {
        POSITION,
        BASIC_COLOURED_SQUARE,
        BASIC_DRAWABLE,
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
