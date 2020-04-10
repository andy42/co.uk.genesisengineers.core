package content.entityPrototypeFactory;

import entity.component.ComponentBase;
import entity.component.types.*;
import util.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

//subclass ComponentFactory & Override createComponent to add your own componentTypes
public class ComponentFactory {

    private static final HashMap<String, Constructor<? extends ComponentBase>> sConstructorMap = new HashMap<String, Constructor<? extends ComponentBase>>();
    private static final Class<?>[] mConstructorSignature = new Class[]{ComponentAttributes.class};
    private final Object[] mConstructorArgs = new Object[1];

    private boolean verifyClassLoader (Constructor<? extends ComponentBase> constructor) {
        final ClassLoader constructorLoader = constructor.getDeclaringClass().getClassLoader();
        ClassLoader cl = getClass().getClassLoader();
        do {
            if (constructorLoader == cl) {
                return true;
            }
            cl = cl.getParent();
        } while (cl != null);
        return false;
    }

    private ComponentBase createComponentFromClassName(String className, ComponentAttributes attrs) {
        Constructor<? extends ComponentBase> constructor = sConstructorMap.get(className);
        if (constructor != null && !verifyClassLoader(constructor)) {
            constructor = null;
            sConstructorMap.remove(className);
        }

        Class<? extends ComponentBase> clazz = null;

        try {
            if (constructor == null) {
                clazz = getClass().getClassLoader().loadClass(className).asSubclass(ComponentBase.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(className, constructor);
            }

            Object[] args = mConstructorArgs;
            args[0] = attrs;

            final ComponentBase instance = constructor.newInstance(args);
            return instance;

        } catch (Exception e) {
            Logger.exception(e, e.getMessage());
        }
        return null;
    }

    public ComponentBase createComponent(String componentType, ComponentAttributes attrs) {
        switch (componentType){
            case "BasicColouredSquare":
                return new BasicColouredSquare(attrs);
            case "BasicTexturedSquare":
                return new BasicTexturedSquare(attrs);
            case "Collision":
                return new Collision(attrs);
            case "KeyboardController":
                return new KeyboardController(attrs);
            case "MapSquare":
                return new MapSquare(attrs);
            case "Movement":
                return new Movement(attrs);
            case "Position":
                return new Position(attrs);
            case "Select":
                return new Select(attrs);
        }
        return createComponentFromClassName(componentType, attrs);
    }
}
