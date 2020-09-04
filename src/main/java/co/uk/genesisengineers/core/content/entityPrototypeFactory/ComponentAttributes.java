package co.uk.genesisengineers.core.content.entityPrototypeFactory;

import co.uk.genesisengineers.core.util.Vector2Df;

public interface ComponentAttributes {
    String getStringValue(String key, String defaultValue);
    Integer getIntValue(String key, Integer defaultValue);
    Float getFloat(String key, Float defaultValue);
    Boolean getBoolean(String key, Boolean defaultValue);
    Vector2Df getVector2Df(String key, Vector2Df defaultValue);
}
