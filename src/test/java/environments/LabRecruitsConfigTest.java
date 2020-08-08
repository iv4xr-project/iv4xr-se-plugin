package environments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.uu.cs.aplib.utils.Pair;

//import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.* ;
import java.lang.reflect.Modifier;

public class LabRecruitsConfigTest {

    private static Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

    /**
     * Check that the json encoding of Config looks ok.
     */
    @Test
    public void JsonTest(){
        LabRecruitsConfig config  = new LabRecruitsConfig("minimal")
                . removeSwitchToDoorLinks(new Pair<>("button1", "door1"))
                . replaceSeed(52572723);

        //GymEnvironment env = new GymEnvironment(config);
        
        // Print the json encoding, and check it visually :|
        String json = gson.toJson(config) ;

        // some sanity checks:
        assertTrue(json != null) ;
        System.out.println(json);

        LabRecruitsConfig config2 = gson.fromJson(json, LabRecruitsConfig.class) ;
        
        assertTrue(config2.seed == config.seed) ;
        assertTrue(config2.remove_links.contains(new Pair<>("button1", "door1"))) ;

    }
}
