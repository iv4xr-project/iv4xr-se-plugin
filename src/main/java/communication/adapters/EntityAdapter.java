package communication.adapters;

import com.google.gson.*;
import helperclasses.datastructures.Vec3;
import world.*;

import java.lang.reflect.Type;
import java.util.Arrays;

public class EntityAdapter implements JsonDeserializer<Entity>, JsonSerializer<Entity> {

    /**
     * This deserializer should deserialize Entities based on their type
     */
    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        Entity entity;
        switch ((EntityType) context.deserialize(obj.get("type"), EntityType.class)){
            case Dynamic:
                entity =  new DynamicEntity();
                break;
            case Interactive:
                entity =  new InteractiveEntity();
                break;
            default:
                entity = new Entity(EntityType.Entity);
                break;
        }

        entity.id = obj.get("id").getAsString();
        entity.tag = obj.get("tag").getAsString();
        entity.position = context.deserialize(obj.get("position"), Vec3.class);
        entity.property = obj.get("property").getAsString();

        switch (entity.type){
            case Dynamic:
                ((DynamicEntity) entity).velocity = context.deserialize(obj.get("velocity"), Vec3.class);
                break;
            case Interactive:
                InteractiveEntity ie = (InteractiveEntity) entity;
                ie.isActive = obj.get("isActive").getAsBoolean();
                ie.connectedObjects = Arrays.asList(context.deserialize(obj.get("connectedObjects"), String[].class));
                ie.center = context.deserialize(obj.get("center"), Vec3.class);
                ie.extents = context.deserialize(obj.get("extents"), Vec3.class);
                break;
        }
        return entity;
    }

    /**
     * This serializer should serialize Entities normally, but entity types should be a number
     */
    @Override
    public JsonElement serialize(Entity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add("type", context.serialize(src.type, EntityType.class));
        obj.add("id", context.serialize(src.id));
        obj.add("tag", context.serialize(src.tag));
        obj.add("position", context.serialize(src.position));
        obj.add("property", context.serialize(src.property));

        switch (src.type){
            case Dynamic:
                obj.add("velocity", context.serialize(((DynamicEntity) src).velocity));
                break;
            case Interactive:
                InteractiveEntity ie = (InteractiveEntity) src;
                obj.add("isActive", context.serialize(ie.isActive));
                obj.add("connectedObjects", context.serialize(ie.connectedObjects));
                obj.add("center", context.serialize(ie.center));
                obj.add("extents", context.serialize(ie.extents));
                break;
        }
        return obj;
    }
}
