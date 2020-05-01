package ui.util;

import com.sun.javafx.geom.Vec3f;
import content.Context;
import content.colors.Color;
import ui.view.ViewGroup;
import util.Logger;
import util.Vector2Df;

public class AttributeParser {


    public static int getLayoutDimension (AttributeSet attrs, String key, int defaultValue) {
        String value = attrs.getAttributeValue(null, key);
        if (value == null) {
            return defaultValue;
        }

        if (value.equalsIgnoreCase("MATCH_PARENT")) {
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


    public static Vec3f getColor (Context context, AttributeSet attrs, String key, String defaultValue) {
        String value = attrs.getAttributeValue(null, key);

        if(value != null  && value.length() > 0 && value.charAt(0) == '#'){
            return colorFromString(value);
        }
        else if(value != null ) {
            Color color = context.getResources().getColor(Integer.parseInt(value));
            if (color != null) {
                return colorFromString(context.getResources().getColor(Integer.parseInt(value)).hexValue);
            }
        }

        if ( defaultValue != null) {
            return colorFromString(defaultValue);
        }
        return null;
    }

    public static Vec3f getColor (Context context,AttributeSet attrs, String key) {
        return getColor(context, attrs, key, "#000000");
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

    public static Integer getInteger (AttributeSet attrs, String key, Integer defaultValue) {
        try {
            return Integer.parseInt( attrs.getAttributeValue(null, key));
        } catch (Exception e){
            return defaultValue;
        }
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
    public static Float getFloat (AttributeSet attrs, String key) {
        return getFloat(attrs, key, 0f);
    }
    public static Float getFloat (AttributeSet attrs, String key, Float defaultValue) {
        try {
            return Float.parseFloat( attrs.getAttributeValue(null, key));
        } catch (Exception e){
            return defaultValue;
        }
    }

    public static Vector2Df getVector2DfFromString(String value, Vector2Df defaultValue){
        if(value == null){
            return null;
        }
        try {
            String[] values = value.split(",");
            if(values.length == 2){
                return new Vector2Df(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
            }
        } catch (Exception e){
            return defaultValue;
        }
        return defaultValue;
    }

    public static Vector2Df getVector2Df(AttributeSet attrs, String key, Vector2Df defaultValue){
        try {
            String value = attrs.getAttributeValue(null, key);
            return getVector2DfFromString(value, defaultValue);
        } catch (Exception e){
            return defaultValue;
        }
    }
}
