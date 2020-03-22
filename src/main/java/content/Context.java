package content;

import util.Logger;
import util.ResourceLoader;

public class Context {

    private Resources resources = null;

    public Resources getResources(){

        //Logger.info("getResource getPath "+Context.class.getClassLoader().getResource("test.txt").getPath());
        //Logger.info("getPackage "+this.getClass().getResource());



        if(resources == null){
            resources = new Resources();
        }

        return resources;
    }

    public ClassLoader getClassLoader(){
        Logger.info("getClass = "+this.getClass().getName());
        return this.getClass().getClassLoader();
    }
}
