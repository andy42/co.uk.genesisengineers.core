package ui;

import clock.ClockHandler;
import input.KeyEvent;
import input.MotionEvent;
import input.Mouse;
import ui.activity.Activity;
import ui.view.View;
import util.Logger;
import util.Vector2Df;
import visualisation.Visualisation;

import java.util.*;

public class ActivityManager {

    private static ActivityManager s_instance = null;

    public static ActivityManager getInstance () {
        if (s_instance == null) {
            s_instance = new ActivityManager();
        }
        return s_instance;
    }

    private ActivityManager () {

    }

    private LinkedList<Activity> activityList = new LinkedList<>();

    public void addActivity (Activity activity) {
        activityList.add(activity);
        activity.onCreate();
        activity.onMeasure(500, 500);
        activity.onLayout(500, 500, 0,0);
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
        return activityList.getFirst().dispatchKeyEvent(keyEvent);
    }

    public Boolean dispatchTouchEvent (MotionEvent motionEvent) {

        if(activityList.size() == 0){
            return false;
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            for (Activity activity : activityList) {
                if(activity.pointCollisionTest(motionEvent)){
                    activityList.remove(activity);
                    activityList.addFirst(activity);
                    return activity.dispatchTouchEvent(motionEvent);
                }
            }
            return false;
        }
        else {
            return activityList.get(0).dispatchTouchEvent(motionEvent);
        }
    }
}
