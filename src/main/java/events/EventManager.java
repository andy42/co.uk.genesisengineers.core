package events;

import java.util.LinkedList;
import java.util.Queue;

public class EventManager {
    private static EventManager s_instance = null;

    private Queue<Event> eventQueue = new LinkedList();

    public static EventManager getInstance () {
        if (s_instance == null) {
            s_instance = new EventManager();
        }
        return s_instance;
    }

    private EventManager(){
    }

    public Event getNext(){
        return eventQueue.poll();
    }

    public void addEvent(Event event){
        eventQueue.add(event);
    }
}
