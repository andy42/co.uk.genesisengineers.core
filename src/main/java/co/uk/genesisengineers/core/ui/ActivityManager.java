package co.uk.genesisengineers.core.ui;

import co.uk.genesisengineers.core.clock.ClockHandler;
import co.uk.genesisengineers.core.content.Context;
import co.uk.genesisengineers.core.input.KeyEvent;
import co.uk.genesisengineers.core.input.MotionEvent;
import co.uk.genesisengineers.core.ui.activity.Activity;

import java.util.*;

public class ActivityManager {

    private Context context;
    private static ActivityManager s_instance = null;
    private boolean windowHasFocus = false;

    public static ActivityManager createInstance(Context context){
        if (s_instance == null) {
            s_instance = new ActivityManager(context);
        }
        return s_instance;
    }

    public static ActivityManager getInstance () {
        if (s_instance == null) {
            throw new RuntimeException("ActivityManager must be created in main");
        }
        return s_instance;
    }

    private ActivityManager (Context context) {
        this.context = context;
    }

    private LinkedList<Activity> activityList = new LinkedList<>();

    public void addActivity (Activity activity) {
        activity.setBaseContext(context);
        activityList.add(activity);
        activity.onCreate();
        activity.onMeasure(500, 500);
        activity.onLayout(500, 500, 0,0);
        activity.onViewCreated();
    }

    public void update () {
        Double time = ClockHandler.getInstance().getClock(ClockHandler.Type.SYSTEM_CLOCK).getTime();

        for (Activity activity : activityList) {
            //activity.touchEvent(Mouse.getInstance().getPosition());
            activity.update(time);
        }

    }

    public void renderActivityList () {

        Iterator<Activity> lit = activityList.descendingIterator();
        while (lit.hasNext()){
            lit.next().render();
        }
    }

    public Boolean dispatchKeyEvent (KeyEvent keyEvent){
        if(activityList.size() == 0){
            return false;
        }
        else if(windowHasFocus){
            return activityList.getFirst().dispatchKeyEvent(keyEvent);
        }
        return false;
    }

    public Boolean dispatchTouchEvent (MotionEvent motionEvent) {

        if(activityList.size() == 0){
            return false;
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_SCROLL){
            for (Activity activity : activityList) {
                if(activity.pointCollisionTest(motionEvent)){
                    windowHasFocus = true;
                    activityList.remove(activity);
                    activityList.addFirst(activity);
                    return activity.dispatchTouchEvent(motionEvent);
                }
            }
            windowHasFocus = false;
            return false;
        }
        else if(windowHasFocus){
            return activityList.get(0).dispatchTouchEvent(motionEvent);
        }
        else return false;
    }
}
