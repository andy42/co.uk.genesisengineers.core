package co.uk.genesisengineers.core.visualisation.font;

import java.util.Map;

public class CharacterInfo {
    protected static final int SPACE_ASCII = 32;

    private int id = 0;
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    private int xoffset = 0;
    private int yoffset = 0;
    private int xadvance = 0;
    private int page = 0;
    private int chnl = 0;

    public CharacterInfo () {

    }

    private int getValueOfVariable (Map<String, String> values, String variable) {
        return Integer.parseInt(values.get(variable));
    }

    public boolean init (Map<String, String> values) {

        this.id = getValueOfVariable(values, "id");


        this.x = getValueOfVariable(values, "x");
        this.y = getValueOfVariable(values, "y");
        this.width = getValueOfVariable(values, "width");
        this.height = getValueOfVariable(values, "height");
        this.xoffset = getValueOfVariable(values, "xoffset");
        this.yoffset = getValueOfVariable(values, "yoffset");
        this.xadvance = getValueOfVariable(values, "xadvance");
        this.page = getValueOfVariable(values, "page");
        this.chnl = getValueOfVariable(values, "chnl");

        return true;
    }

    public int getId () {
        return id;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

    public int getXoffset () {
        return xoffset;
    }

    public int getYoffset () {
        return yoffset;
    }

    public int getXadvance () {
        return xadvance;
    }

    public int getPage () {
        return page;
    }

    public int getChnl () {
        return chnl;
    }
}
