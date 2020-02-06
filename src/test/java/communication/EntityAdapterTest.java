package communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import communication.adapters.*;
import helperclasses.datastructures.Vec3;
import org.junit.Test;
import world.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class EntityAdapterTest {

    @Test
    public void deserialisationTest(){

        // copy from SocketEnvironment
        Gson deserializer = new GsonBuilder()
                .registerTypeAdapter(EntityType.class, new EntityTypeAdapter())
                .registerTypeHierarchyAdapter(Entity.class, new EntityAdapter())
                .create();

        Entity[] originals = new Entity[] {
                fill(new Entity(EntityType.Entity), 1),
                fill(new DynamicEntity(), 2),
                fill(new InteractiveEntity(), 3)
        };

        String json = deserializer.toJson(originals);

        Entity[] deserializedEntities = deserializer.fromJson(json, Entity[].class);

        for(int i = 0; i < 3; i++){
            assertEquals(originals[i].getClass(), deserializedEntities[i].getClass());
            assertEquals(originals[i], deserializedEntities[i]);
        }
    }

    @Test
    public void EntityTypeAdapterTest(){
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(EntityType.class, new EntityTypeAdapter())
                .create();
        assertEquals("0", gson.toJson(EntityType.Entity));
        assertEquals("1", gson.toJson(EntityType.Dynamic));
        assertEquals("2", gson.toJson(EntityType.Interactive));
        assertEquals(EntityType.Entity, gson.fromJson(gson.toJson(EntityType.Entity), EntityType.class));
        assertEquals(EntityType.Dynamic, gson.fromJson(gson.toJson(EntityType.Dynamic), EntityType.class) );
        assertEquals(EntityType.Interactive, gson.fromJson(gson.toJson(EntityType.Interactive), EntityType.class) );
    }

    private Entity fill(Entity e, int i){
        e.position = new Vec3(i);
        e.tag = "tag" + i;
        e.id = "id" + i;
        return e;

    }

    private DynamicEntity fill(DynamicEntity e, int i){
        fill((Entity)e, i);
        e.velocity = new Vec3(i, i, i);
        return e;
    }

    private InteractiveEntity fill(InteractiveEntity e, int i){
        fill((Entity)e, i);
        e.extents = new Vec3(i, i, i);
        e.center = new Vec3(i, i, i);
        e.connectedObjects = new ArrayList<>();
        e.connectedObjects.add(""+i);
        e.isActive = true;
        return e;
    }
}
