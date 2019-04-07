package system;

import entity.EntityHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class SystemHandler implements Iterable<SystemBase> {
    private ArrayList<SystemBase> systemList = new ArrayList<>();

    //this  returns null for  out of bounds
    public SystemBase getSystem (int index) {
        return systemList.get(index);
    }

    public int getSize () {
        return systemList.size();
    }

    public void addSystem (SystemBase system) {
        systemList.add(system);
    }

    @Override
    public Iterator<SystemBase> iterator () {
        return systemList.listIterator();
    }

    @Override
    public void forEach (Consumer<? super SystemBase> action) {
        for (SystemBase system : systemList) {
            action.accept(system);
        }
    }

    public void init (EntityHandler entityHandler) {
        for (SystemBase system : systemList) {
            system.init(entityHandler);
        }
    }

    public void update () {
        for (SystemBase system : systemList) {
            system.update();
        }
    }
}
