package communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import communication.adapters.*;
import helperclasses.datastructures.Vec3;
import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;
import world.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class EntityAdapterTest {

    @Test
    public void deserialisationTest(){

        // copy from SocketEnvironment
        Gson deserializer = new GsonBuilder()
                .registerTypeAdapter(LegacyEntityType.class, new EntityTypeAdapter())
                .registerTypeHierarchyAdapter(LegacyEntity.class, new EntityAdapter())
                .create();

        LegacyEntity[] originals = new LegacyEntity[] {
                fill(new LegacyEntity(LegacyEntityType.Entity), 1),
                fill(new LegacyDynamicEntity(), 2),
                fill(new LegacyInteractiveEntity(), 3)
        };

        String json = deserializer.toJson(originals);

        LegacyEntity[] deserializedEntities = deserializer.fromJson(json, LegacyEntity[].class);

        for(int i = 0; i < 3; i++){
        	Assertions.assertEquals(originals[i].getClass(), deserializedEntities[i].getClass());
        	Assertions.assertEquals(originals[i], deserializedEntities[i]);
        }
    }

    @Test
    public void EntityTypeAdapterTest(){
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(LegacyEntityType.class, new EntityTypeAdapter())
                .create();
        Assertions.assertEquals("0", gson.toJson(LegacyEntityType.Entity));
        Assertions.assertEquals("1", gson.toJson(LegacyEntityType.Dynamic));
        Assertions.assertEquals("2", gson.toJson(LegacyEntityType.Interactive));
        Assertions.assertEquals(LegacyEntityType.Entity, gson.fromJson(gson.toJson(LegacyEntityType.Entity), LegacyEntityType.class));
        Assertions.assertEquals(LegacyEntityType.Dynamic, gson.fromJson(gson.toJson(LegacyEntityType.Dynamic), LegacyEntityType.class) );
        Assertions.assertEquals(LegacyEntityType.Interactive, gson.fromJson(gson.toJson(LegacyEntityType.Interactive), LegacyEntityType.class) );
    }

    private LegacyEntity fill(LegacyEntity e, int i){
        e.position = new Vec3(i);
        e.tag = "tag" + i;
        e.id = "id" + i;
        return e;

    }

    private LegacyDynamicEntity fill(LegacyDynamicEntity e, int i){
        fill((LegacyEntity)e, i);
        e.velocity = new Vec3(i, i, i);
        return e;
    }

    private LegacyInteractiveEntity fill(LegacyInteractiveEntity e, int i){
        fill((LegacyEntity)e, i);
        e.extents = new Vec3(i, i, i);
        e.center = new Vec3(i, i, i);
        e.connectedObjects = new ArrayList<>();
        e.connectedObjects.add(""+i);
        e.isActive = true;
        return e;
    }
}
