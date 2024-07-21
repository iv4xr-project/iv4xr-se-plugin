package uuspaceagent;

import environments.SeEnvironmentKt;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Observation;
import uuspaceagent.AplibCopy.NavGridCopy;
import uuspaceagent.exploration.Explorable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UUSeAgentState2D extends UUSeAgentState<DPos3> {

    public NavGridCopy navgrid = new NavGridCopy() ;

    public UUSeAgentState2D(String agentId) {
        super(agentId);
    }

    @Override
    public DPos3 getGridPos(Vec3 targetLocation) {
        return navgrid.gridProjectedLocation(targetLocation);
    }

    @Override
    public Vec3 getBlockCenter(Vec3 targetLocation) {
        return navgrid.getSquareCenterLocation(navgrid.gridProjectedLocation(targetLocation));
    }

    @Override
    public Vec3 getBlockCenter(DPos3 targetLocation) {
        return navgrid.getSquareCenterLocation(targetLocation);
    }

    @Override
    public Vec3 getOrigin() {
        return navgrid.origin;
    }

    @Override
    public Explorable<DPos3> getGrid() {
        return navgrid;
    }

    @Override
    public void updateState(String agentId) {
        Timer.updateStateStart = Instant.now();

        super.updateState(agentId);

        // get the new WOM. Currently, it does not include agent's extended properties, so we add them
        // explicitly here:
        WorldModel newWom = env().observe() ;
        //System.out.println(">>>-- agent pos as received from SE:" + newWom.position);
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

            Timer.addToGridStart = Instant.now();
            for(var block : SEBlockFunctions.getAllBlocks(gridsAndBlocksStates)) {
                addToGrid(block);
            }
            Timer.endAddToGrid();
        }
        else {
            // MERGING the two woms:
            var changes = wom.mergeNewObservation(newWom) ;
            // merges the woms, but cannot easily be used for exploration because everything outside the viewing distance
            // is thrown away out of the wom

            System.out.println("========================================================");

            // Remove grids that are not in the WOM anymore
            List<String> tobeRemoved = wom.elements.keySet().stream()
                    .filter(id -> ! newWom.elements.containsKey(id))
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
            for(var cubeGridNew : changes) {
                if (Objects.equals(cubeGridNew.id, this.agentId)) continue;

                var cubegridOld = cubeGridNew.getPreviousState();

                if (cubegridOld == null) {
                    continue;
                }

                // Then, we remove disappearing blocks (from grids that changed):
                tobeRemoved.clear();
                tobeRemoved = cubegridOld.elements.keySet().stream()
                        .filter(blockId -> !cubeGridNew.elements.containsKey(blockId))
                        .collect(Collectors.toList());

                boolean rebuild = false;
                for (var blockId : tobeRemoved) {
                    var block = cubegridOld.elements.get(blockId);
                    if (Vec3.dist(block.position, newWom.position) < OBSERVATION_RADIUS) {
                        Timer.removeFromGridStart = Instant.now();
                        navgrid.setOpen(block);
                        Timer.endRemoveFromGrid();
                        rebuild = true;
                    }
                }
                if (rebuild) {
                    // Removing a block makes all voxels it overlaps with empty, so go over all blocks to check
                    // if some voxels overlapped with multiple blocks.
                    Timer.addToGridStart = Instant.now();
                    for (var block2 : SEBlockFunctions.getAllBlocks(gridsAndBlocksStates)) {
                        addToGrid(block2);
                    }
                    Timer.endAddToGrid();
                }
                else {
                    // We add new blocks (from grids that changed):
                    List<String> tobeAdded = cubeGridNew.elements.keySet().stream()
                            .filter(id -> !cubegridOld.elements.containsKey(id))
                            .toList();
                    Timer.addToGridStart = Instant.now();
                    for (var blockId : tobeAdded) {
                        addToGrid(cubeGridNew.elements.get(blockId));
                    }
                    Timer.endAddToGrid();
                }
            }
        }
        Timer.endUpdateState();
    }

    @Override
    public void updateDoors() {
        Timer.updateDoorsStart = Instant.now();
        //Boundary observation_radius = new Boundary(Vec3.sub(wom.position, new Vec3(OBSERVATION_RADIUS + 2.5f)), 2 * OBSERVATION_RADIUS + 5);
        doors.forEach(door -> {
            if (Boolean.FALSE.equals(SEBlockFunctions.getSlideDoorState(door)))
                navgrid.setOpen(door);
            else
                navgrid.addObstacle(door);
        });

        // if any door is inside the viewing range, re-add all blocks
        //if (doors.stream().anyMatch(door -> observation_radius.contains(door.position))) {
        Observation rawGridsAndBlocksStates = env().getController().getObserver().observeBlocks();
        WorldModel gridsAndBlocksStates = SeEnvironmentKt.toWorldModel(rawGridsAndBlocksStates);
        for(var block : SEBlockFunctions.getAllBlocks(gridsAndBlocksStates)) {
            addToGrid(block);
        }
        //}
        Timer.endUpdateDoors();
    }

    public void addToGrid(WorldEntity block) {
        // check if it is a door, and get its open/close state:
        Boolean isOpen = SEBlockFunctions.getSlideDoorState(block) ;
        if (isOpen != null) {
            if (!isOpen)
                navgrid.addObstacle(block);
            if (doors.stream().noneMatch(d -> Objects.equals(d.id, block.id))) {
                System.out.println("Found a door: " + block.id);
                doors.add(block);
            }
        }
        // check if it is a button panel, and make it not blocking
        else if (block.getStringProperty("blockType").contains("ButtonPanel")) {
            if (buttons.stream().noneMatch(b -> Objects.equals(b.id, block.id))) {
                System.out.println("Found a button-panel: " + block.id);
                buttons.add(block);
            }
        }
        else
            navgrid.addObstacle(block);
    }

    public void exportGrid() {
        try {
            System.out.println(System.getProperty("user.dir"));
            FileWriter fileWriter = new FileWriter("NavGrid_HashMap.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            navgrid.knownObstacles.forEach(k -> {
                printWriter.printf("%d %d %d %n", k.x, k.y, k.z);
            });
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
