package environments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;
//import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

public class EnvConfigTest {

    private static Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

    @Test
    public void JsonTest(){
        EnvironmentConfig config  = new EnvironmentConfig("minimal")
                .removeLink(new Tuple<>("button1", "door1"))
                .replaceSeed(52572723);

        //GymEnvironment env = new GymEnvironment(config);

        System.out.println(gson.toJson(config));

    }
}
