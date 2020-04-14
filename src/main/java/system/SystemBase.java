package system;

import entity.EntityHandler;

public abstract class SystemBase {
    public enum Type {
        RENDER_TEXTURE,
        RENDER_COLOUR,
        RENDER_DRAWABLE,
        MOVEMENT,
        KEYBOARD_CONTROLLER,
        COLLISION,
        MAP_RENDER
    }

    protected Type type;

    public Type getType () {
        return type;
    }

    public abstract void init (EntityHandler entityHandler);

    public abstract void update ();
}
