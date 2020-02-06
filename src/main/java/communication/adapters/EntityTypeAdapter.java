package communication.adapters;

import com.google.gson.*;
import world.EntityType;

import java.lang.reflect.Type;

public class EntityTypeAdapter implements JsonDeserializer<EntityType>, JsonSerializer<EntityType> {
    @Override
    public EntityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //System.out.println("des:" + json.getAsInt());
        return EntityType.values()[json.getAsInt()];
    }

    @Override
    public JsonElement serialize(EntityType src, Type typeOfSrc, JsonSerializationContext context) {
        switch (src){
            case Entity:
                return new JsonPrimitive(0);
            case Dynamic:
                return new JsonPrimitive(1);
            default:
                return new JsonPrimitive(2);
        }
    }
}
