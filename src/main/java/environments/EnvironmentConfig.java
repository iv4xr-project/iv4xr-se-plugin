package environments;

import helperclasses.Util;
import helperclasses.datastructures.Tuple;
import game.Platform;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class EnvironmentConfig {

    // will not be sent over to Unity
    public transient String host = "localhost";
    public transient int port = 8053;

    // configurations
    public int seed = 1;

    public String level_path = "";
    public String level_name = "";

    public float agent_speed = 0.13f;
    public float npc_speed = 0.11f;
    public float fire_spread = 0.02f;
    public float jump_force = 0.18f;
    public float view_distance = 10f;
    public float light_intensity = 1f; // does not seem to work ... TODO

    public ArrayList<Tuple<String, String>> add_links = new ArrayList<>();
    public ArrayList<Tuple<String, String>> remove_links = new ArrayList<>();

    public EnvironmentConfig(){}

    public EnvironmentConfig(String levelName){
        useLevel(levelName, Platform.LEVEL_PATH);
    }

    public EnvironmentConfig(String levelName, String levelFolder){
        useLevel(levelName, levelFolder);
    }

    private EnvironmentConfig useLevel(String levelName, String levelFolder){
        String fullPath = Paths.get(levelFolder, levelName + ".csv").toAbsolutePath().toString();
        Util.verifyPath(fullPath);
        this.level_path = fullPath;
        this.level_name = levelName;
        return this;
    }

    public EnvironmentConfig replaceSeed(int seed){
        this.seed = seed;
        return this;
    }

    public EnvironmentConfig replaceNPCMovementSpeed(float arg){
        this.npc_speed = arg;
        return this;
    }

    public EnvironmentConfig replaceAgentMovementSpeed(float arg){
        agent_speed = arg;
        return this;
    }

    public EnvironmentConfig replaceJumpForce(float arg){
        this.jump_force = arg;
        return this;
    }

    public EnvironmentConfig replaceAgentViewDistance(float arg){
        this.view_distance = arg;
        return this;
    }

    public EnvironmentConfig replaceLightIntensity(float arg){
        this.light_intensity = arg;
        return this;
    }

    public EnvironmentConfig replaceFireSpreadSpeed(float arg){
        this.fire_spread = arg;
        return this;
    }

    public EnvironmentConfig addLink(Tuple<String, String> link){
        this.add_links.add(link);
        return this;
    }

    public EnvironmentConfig addLinks(Tuple<String, String> ... links){
        this.add_links.addAll(Arrays.asList(links));
        return this;
    }

    public EnvironmentConfig removeLink(Tuple<String, String> link){
        this.remove_links.add(link);
        return this;
    }

    public EnvironmentConfig removeLinks(Tuple<String, String> ... links){
        this.remove_links.addAll(Arrays.asList(links));
        return this;
    }
}
