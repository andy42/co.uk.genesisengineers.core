package system;

import events.Event;

public interface EventListener {
    boolean dispatchEvent(Event event);
}
