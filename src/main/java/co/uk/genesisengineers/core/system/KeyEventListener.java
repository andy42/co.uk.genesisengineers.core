package co.uk.genesisengineers.core.system;

import co.uk.genesisengineers.core.input.KeyEvent;

public interface KeyEventListener {
    boolean dispatchKeyEvent(KeyEvent keyEvent);
}
