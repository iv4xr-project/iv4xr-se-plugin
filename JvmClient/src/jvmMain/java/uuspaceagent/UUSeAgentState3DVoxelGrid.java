package uuspaceagent;

import environments.SeEnvironmentKt;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import spaceEngineers.model.CharacterObservation;
import spaceEngineers.model.Observation;
import uuspaceagent.exploration.Explorable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UUSeAgentState3DVoxelGrid extends UUSeAgentState<DPos3> {

    public VoxelGrid grid = new VoxelGrid(0.62f) ;

    public UUSeAgentState3DVoxelGrid(String agentId) {
        super(agentId);
    }

    @Override
    public DPos3 getGridPos(Vec3 targetLocation) {
        return grid.gridProjectedLocation(targetLocation);
    }

    @Override
    public Vec3 getBlockCenter(Vec3 targetLocation) {
        return grid.getCubeCenterLocation(grid.gridProjectedLocation(targetLocation));
    }

    @Override
    public Vec3 getBlockCenter(DPos3 targetLocation) {
        return grid.getCubeCenterLocation(targetLocation);
    }

    @Override
    public Vec3 getOrigin() {
        return grid.boundary.lowerBounds;
    }

    @Override
    public Explorable<DPos3> getGrid() {
        return grid;
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
        assignTimeStamp(newWom, updateCount) ;

        // The obtained wom also does not include blocks observed. So we get them explicitly here:
        // Well, we will get ALL blocks. Note that S=some blocks may change state or disappear,
        // compared to what the agent currently has it its state.wom.
        Observation rawGridsAndBlocksStates = env().getController().getObserver().observeBlocks() ;
        WorldModel gridsAndBlocksStates = SeEnvironmentKt.toWorldModel(rawGridsAndBlocksStates) ;
        // HACK: make the grids and blocks marked as dynamic elements. SE sends them as non-dynamic
        // that will cause them to be ignored by mergeObservation.
        SEBlockFunctions.hackForceDynamicFlag(gridsAndBlocksStates) ;
        // assign a fresh timestamp too:
        assignTimeStamp(gridsAndBlocksStates, updateCount) ;
        for(var e : gridsAndBlocksStates.elements.entrySet()) {
            newWom.elements.put(e.getKey(), e.getValue()) ;
        }

        // updating the count:
        updateCount++ ;

        if(wom == null) {
            // this is the first observation
            wom = newWom ;
            Timer.initializeGridStart = Instant.now();
            grid.initializeGrid(wom.position, OBSERVATION_RADIUS);
            Timer.endInitializeGrid();

            Timer.addToGridStart = Instant.now();
            for(var block : SEBlockFunctions.getAllBlocks(gridsAndBlocksStates)) {
                addToGrid(block);
            }
            Timer.endAddToGrid();

            grid.updateUnknown(wom.position, OBSERVATION_RADIUS);
        }
        else {
            // MERGING the two woms:
            var changes = wom.mergeNewObservation(newWom) ;
            // merges the woms, but cannot easily be used for exploration because everything outside the viewing distance
            // is thrown away out of the wom

            System.out.println("========================================================");

            // Check if the VoxelGrid needs to be expanded
            float size = OBSERVATION_RADIUS + VoxelGrid.BLOCK_SIZE + VoxelGrid.AGENT_HEIGHT + grid.voxelSize;
            // Exact size should be OBSERVATION_RADIUS + VoxelGrid.BLOCK_SIZE + (VoxelGrid.AGENT_HEIGHT - grid.voxelSize) * 0.5f + grid.voxelSize;
            // But we make it a little bigger (prevents out-of-bounds errors because of rounding)
            Boundary observation_radius = new Boundary(Vec3.sub(wom.position, new Vec3(size)), 2 * size);
            grid.checkAndExpand(observation_radius, this);

            // Remove grids that are not in the WOM anymore
            List<String> tobeRemoved = wom.elements.keySet().stream()
                    .filter(id -> ! newWom.elements.containsKey(id))
                    .collect(Collectors.toList());
            for(var id : tobeRemoved) wom.elements.remove(id) ;

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
                        grid.removeObstacle(block);
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
        Boundary observation_radius = new Boundary(Vec3.sub(wom.position, new Vec3(OBSERVATION_RADIUS + 2.5f)), 2 * OBSERVATION_RADIUS + 5);
        doors.forEach(door -> {
            if (observation_radius.contains(door.position)) {
                if (Boolean.FALSE.equals(SEBlockFunctions.getSlideDoorState(door)))
                    grid.setOpen(door);
                else
                    grid.addObstacle(door);
            } else {
                grid.setUnknown(door);
            }
        });

        // if any door is inside the viewing range, re-add all blocks
        if (doors.stream().anyMatch(door -> observation_radius.contains(door.position))) {
            Observation rawGridsAndBlocksStates = env().getController().getObserver().observeBlocks();
            WorldModel gridsAndBlocksStates = SeEnvironmentKt.toWorldModel(rawGridsAndBlocksStates);
            for(var block : SEBlockFunctions.getAllBlocks(gridsAndBlocksStates)) {
                addToGrid(block);
            }
        }
        Timer.endUpdateDoors();
    }

    public void addToGrid(WorldEntity block) {
        Boolean isOpen = SEBlockFunctions.getSlideDoorState(block) ;
        if (isOpen != null) {
            if (!isOpen)
                grid.addObstacle(block);
            if (doors.stream().noneMatch(d -> Objects.equals(d.id, block.id))) {
                System.out.println("Found a door: " + block.id);
                doors.add(block);
            }
        }
        else if(block.getStringProperty("blockType").contains("ButtonPanel")) {
            if (buttons.stream().noneMatch(b -> Objects.equals(b.id, block.id))) {
                System.out.println("Found a button-panel: " + block.id);
                buttons.add(block);
            }
        }
        else
            grid.addObstacle(block);
    }

    public void exportGrid() {
        try {
            System.out.println(System.getProperty("user.dir"));
            FileWriter fileWriter = new FileWriter("3D_Internal_WOM.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            Vec3 player_pos = grid.getCubeCenterLocation(grid.gridProjectedLocation(new Vec3(wom.position.x, wom.position.y + VoxelGrid.AGENT_HEIGHT * 0.5f, wom.position.z)));
            Vec3 door_pos = grid.getCubeCenterLocation(grid.gridProjectedLocation(new Vec3(9999,9999,9999)));
            printWriter.printf("player: %f %f %f %n", player_pos.x, player_pos.y, player_pos.z);
            printWriter.printf("door: %f %f %f %n", door_pos.x, door_pos.y, door_pos.z);
            for (int x = 0; x < grid.grid.size(); x++) {
                for (int y = 0; y < grid.grid.get(x).size(); y++) {
                    for (int z = 0; z < grid.grid.get(x).get(y).size(); z++) {
                        //if (grid.get(x, y, z).label == Label.OPEN) {
                            Vec3 block_pos = grid.getCubeCenterLocation(new DPos3(x, y, z));
                            printWriter.printf("%f %f %f %d %n", block_pos.x, block_pos.y, block_pos.z, grid.get(x, y, z).label);
                        //}
                    }
                }
            }
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
