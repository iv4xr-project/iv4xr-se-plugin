package communication.adapters;

import com.google.gson.*;
import helperclasses.datastructures.Vec3;
import world.*;

import java.lang.reflect.Type;
import java.util.Arrays;

public class EntityAdapter implements JsonDeserializer<LegacyEntity>, JsonSerializer<LegacyEntity> {

    /**
     * This deserializer should deserialize Entities based on their type
     */
    @Override
    public LegacyEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        LegacyEntity entity;
        switch ((LegacyEntityType) context.deserialize(obj.get("type"), LegacyEntityType.class)){
            case Dynamic:
                entity =  new LegacyDynamicEntity();
                break;
            case Interactive:
                entity =  new LegacyInteractiveEntity();
                break;
            default:
                entity = new LegacyEntity(LegacyEntityType.Entity);
                break;
        }

        entity.id = obj.get("id").getAsString();
        entity.tag = obj.get("tag").getAsString();
        entity.position = context.deserialize(obj.get("position"), Vec3.class);
        entity.property = obj.get("property").getAsString();

        switch (entity.type){
            case Dynamic:
                ((LegacyDynamicEntity) entity).velocity = context.deserialize(obj.get("velocity"), Vec3.class);
                break;
            case Interactive:
                LegacyInteractiveEntity ie = (LegacyInteractiveEntity) entity;
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
    public JsonElement serialize(LegacyEntity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add("type", context.serialize(src.type, LegacyEntityType.class));
        obj.add("id", context.serialize(src.id));
        obj.add("tag", context.serialize(src.tag));
        obj.add("position", context.serialize(src.position));
        obj.add("property", context.serialize(src.property));

        switch (src.type){
            case Dynamic:
                obj.add("velocity", context.serialize(((LegacyDynamicEntity) src).velocity));
                break;
            case Interactive:
                LegacyInteractiveEntity ie = (LegacyInteractiveEntity) src;
                obj.add("isActive", context.serialize(ie.isActive));
                obj.add("connectedObjects", context.serialize(ie.connectedObjects));
                obj.add("center", context.serialize(ie.center));
                obj.add("extents", context.serialize(ie.extents));
                break;
        }
        return obj;
    }
}
