package ui.util;

import com.sun.javafx.geom.Vec3f;
import ui.view.ViewGroup;
import util.Logger;

import java.awt.*;

public class AttributeParser {


    public static int getLayoutDimension (AttributeSet attrs, String key, int defaultValue) {
        String value = attrs.getAttributeValue(null, key);
        if (value == null) {
            return defaultValue;
        }

        if (value.equalsIgnoreCase("FILL_PARENT")) {
            return ViewGroup.LayoutParams.FILL_PARENT;
        } else if (value.equalsIgnoreCase("MATCH_PARENT")) {
            return ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (value.equalsIgnoreCase("WRAP_CONTENT")) {
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            return getDimension(attrs, key);
        }
    }

    public static int getLayoutDimension (AttributeSet attrs, String key) {
        return getLayoutDimension(attrs, key, 0);
    }

    public static int getDimension (AttributeSet attrs, String key, int defaultValue) {
        String value = attrs.getAttributeValue(null, key);
        if (value == null) {
            return defaultValue;
        }

        //TODO: add other vlaue types e.g px/dp
        return Integer.parseInt(value);
    }

    public static int getDimension (AttributeSet attrs, String key) {
        return getDimension(attrs, key, 0);
    }


    public static Vec3f getColor (AttributeSet attrs, String key, String defaultValue) {
        String value = attrs.getAttributeValue(null, key);
        if (value == null) {
            if(defaultValue == null){
                return null;
            } else {
                return colorFromString(defaultValue);
            }
        }
        return colorFromString(value);
    }

    public static Vec3f getColor (AttributeSet attrs, String key) {
        return getColor(attrs, key, "#000000");
    }

    public static Vec3f colorFromString (String value) {
        try {
            float red = Integer.valueOf(value.substring(1, 3), 16) * 0.00392156863f;
            float green = Integer.valueOf(value.substring(3, 5), 16) * 0.00392156863f;
            float blue = Integer.valueOf(value.substring(5, 7), 16) * 0.00392156863f;
            return new Vec3f(red, green, blue);
        } catch (Exception e) {
            Logger.exception(e, "getColor value " + value);
            return null;
        }
    }

    public static String getString (AttributeSet attrs, String key, String defaultValue) {
        String value = attrs.getAttributeValue(null, key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static String getString (AttributeSet attrs, String key) {
        return getString(attrs, key, "");
    }

    public static boolean getBoolean(AttributeSet attrs, String key, boolean defaultValue){
        String value = attrs.getAttributeValue(null, key);
        if(value == null){
            return defaultValue;
        }
        else if(value.contentEquals("true")){
            return true;
        }
        else {
            return false;
        }
    }
}
