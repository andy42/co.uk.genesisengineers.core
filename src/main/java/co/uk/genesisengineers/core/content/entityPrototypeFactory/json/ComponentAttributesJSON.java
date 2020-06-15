package co.uk.genesisengineers.core.content.entityPrototypeFactory.json;

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes;
import org.json.JSONException;
import org.json.JSONObject;
import co.uk.genesisengineers.core.util.Vector2Df;

public class ComponentAttributesJSON implements ComponentAttributes {
    JSONObject jsonObject;

    public ComponentAttributesJSON(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    @Override
    public String getStringValue(String key, String defaultValue) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e){
            return defaultValue;
        }
    }

    @Override
    public Integer getIntValue(String key, Integer defaultValue) {
        try {
            return jsonObject.getInt(key);
        } catch (JSONException e){
            return defaultValue;
        }
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        try {
            return jsonObject.getFloat(key);
        } catch (JSONException e){
            return defaultValue;
        }
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        try {
            return jsonObject.getBoolean(key);
        } catch (JSONException e){
            return defaultValue;
        }
    }

    @Override
    public Vector2Df getVector2Df(String key, Vector2Df defaultValue) {
        String value;
        try{
            value =  jsonObject.getString(key);
        }catch (JSONException e){
            return defaultValue;
        }

        String[] values = value.split(",");

        if(values.length == 2){
            return new Vector2Df(Float.parseFloat(values[0]), Float.parseFloat(values[1]));
        }
        return defaultValue;
    }
}
