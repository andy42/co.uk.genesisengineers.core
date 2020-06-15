package content;

import util.Logger;
import util.ResourceLoader;

public class Context {

    private Resources resources = null;

    public Resources getResources(){
        if(resources == null){
            resources = new Resources();
            resources.init();
        }

        return resources;
    }

    public ClassLoader getClassLoader(){
        Logger.info("getClass = "+this.getClass().getName());
        return this.getClass().getClassLoader();
    }

    public int getWindowWidth(){
        return 500;
    }
    public int getWindowHeight(){
        return 500;
    }
}
