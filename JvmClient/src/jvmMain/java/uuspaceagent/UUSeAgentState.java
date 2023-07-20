package uuspaceagent;


//import eu.iv4xr.framework.mainConcepts.W3DAgentState;
import environments.SeEnvironment;
import environments.SeEnvironmentKt;
import eu.iv4xr.framework.extensions.pathfinding.AStar;
import eu.iv4xr.framework.extensions.pathfinding.Pathfinder;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.agents.State ;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Block;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Observation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static uuspaceagent.SEBlockFunctions.fromSEVec3;

public class UUSeAgentState extends State {

    public String agentId ;
    public WorldModel wom ;
    public NavGrid navgrid = new NavGrid() ;
    public Pathfinder<DPos3> pathfinder2D = new AStar<>() ;
    public List<DPos3> currentPathToFollow = new LinkedList<>();

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

    //@Override
    public void updateState() {

        super.updateState(agentId);

        // get the new WOM. Currently it does not include agent's extended properties, so we add them
        // explicitly here:
        WorldModel newWom = env().observe() ;
        // HACK: SE gives generated-id to the agent, replace that:
        newWom.agentId = this.agentId ;
        // HACK: because wom that comes from SE has its wom.elements read-only :|
        var origElements = newWom.elements ;
        newWom.elements = new HashMap<>() ;
        for (var e : origElements.entrySet()) newWom.elements.put(e.getKey(),e.getValue()) ;
        CharacterObservation agentObs = env().getController().getObserver().observe() ;
        newWom.elements.put(this.agentId, agentAdditionalInfo(agentObs)) ;
        assignTimeStamp(newWom,updateCount) ;

        // The obtained wom also does not include blocks observed. So we get them explicitly here:
        // Well, we will get ALL blocks. Note that S=some blocks may change state or disappear,
        // compared to what the agent currently has it its state.wom.
        Observation rawGridsAndBlocksStates = env().getController().getObserver().observeBlocks() ;
        WorldModel gridsAndBlocksStates = SeEnvironmentKt.toWorldModel(rawGridsAndBlocksStates) ;
        // HACK: make the grids and blocks marked as dynamic elements. SE sends them as non-dymanic
        // that will cause them to be ignored by mergeObservation.
        SEBlockFunctions.hackForceDynamicFlag(gridsAndBlocksStates) ;
        // assign a fresh timestamp too:
        assignTimeStamp(gridsAndBlocksStates,updateCount) ;
        for(var e : gridsAndBlocksStates.elements.entrySet()) {
            newWom.elements.put(e.getKey(), e.getValue()) ;
        }
        // updating the count:
        updateCount++ ;

        if(navgrid.origin == null) {
            // TODO .. we should also reset the grid if the agent flies to a new plane.
            navgrid.resetGrid(newWom.position);
        }
        if(wom == null) {
            // this is the first observation
            wom = newWom ;
        }
        else {
            // MERGING the two woms:
            wom.mergeNewObservation(newWom) ;

            // HOWEVER, some blocks and grids-of-blocks may have been destroyed, hence
            // do not exist anymore. We need to remove them from state.wom. This is handled
            // below.
            // First, remove disappearing "cube-grids" (composition of blocks)
            List<String> tobeRemoved = wom.elements.keySet().stream()
                    .filter(id -> ! newWom.elements.keySet().contains(id))
                    .collect(Collectors.toList());
            for(var id : tobeRemoved) wom.elements.remove(id) ;
            // Then, we remove disappearing blocks (from grids that remain):
            for(var cubegridOld : wom.elements.values()) {
                var cubeGridNew = newWom.elements.get(cubegridOld.id) ;
                tobeRemoved.clear();
                tobeRemoved = cubegridOld.elements.keySet().stream()
                        .filter(blockId -> ! cubeGridNew.elements.keySet().contains(blockId))
                        .collect(Collectors.toList());
                for(var blockId : tobeRemoved) cubegridOld.elements.remove(blockId) ;
            }

            // updating the "navigational-2DGrid:
            var blocksInWom =  SEBlockFunctions.getAllBlockIDs(wom) ;
            List<String> toBeRemoved = navgrid.allObstacleIDs.stream()
                    .filter(id -> !blocksInWom.contains(id))
                    .collect(Collectors.toList());
            // first, removing obstacles that no longer exist:
            for(var id : toBeRemoved) {
                navgrid.removeObstacle(id);
            }
        }

        // then, there may also be new blocks ... we add them to the nav-grid:
        // TODO: this assumes doors are initially closed. Calculating blocked squares
        // for open-doors is more complicated. TODO.
        for(var block : SEBlockFunctions.getAllBlocks(gridsAndBlocksStates)) {
            navgrid.addObstacle(block);
            // check if it is a door, and get its open/close state:
            Boolean isOpen = SEBlockFunctions.geSlideDoorState(block) ;
            if (isOpen != null) {
                navgrid.setObstacleBlockingState(block,! isOpen);
            }
        }
        // updating dynamic blocking-state: (e.g. handling doors)
        // TODO!
    }

    // bunch of getters:

    Vec3 orientationForward() {
        return (Vec3) wom.elements.get(agentId).properties.get("orientationForward") ;
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
