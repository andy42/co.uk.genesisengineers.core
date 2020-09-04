package co.uk.genesisengineers.core.system;

import co.uk.genesisengineers.core.entity.EntityHandler;
import co.uk.genesisengineers.core.events.Event;
import co.uk.genesisengineers.core.input.KeyEvent;
import co.uk.genesisengineers.core.input.MotionEvent;

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

    public boolean dispatchKeyEvent(KeyEvent keyEvent){
        for (SystemBase system : systemList) {
            if(system instanceof KeyEventListener){
                ((KeyEventListener) system).dispatchKeyEvent(keyEvent);
            }
        }
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent){
        for (SystemBase system : systemList) {
            if(system instanceof MotionEventListener){
                ((MotionEventListener) system).dispatchTouchEvent(motionEvent);
            }
        }
        return false;
    }

    public boolean dispatchEvent(Event event){
        for (SystemBase system : systemList) {
            if(system instanceof EventListener){
                ((EventListener) system).dispatchEvent(event);
            }
        }
        return false;
    }
}
