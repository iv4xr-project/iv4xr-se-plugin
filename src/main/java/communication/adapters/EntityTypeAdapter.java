package communication.adapters;

import com.google.gson.*;
import world.LegacyEntityType;

import java.lang.reflect.Type;

public class EntityTypeAdapter implements JsonDeserializer<LegacyEntityType>, JsonSerializer<LegacyEntityType> {
    @Override
    public LegacyEntityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //System.out.println("des:" + json.getAsInt());
        return LegacyEntityType.values()[json.getAsInt()];
    }

    @Override
    public JsonElement serialize(LegacyEntityType src, Type typeOfSrc, JsonSerializationContext context) {
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
