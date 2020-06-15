package co.uk.genesisengineers.core.clock;

public class Clock {

    private double lastUpdateRealTime;
    private double currentTime = 0;
    private float timeModifier = 1f;
    private boolean paused = false;
    private boolean setup = false;

    public Clock () {

    }

    public void update (double realTime) {
        if (setup == false) {
            setup = true;
            lastUpdateRealTime = realTime;
        }

        //if we have a large skip in time, the amount progressed i limited to the last update
        if ((realTime - lastUpdateRealTime) > 2) {
            lastUpdateRealTime = realTime;
        }

        if (paused == false) {
            currentTime += (realTime - lastUpdateRealTime) * timeModifier;
        }

        lastUpdateRealTime = realTime;
    }

    public double getTime () {
        return currentTime;
    }

    public boolean isPaused () {
        return paused;
    }

    public void setPaused (boolean paused) {
        this.paused = paused;
    }

    public float getTimeModifier () {
        return timeModifier;
    }

    public void setTimeModifier (float timeModifier) {
        this.timeModifier = timeModifier;
    }
}
