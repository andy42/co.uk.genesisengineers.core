package ui.activity;

import content.Context;
import ui.view.View;
import ui.view.ViewGroup;
import util.Vector2Df;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

public abstract class Fragment {
    protected int id = -1;
    protected Vector2Df dimensions = new Vector2Df(0, 0);
    protected Vector2Df position = new Vector2Df(0, 0);
    protected View view = null;
    protected Activity hostActivity;

    public Fragment(){
    }

    public void onAttach(Activity activity) {
        this.hostActivity = activity;
    }

    public void onCreate (Context context) {

    }

    public abstract View onCreateView(ViewGroup viewGroup);

    public void onViewCreated(View view){

    }

    public void onResume(){

    }

    public void onPause(){

    }

    public void onDestroyView(){

    }

    public void onDestroy(){

    }

    public void onDetach(){
        this.hostActivity = null;
    }

    public void render () {
        glPushMatrix();
        glTranslatef(position.x, position.y, 0);
        view.render();
        glPopMatrix();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Activity getActivity(){
        return hostActivity;
    }
}
