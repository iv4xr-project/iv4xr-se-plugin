package USE;


//import eu.iv4xr.framework.mainConcepts.W3DAgentState;
import environments.SeEnvironment;
import environments.SeEnvironmentKt;
import eu.iv4xr.framework.extensions.pathfinding.AStar;
import eu.iv4xr.framework.extensions.pathfinding.Pathfinder;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.agents.State ;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Observation;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class USeAgentState extends State {

    public String agentId ;
    public WorldModel wom ;
    public Grid2DNav grid2D = new Grid2DNav() ;
    public Pathfinder<Pair<Integer,Integer>> pathfinder2D = new AStar<>() ;
    public List<Pair<Integer,Integer>> currentPathToFollow ;

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

    @Override
    public void updateState() {

        super.updateState();

        // get blocks... well, all blocks, since some blocks may change state or disaapear.
        Observation primObs = env().getController().getObserver().observeBlocks() ;
        WorldModel newWom = env().observe() ;
        if(grid2D.origin == null) {
            // TODO .. we should also reset the grid if the agent flies to a new plane.
            grid2D.resetGrid(newWom.position);
        }
        if(wom == null) {
            wom = newWom ;
        }
        else {
            wom.mergeNewObservation(newWom) ;
            // remove disappearing "cube-grids" (composition of blocks)
            List<String> tobeRemoved = wom.elements.keySet().stream()
                    .filter(id -> ! newWom.elements.keySet().contains(id))
                    .collect(Collectors.toList());
            for(var id : tobeRemoved) wom.elements.remove(id) ;
            // remove disappearing blocks:
            for(var cubegridOld : wom.elements.values()) {
                var cubeGridNew = newWom.elements.get(cubegridOld.id) ;
                tobeRemoved.clear();
                tobeRemoved = cubegridOld.elements.keySet().stream()
                        .filter(blockId -> ! cubeGridNew.elements.keySet().contains(blockId))
                        .collect(Collectors.toList());
                for(var blockId : tobeRemoved) cubegridOld.elements.remove(blockId) ;
            }
        }
        // updating the 2DGrid:
        List<String> toBeRemoved = grid2D.allObstacleIDs.stream()
                .filter(id -> ! getAllBlockIDs().contains(id))
                .collect(Collectors.toList());
        // removing obstacles that no longer exist:
        for(var id : toBeRemoved) {
            grid2D.removeObstacle(id);
        }
        // adding newly observed blocks:
        Observation newBlocks = env().getController().getObserver().observeNewBlocks() ;
        for(var gr : newBlocks.getGrids()) {
            grid2D.addObstacle(gr.getBlocks());
        }
        // updating dynamic blocking-state:
        // TODO!
    }

}
