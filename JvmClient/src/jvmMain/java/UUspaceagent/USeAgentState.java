package UUspaceagent;


//import eu.iv4xr.framework.mainConcepts.W3DAgentState;
import environments.SeEnvironment;
import eu.iv4xr.framework.extensions.pathfinding.AStar;
import eu.iv4xr.framework.extensions.pathfinding.Pathfinder;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.agents.State ;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Observation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static UUspaceagent.SEBlockFunctions.fromSEVec3;

public class USeAgentState extends State {

    public String agentId ;
    public WorldModel wom ;
    public Grid2DNav grid2D = new Grid2DNav() ;
    public Pathfinder<Pair<Integer,Integer>> pathfinder2D = new AStar<>() ;
    public List<Pair<Integer,Integer>> currentPathToFollow = new LinkedList<>();

    public USeAgentState(String agentId) {
        this.agentId = agentId ;
    }

    @Override
    public SeEnvironment env() {
        return (SeEnvironment) super.env() ;
    }

    List<String> getAllBlockIDs() {
        List<String> blockIds = new LinkedList<>() ;
        for(var e : wom.elements.values()) {
            for(var b : e.elements.values()) {
                if (b.type.equals("block")) {
                    blockIds.add(b.id) ;
                }
            }
        }
        return blockIds ;
    }

    WorldEntity agentAdditionalInfo(CharacterObservation obs) {
        WorldEntity agentWE = new WorldEntity(this.agentId, "agentMoreInfo", true) ;
        agentWE.properties.put("orientationForward", fromSEVec3(obs.getOrientationForward())) ;
        agentWE.properties.put("orientationUp", fromSEVec3(obs.getOrientationUp())) ;
        agentWE.properties.put("jetpackRunning", obs.getJetpackRunning()) ;
        agentWE.properties.put("healthRatio", obs.getHealthRatio()) ;
        agentWE.properties.put("targetBlock", obs.getTargetBlock().getId()) ;
        return agentWE ;
    }

    @Override
    public void updateState() {

        super.updateState();

        // get the new WOM. Currently it does not include agent's extended properties, so we add them
        // explicitly here:
        WorldModel newWom = env().observe() ;
        CharacterObservation agentObs = env().getController().getObserver().observe() ;
        newWom.elements.put(this.agentId, agentAdditionalInfo(agentObs)) ;

        // The obtained wom also does not include blocks observed. So we get them explicitly here:
        // Well, we will get ALL blocks. Note that S=some blocks may change state or disappear,
        // compared to what the agent currently has it its state.wom.
        Observation gridsAndBlocksStates = env().getController().getObserver().observeBlocks() ;

        if(grid2D.origin == null) {
            // TODO .. we should also reset the grid if the agent flies to a new plane.
            grid2D.resetGrid(newWom.position);
        }
        if(wom == null) {
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
        }
        // updating the "navigational-2DGrid:
        List<String> toBeRemoved = grid2D.allObstacleIDs.stream()
                .filter(id -> ! getAllBlockIDs().contains(id))
                .collect(Collectors.toList());
        // removing obstacles that no longer exist:
        for(var id : toBeRemoved) {
            grid2D.removeObstacle(id);
        }
        // adding newly observed blocks:
        // TODO: this assumes doors are initially closed. Calculating blocked squares
        // for open-doors is more complicated. TODO.
        Observation newBlocks = env().getController().getObserver().observeNewBlocks() ;
        for(var gr : newBlocks.getGrids()) {
            grid2D.addObstacle(gr.getBlocks());
        }
        // updating dynamic blocking-state: (e.g. handling doors)
        // TODO!
    }

    // bunch of getters:

    Vec3 orientationForward() {
        return (Vec3) wom.elements.get(agentId).properties.get("orientationForward") ;
    }

    float healthRatio() {
        return (float) wom.elements.get(agentId).properties.get("healthRatio") ;
    }

    boolean jetpackRunning() {
        return (boolean) wom.elements.get(agentId).properties.get("jetpackRunning") ;
    }



}
