package entity.component;

public abstract class ComponentBase {

    public enum Type {
        POSITION,
        BASIC_COLOURED_SQUARE,
        MOVEMENT,
        KEYBOARD_CONTROLLER,
        COLLISION,
        MAP_SQUARE,
        BASIC_TEXTURED_SQUARE
    }

    protected Type type;

    public Type getType () {
        return type;
    }
}
