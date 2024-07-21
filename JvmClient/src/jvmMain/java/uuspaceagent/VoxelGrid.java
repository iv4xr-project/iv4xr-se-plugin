package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Vec3;
import uuspaceagent.exploration.Explorable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class VoxelGrid implements Explorable<DPos3> {

    /**
     * The assumed height of the player characters. It is 1.8, we conservatively
     * assume it is 2f.
     */
    public static float AGENT_HEIGHT = 1.8f ;
    public static float AGENT_WIDTH  = 1f ;
    public static float BLOCK_SIZE   = 2.5f ;

    public Boundary2 boundary;
    public float voxelSize;
    float squareDiagonalLength;
    float cubeDiagonalLength;
    ArrayList<ArrayList<ArrayList<Voxel>>> grid;

    public Voxel get(int x, int y, int z) {
        return grid.get(x).get(y).get(z);
    }
    public Voxel get(DPos3 pos) {
        return grid.get(pos.x).get(pos.y).get(pos.z);
    }
    public DPos3 size() { return new DPos3(grid.size(), grid.get(0).size(), grid.get(0).get(0).size()); }

    public VoxelGrid(float voxelSize) {
        this.voxelSize = voxelSize;
        squareDiagonalLength = new Vec3(0,voxelSize,voxelSize).length();
        cubeDiagonalLength = new Vec3(voxelSize,voxelSize,voxelSize).length();
    }

    /**
     *  Initializes the starting grid based on the first observed position and the observation radius.
     *  Call in UUSeAgentstate3D on first observation.
     */
    public void initializeGrid(Vec3 pos, float observation_radius) {
        float size = observation_radius + 2.5f + (AGENT_HEIGHT - voxelSize) * 0.5f + voxelSize;
        Vec3 lowerB = Vec3.sub(pos, new Vec3(size));
        Vec3 upperB = Vec3.add(pos, new Vec3(size));
        boundary = new Boundary2(lowerB, upperB);

        int initialSize = (int) ((boundary.upperBounds.x - boundary.lowerBounds.x) / voxelSize);
        grid = Stream.generate(() -> Stream.generate(() -> Stream.generate(Voxel::new)
                                .limit(initialSize)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .limit(initialSize)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .limit(initialSize)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calculate the unit-cube that contains a point p.
     */
    public DPos3 gridProjectedLocation(Vec3 p) {
        p = Vec3.sub(p, boundary.lowerBounds) ;
        /*
        int x = (int) (Math.floor(p.x / CUBE_SIZE)) ;
        int y = (int) (Math.floor(p.y / CUBE_SIZE)) ;
        int z = (int) (Math.floor(p.z / CUBE_SIZE)) ;
        */
        int x = myFloor(p.x / voxelSize) ;
        int y = myFloor(p.y / voxelSize) ;
        int z = myFloor(p.z / voxelSize) ;

        return new DPos3(x,y,z) ;
    }

    /**
     * Return the actual location of the center of a unit cube; the location is expressed
     * as a 3D position in the space.
     */
    public Vec3 getCubeCenterLocation(DPos3 cube) {
        float x = (((float) cube.x) + 0.5f) * voxelSize + boundary.lowerBounds.x ;
        float y = (((float) cube.y) + 0.5f) * voxelSize + boundary.lowerBounds.y ;
        float z = (((float) cube.z) + 0.5f) * voxelSize + boundary.lowerBounds.z ;
        return new Vec3(x,y,z) ;
    }

    public static int myFloor(float x) {
        return (int) Math.floor(x) ;
    }

    List<DPos3> getObstructedCubes(WorldEntity block) {

        List<DPos3> obstructed = new LinkedList<>() ;

        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(block) ; // should add rotation if it is not a cube. TODO.
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(block) ; // should add rotation if it is not a cube. TODO.


        // add some padding due to agent's body width/height:
        //      note: agent height = 1.8, about 0.5 above feet is the rotation point, so to prevent the agent from
        //            hitting their head, pad with (1.3 - 0.5 * voxelSize)
        Vec3 vpadding = new Vec3((AGENT_HEIGHT - voxelSize) * 0.5f) ;
        //minCorner = Vec3.sub(minCorner, hpadding) ;
        minCorner = Vec3.sub(minCorner, vpadding) ;
        maxCorner = Vec3.add(maxCorner, vpadding) ;

        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;
        // all squares between these two corners are blocked:
        for(int x = corner1.x; x<=corner2.x; x++) {
            for (int y = corner1.y; y <= corner2.y; y++) {
                for (int z = corner1.z; z<=corner2.z; z++) {
                    var cube = new DPos3(x,y,z) ;
                    obstructed.add(cube) ;
                }
            }
        }
        return obstructed ;
    }

    public void addObstacle(WorldEntity block) {

        var obstructedCubes = getObstructedCubes(block);
        for(var voxel : obstructedCubes) {
            get(voxel).label = Label.BLOCKED;
        }
    }

    public void removeObstacle(WorldEntity block) {

        var obstructedCubes = getObstructedCubes(block) ;
        for(var voxel : obstructedCubes) {
            get(voxel).label = Label.OPEN;
        }
    }

    public void setUnknown(WorldEntity block) {

        var obstructedCubes = getObstructedCubes(block) ;
        for(var voxel : obstructedCubes) {
            get(voxel).label = Label.UNKNOWN;
        }
    }

    public void setOpen(WorldEntity block) {

        var obstructedCubes = getObstructedCubes(block) ;
        for(var voxel : obstructedCubes) {
            get(voxel).label = Label.OPEN;
        }
    }

    /**
     * Check if the VoxelGrid fully contains the viewing range. If not, expand it outwards.
     */
    public void checkAndExpand(Boundary range, UUSeAgentState3DVoxelGrid state) {
        // If the VoxelGrid boundary fully contain the viewing range, do nothing
        if (this.boundary.contains(range))
            return;

        Timer.expandStart = Instant.now();
        // Otherwise, expand
        if (range.pos().x < this.boundary.lowerBounds.x) { // expand to the left
            DPos3 gridSize = size();
            int diff = (int) Math.ceil(this.boundary.lowerBounds.x - range.pos().x);
            for (int x = 0; x < diff; x++) {
                ArrayList<ArrayList<Voxel>> newX = Stream.generate(() -> Stream.generate(Voxel::new)
                                .limit(gridSize.z)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .limit(gridSize.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                grid.add(0, newX);
            }
            boundary.lowerBounds.x -= voxelSize * diff;
            state.currentPathToFollow.forEach(dPos3 -> dPos3.x += diff);
        }
        else if (range.upperBounds().x > this.boundary.upperBounds.x) { // expand to the right
            DPos3 gridSize = size();
            int diff = (int) Math.ceil(range.upperBounds().x - this.boundary.upperBounds.x);
            for (int x = 0; x < diff; x++) {
                ArrayList<ArrayList<Voxel>> newX = Stream.generate(() -> Stream.generate(Voxel::new)
                                .limit(gridSize.z)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .limit(gridSize.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                grid.add(newX);
            }
            boundary.upperBounds.x += voxelSize * diff;
        }
        if (range.pos().y < this.boundary.lowerBounds.y) { // expand downwards
            DPos3 gridSize = size();
            int diff = (int) Math.ceil(this.boundary.lowerBounds.y - range.pos().y);
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < diff; y++) {
                    ArrayList<Voxel> newY = Stream.generate(Voxel::new)
                            .limit(gridSize.z).collect(Collectors.toCollection(ArrayList::new));
                    grid.get(x).add(0, newY);
                }
            }
            boundary.lowerBounds.y -= voxelSize * diff;
            state.currentPathToFollow.forEach(dPos3 -> dPos3.y += diff);
        }
        else if (range.upperBounds().y > this.boundary.upperBounds.y) { // expand upwards
            DPos3 gridSize = size();
            int diff = (int) Math.ceil(range.upperBounds().y - this.boundary.upperBounds.y);
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < diff; y++) {
                    ArrayList<Voxel> newY = Stream.generate(Voxel::new)
                            .limit(gridSize.z).collect(Collectors.toCollection(ArrayList::new));
                    grid.get(x).add(newY);
                }
            }
            boundary.upperBounds.y += voxelSize * diff;
        }
        if (range.pos().z < this.boundary.lowerBounds.z) { // expand to the front
            DPos3 gridSize = size();
            int diff = (int) Math.ceil(this.boundary.lowerBounds.z - range.pos().z);
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < gridSize.y; y++) {
                    for (int z = 0; z < diff; z++) {
                        grid.get(x).get(y).add(0, new Voxel());
                    }
                }
            }
            boundary.lowerBounds.z -= voxelSize * diff;
            state.currentPathToFollow.forEach(dPos3 -> dPos3.z += diff);
        }
        else if (range.upperBounds().z > this.boundary.upperBounds.z) { // expand to the back
            DPos3 gridSize = size();
            int diff = (int) Math.ceil(range.upperBounds().z - this.boundary.upperBounds.z);
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < gridSize.y; y++) {
                    for (int z = 0; z < diff; z++) {
                        grid.get(x).get(y).add(new Voxel());
                    }
                }
            }
            boundary.upperBounds.z += voxelSize * diff;
        }
        Timer.endExpand();
    }

    /**
     * Checks if the voxel is outside the grid and if so, expands the grid to contain the voxel
     * @param voxel: The voxel position that is or is not yet in the grid
     * @param pos: The agent position (unused with current implementation)
     * @param observation_radius: speaks for itself
     * @return X,Y,Z amount it expanded towards minus (used to add to subsequent calls that do not re-evaluate the gridSize)
     */
    public DPos3 checkAndExpand2(DPos3 voxel, Vec3 pos, float observation_radius) {
        var retVal = new DPos3(0, 0, 0);
        DPos3 gridSize = size();

        if (voxel.x < 0) {
            int diff = -1 * voxel.x;
            for (int x = 0; x < diff; x++) {
                ArrayList<ArrayList<Voxel>> newX = Stream.generate(() -> Stream.generate(Voxel::new)
                                .limit(gridSize.z)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .limit(gridSize.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                grid.add(0, newX);
                boundary.lowerBounds.x -= voxelSize;
                retVal.x += diff;
            }
        }
        else if (voxel.x >= gridSize.x) {
            int diff = voxel.x - (gridSize.x-1);
            for (int x = 0; x < diff; x++) {
                ArrayList<ArrayList<Voxel>> newX = Stream.generate(() -> Stream.generate(Voxel::new)
                                .limit(gridSize.z)
                                .collect(Collectors.toCollection(ArrayList::new)))
                        .limit(gridSize.y)
                        .collect(Collectors.toCollection(ArrayList::new));
                grid.add(newX);
            }
        }
        if (voxel.y < 0) {
            int diff = -1 * voxel.y;
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < diff; y++) {
                    ArrayList<Voxel> newY = Stream.generate(Voxel::new)
                            .limit(gridSize.z).collect(Collectors.toCollection(ArrayList::new));
                    grid.get(x).add(0, newY);
                }
            }
            boundary.lowerBounds.y -= voxelSize;
            retVal.y++;
        }
        else if (voxel.y >= gridSize.y) {
            int diff = voxel.y - (gridSize.y-1);
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < diff; y++) {
                    ArrayList<Voxel> newY = Stream.generate(Voxel::new)
                            .limit(gridSize.z).collect(Collectors.toCollection(ArrayList::new));
                    grid.get(x).add(newY);
                }
            }
        }
        if (voxel.z < 0) {
            int diff = -1 * voxel.z;
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < gridSize.y; y++) {
                    for (int z = 0; z < diff; z++) {
                        grid.get(x).get(y).add(0, new Voxel());
                    }
                }
            }
            boundary.lowerBounds.z -= voxelSize;
            retVal.z++;
        }
        else if (voxel.z >= gridSize.z) {
            int diff = voxel.z - (gridSize.z-1);
            for (int x = 0; x < gridSize.x; x++) {
                for (int y = 0; y < gridSize.y; y++) {
                    for (int z = 0; z < diff; z++) {
                        grid.get(x).get(y).add(new Voxel());
                    }
                }
            }
        }
        return retVal;
    }

    @Override
    public Iterable<DPos3> neighbours(DPos3 p) {
        Timer.getNeighbourStart = Instant.now();
        List<DPos3> candidates = new LinkedList<>();

        if (get(p).label == Label.BLOCKED) {
            for (int x = max(p.x-1, 0); x <= min(p.x+1, size().x-1); x++) {
                for (int y = max(p.y-1, 0); y <= min(p.y+1, size().y-1); y++) {
                    for (int z = max(p.z-1, 0); z <= min(p.z+1, size().z-1); z++) {
                        if(x==p.x && y==p.y && z==p.z) continue;
                        var neighbourCube = new DPos3(x,y,z) ; // a neighbouring cube
                        if(get(x,y,z).label == Label.OPEN)
                            candidates.add(neighbourCube) ;
                    }
                }
            }
            Timer.endGetNeighbour();
            return candidates ;
        }

        boolean top, right, left, bottom, front, back,
                topleft, topright, bottomleft, bottomright,
                leftfront, rightfront, leftback, rightback,
                topfront, bottomfront, topback, bottomback;
        top = right = left = bottom = front = back =
                topleft = topright = bottomleft = bottomright =
                leftfront = rightfront = leftback = rightback =
                topfront = bottomfront = topback = bottomback = false;
        int x, y, z;
        int maxX = min(p.x+1, size().x-1);
        int maxY = min(p.y+1, size().y-1);
        int maxZ = min(p.z+1, size().z-1);
        int minX = max(p.x-1, 0);
        int minY = max(p.y-1, 0);
        int minZ = max(p.z-1, 0);

        // Directional
        x = p.x; y = maxY; z = p.z;
        if (get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            top = true;
        }
        y = minY;
        if (get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            bottom = true;
        }
        y = p.y; x = maxX;
        if (get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            right = true;
        }
        x = minX;
        if (get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            left = true;
        }
        x = p.x; z = maxZ;
        if (get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            back = true;
        }
        z = minZ;
        if (get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            front = true;
        }

        // Diagonal
        z = p.z; x = minX; y = maxY;
        if (top && left && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            topleft = true;
        }
        x = maxX;
        if (top && right && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            topright = true;
        }
        y = minY;
        if (bottom && right && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            bottomright = true;
        }
        x = minX;
        if (bottom && left && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            bottomleft = true;
        }
        x = p.x; z = minZ;
        if (bottom && front && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            bottomfront = true;
        }
        z = maxZ;
        if (bottom && back && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            bottomback = true;
        }
        y = maxY;
        if (top && back && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            topback = true;
        }
        z = minZ;
        if (top && front && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            topfront = true;
        }
        y = p.y; x = minX;
        if (left && front && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            leftfront = true;
        }
        x = maxX;
        if (right && front && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            rightfront = true;
        }
        z = maxZ;
        if (right && back && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            rightback = true;
        }
        x = minX;
        if (left && back && get(x,y,z).label == Label.OPEN) {
            candidates.add(new DPos3(x,y,z));
            leftback = true;
        }

        y = maxY;
        if (topleft && topback && leftback && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        z = minZ;
        if (topleft && topfront && leftfront && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        x = maxX;
        if (topright && topfront && rightfront && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        z = maxZ;
        if (topright && topback && rightback && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        y = minY;
        if (bottomright && bottomback && rightback && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        z = minZ;
        if (bottomright && bottomfront && rightfront && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        x = minX;
        if (bottomleft && bottomfront && leftfront && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }
        z = maxZ;
        if (bottomleft && bottomback && leftback && get(x,y,z).label == Label.OPEN) { candidates.add(new DPos3(x,y,z)); }

        Timer.endGetNeighbour();
        return candidates ;
    }

    @Override
    public Iterable<DPos3> neighbours_explore(DPos3 p) {
        Timer.getNeighbourStart = Instant.now();
        List<DPos3> candidates = new LinkedList<>();

        if (get(p).label == Label.BLOCKED) {
            for (int x = max(p.x-1, 0); x <= min(p.x+1, size().x-1); x++) {
                for (int y = max(p.y-1, 0); y <= min(p.y+1, size().y-1); y++) {
                    for (int z = max(p.z-1, 0); z <= min(p.z+1, size().z-1); z++) {
                        if(x==p.x && y==p.y && z==p.z) continue;
                        var neighbourCube = new DPos3(x,y,z) ; // a neighbouring cube
                        if(get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN)
                            candidates.add(neighbourCube) ;
                    }
                }
            }
            Timer.endGetNeighbour();
            return candidates ;
        }

        boolean top, right, left, bottom, front, back,
                topleft, topright, bottomleft, bottomright,
                leftfront, rightfront, leftback, rightback,
                topfront, bottomfront, topback, bottomback;
        top = right = left = bottom = front = back =
                topleft = topright = bottomleft = bottomright =
                leftfront = rightfront = leftback = rightback =
                topfront = bottomfront = topback = bottomback = false;
        int x, y, z;
        int maxX = min(p.x+1, size().x-1);
        int maxY = min(p.y+1, size().y-1);
        int maxZ = min(p.z+1, size().z-1);
        int minX = max(p.x-1, 0);
        int minY = max(p.y-1, 0);
        int minZ = max(p.z-1, 0);

        // Directional
        x = p.x; y = maxY; z = p.z;
        if (get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            top = true;
        }
        y = minY;
        if (get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            bottom = true;
        }
        y = p.y; x = maxX;
        if (get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            right = true;
        }
        x = minX;
        if (get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            left = true;
        }
        x = p.x; z = maxZ;
        if (get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            back = true;
        }
        z = minZ;
        if (get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            front = true;
        }

        // Diagonal
        z = p.z; x = minX; y = maxY;
        if (top && left && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            topleft = true;
        }
        x = maxX;
        if (top && right && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            topright = true;
        }
        y = minY;
        if (bottom && right && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            bottomright = true;
        }
        x = minX;
        if (bottom && left && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            bottomleft = true;
        }
        x = p.x; z = minZ;
        if (bottom && front && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            bottomfront = true;
        }
        z = maxZ;
        if (bottom && back && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            bottomback = true;
        }
        y = maxY;
        if (top && back && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            topback = true;
        }
        z = minZ;
        if (top && front && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            topfront = true;
        }
        y = p.y; x = minX;
        if (left && front && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            leftfront = true;
        }
        x = maxX;
        if (right && front && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            rightfront = true;
        }
        z = maxZ;
        if (right && back && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            rightback = true;
        }
        x = minX;
        if (left && back && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z));
            leftback = true;
        }

        y = maxY;
        if (topleft && topback && leftback && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        z = minZ;
        if (topleft && topfront && leftfront && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        x = maxX;
        if (topright && topfront && rightfront && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        z = maxZ;
        if (topright && topback && rightback && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        y = minY;
        if (bottomright && bottomback && rightback && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        z = minZ;
        if (bottomright && bottomfront && rightfront && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        x = minX;
        if (bottomleft && bottomfront && leftfront && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }
        z = maxZ;
        if (bottomleft && bottomback && leftback && get(x,y,z).label == Label.OPEN || get(x,y,z).label == Label.UNKNOWN) {
            candidates.add(new DPos3(x,y,z)); }

        Timer.endGetNeighbour();
        return candidates ;
    }

    @Override
    public float heuristic(DPos3 from, DPos3 to) {
        // using Manhattan distance...
        // return voxelSize * (float) (Math.abs(to.x - from.x) + Math.abs(to.y - from.y) + Math.abs(to.z - from.z)) ;

        // using Euclidean distance...
        return voxelSize * (float) Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y) + (to.z - from.z) * (to.z - from.z)) ;
    }

    @Override
    public float heuristic(DPos3 fromNode, Vec3 to) {
        Vec3 from = getCubeCenterLocation(fromNode);
        return voxelSize * (float) Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y) + (to.z - from.z) * (to.z - from.z)) ;
    }

    @Override
    public float distance(DPos3 from, DPos3 to) {
        if (from.x != to.x && from.y != to.y && from.z != to.z) {
            return cubeDiagonalLength ;
        }
        if(from.x == to.x) {
            if (from.y == to.y || from.z == to.z) {
                return voxelSize;
            }
            return squareDiagonalLength ;
        }
        if(from.y == to.y) {
            if (from.x == to.x || from.z == to.z) {
                return voxelSize;
            }
            return squareDiagonalLength ;
        }
        return squareDiagonalLength ;
    }

    @Override
    public DPos3 phantom_neighbour(DPos3 node, DPos3 real_neighbour) {
        return real_neighbour;
    }

    @Override
    public boolean isUnknown(DPos3 node) {
        if (node.x < 0 || node.y < 0 || node.z < 0 || node.x >= size().x || node.y >= size().y || node.z >= size().z)
            return true; // if outside the grid, it must be unknown
        return get(node).label == Label.UNKNOWN;
    }

    @Override
    public void updateUnknown(Vec3 pos, float observation_radius) {
        Vec3 minCorner = Vec3.sub(pos, new Vec3(observation_radius));
        Vec3 maxCorner = Vec3.add(pos, new Vec3(observation_radius));
        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;

        for(int x = max(corner1.x, 0); x <= min(corner2.x, size().x-1); x++) {
            for (int y = max(corner1.y, 0); y <= min(corner2.y, size().y-1); y++) {
                for (int z = max(corner1.z, 0); z <= min(corner2.z, size().z-1); z++) {
                    float distance = Vec3.dist(pos, new Vec3(x * voxelSize + boundary.lowerBounds.x,
                            y * voxelSize + boundary.lowerBounds.y,
                            z * voxelSize + boundary.lowerBounds.z));
                    if (get(x, y, z).label == Label.UNKNOWN && distance <= observation_radius) {
                        get(x, y, z).label = Label.OPEN;
                    }
                }
            }
        }
    }
}
