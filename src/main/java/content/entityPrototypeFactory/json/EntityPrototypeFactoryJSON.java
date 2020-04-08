package content.entityPrototypeFactory.json;

import content.entityPrototypeFactory.ComponentFactory;
import content.entityPrototypeFactory.EntityPrototypeFactory;
import content.entityPrototypeFactory.json.ComponentAttributesJSON;
import entity.Entity;
import entity.component.ComponentBase;
import org.json.JSONArray;
import org.json.JSONObject;

public class EntityPrototypeFactoryJSON extends EntityPrototypeFactory {

    private ComponentFactory componentFactory;

    public EntityPrototypeFactoryJSON(){
        componentFactory = new ComponentFactory();
    }

    public EntityPrototypeFactoryJSON(ComponentFactory componentFactory){
        componentFactory = componentFactory;
    }

    @Override
    public boolean loadEntities(String data) {
        if(data == null) return false;

        JSONArray jsonArray = new JSONArray(data);
        if(jsonArray == null) return false;

        for(int i=0; i <jsonArray.length(); i++ ){
            JSONObject entityJson =  jsonArray.getJSONObject(i);
            Entity entity = createEntity(entityJson);
            entityMap.put(entityJson.getString("id"), entity);
        }
        return true;
    }

    private Entity createEntity(JSONObject entityJson){

        JSONArray componentJSONArray = entityJson.getJSONArray("components");

        Entity  entity = new Entity(idIndex++);
        for(int i=0; i <componentJSONArray.length(); i++ ){
            ComponentBase component = createComponent(componentJSONArray.getJSONObject(i));
            if(component == null) continue;
            entity.addComponent(component);
        }
        return entity;
    }

    private ComponentBase createComponent(JSONObject componentJSON){
        String componentType = componentJSON.getString("type");
        return componentFactory.createComponent(componentType, new ComponentAttributesJSON(componentJSON));
    }
}
