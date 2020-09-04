package co.uk.genesisengineers.core.system;

import co.uk.genesisengineers.core.events.Event;

public interface EventListener {
    boolean dispatchEvent(Event event);
}
