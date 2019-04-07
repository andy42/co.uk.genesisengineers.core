package ui.activity;

import com.sun.javafx.geom.Vec3f;
import ui.view.*;
import util.Logger;
import util.Vector2Df;
import visualisation.Visualisation;
import visualisation.font.Font;

public class TestActivity extends Activity implements View.OnClickListener{

    protected Button button = null;

    public TestActivity () {
        super();
        this.open = true;
    }

    @Override
    public void onCreate () {
        LayoutInflater layoutInflater = new LayoutInflater();
        this.view = layoutInflater.inflate("basic_layout.xml", null);

        this.button = (Button) this.view.findViewById("button2");
        this.button.setOnClickListener(this);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void onClick (View v) {
        if(v.getId() == this.button.getId()){
            Logger.info("onClick ");
        }
    }
}
