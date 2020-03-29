package content.entityPrototypeFactory;

import entity.Entity;
import entity.component.ComponentBase;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityPrototypeFactoryJSON implements EntityPrototypeFactory {

    private ComponentFactory componentFactory;

    private HashMap<String, Entity> entityMap = new HashMap<>();
    private int idIndex = 0;

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

    @Override
    public Entity getEntityId(String id) {
        return entityMap.get(id);
    }

    @Override
    public Entity cloneEntity(String id) {
        Entity entity = getEntityId(id);
        if(entity == null) return null;
        return entity.clone(0);
    }

    @Override
    public List<Entity> cloneEntity(String id, int count) {
        Entity entity = getEntityId(id);
        if(entity == null) {
            Logger.error("EntityPrototypeFactoryJSON cloneEntity Entity id "+id+" not found");
            return new ArrayList<>();
        }

        List<Entity> entities = new ArrayList<>();
        for(int i = 0; i<count; i++){
            entities.add(entity.clone(i));
        }
        return entities;
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
