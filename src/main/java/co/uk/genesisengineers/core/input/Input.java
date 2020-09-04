package co.uk.genesisengineers.core.input;

/**
 * @author Danny Weatherley <danny.f.weatherley@gmail.com>
 */
public abstract class Input {

    long window;

    Input () {
    }

    public abstract void init (long window);

    void setWindow (long window) {
        this.window = window;
    }
}
