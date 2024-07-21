package uuspaceagent;

import environments.SeEnvironment;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.agents.State ;
import spaceEngineers.model.Block;
import spaceEngineers.model.CharacterObservation;
import uuspaceagent.exploration.AStarExplore;
import uuspaceagent.exploration.Explorable;
import uuspaceagent.exploration.PathExplorer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static uuspaceagent.SEBlockFunctions.fromSEVec3;

public abstract class UUSeAgentState<NodeId> extends State {
    // Make sure this is always the same value as in C/Users/<NAME>/AppData/Roaming/SpaceEngineers/iv4xr-plugin.config
    public static float OBSERVATION_RADIUS = 50.0f;

    public String agentId ;
    public WorldModel wom ;
    public PathExplorer<NodeId> pathfinder = new AStarExplore<>();
    public List<NodeId> currentPathToFollow = new LinkedList<>();

    public List<WorldEntity> doors = new ArrayList<>();
    public List<WorldEntity> buttons = new ArrayList<>();

    /**
     * SE does not seem to send time-stamp, so we will keep track the number of state-updates
     * as a replacement of time-stamp.
     */
    long updateCount = 0 ;

    public UUSeAgentState(String agentId) {
        this.agentId = agentId ;
    }

    @Override
    public SeEnvironment env() {
        return (SeEnvironment) super.env() ;
    }

    WorldEntity agentAdditionalInfo(CharacterObservation obs) {
        Block targetBlock = obs.getTargetBlock();
        WorldEntity agentWE = new WorldEntity(this.agentId, "agentMoreInfo", true) ;
        agentWE.properties.put("orientationForward", fromSEVec3(obs.getOrientationForward())) ;
        agentWE.properties.put("orientationUp", fromSEVec3(obs.getOrientationUp())) ;
        agentWE.properties.put("jetpackRunning", obs.getJetpackRunning()) ;
        agentWE.properties.put("health", obs.getHealth()) ;
        agentWE.properties.put("targetBlock", targetBlock == null ? null : targetBlock.getId()) ;
       //System.out.println(">>> constructing extra info for agent") ;
        return agentWE ;
    }

    public abstract NodeId getGridPos(Vec3 targetLocation);
    public abstract Vec3 getBlockCenter(Vec3 targetLocation);
    public abstract Vec3 getBlockCenter(NodeId targetLocation);
    public abstract Vec3 getOrigin();
    public abstract Explorable<NodeId> getGrid();

    @Override
    public void updateState(String agentId) {

        super.updateState(agentId);

    }

    public abstract void updateDoors();

    // bunch of getters:

    Vec3 orientationForward() {
        return (Vec3) wom.elements.get(agentId).properties.get("orientationForward") ;
    }

    Vec3 orientationUp() {
        return (Vec3) wom.elements.get(agentId).properties.get("orientationUp") ;
    }

    /**
     * Adds half the player height to the location (the feet) in the up direction.
     * @return Center position of the player
     */
    Vec3 centerPos() {
        return Vec3.add(wom.position, Vec3.mul(orientationUp(), 0.9f)); // half of the agent height (1.8 m)
    }

    WorldEntity targetBlock() {
        var targetId = wom.elements.get(agentId).getStringProperty ("targetBlock") ;
        if (targetId == null) return null ;
        return SEBlockFunctions.findWorldEntity(wom,targetId) ;
    }

    float healthRatio() {
        return (float) wom.elements.get(agentId).properties.get("healthRatio") ;
    }

    boolean jetpackRunning() {
        return (boolean) wom.elements.get(agentId).properties.get("jetpackRunning") ;

    }

    // helper functions

    static void assignTimeStamp(WorldModel wom, long time) {
        wom.timestamp = time ;
        for(var e : wom.elements.values()) {
            e.assignTimeStamp(time);
        }
    }
}
