package uuspaceagent;

import eu.iv4xr.framework.extensions.pathfinding.Navigatable;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Obstacle;
import eu.iv4xr.framework.spatial.Vec3;
import uuspaceagent.exploration.Explorable;

import java.util.*;

/**
 * Implements a 3D-grid-based navigation graph. The space is thought to be discretely
 * divided into small unit cubes (of size 1x1x1 unit). The orientation of these cubes
 * are aligned to the x/y/z axes of the space.
 *
 * For this particular implementation, most of the space is assumed to be empty (not
 * containing obstacles) and is only sparsely populated by objects that obstruct
 * navigation. So, rather than explicitly representing each unit cubes in the memory
 * (there can be prohibitively lots of them) we will use a sparse representation, where
 * only the unit-cubes that intersect with objects (e.g. walls or buildings) will
 * be kept in the memory.
 *
 * The type of navigation supported by this class is "above ground" navigation. It
 * assumes there is a flat ground surface. The surface on the ground is assumed to be
 * navigable, and flat. On the other hand, the ground beneath this surface is assumed
 * o be unnavigable. The ground surface is also assumed to extend infinitely (it has no
 * "edge of the world" that would allow an agent to wrap around to go beneath the
 * ground surface :D ).
 *
 * So, the above means that only the ground surface and the space above it are
 * navigable.
 *
 * For flexibility, the origin of the grid does not necessarily co-incide with the
 * origin of the space's own 3D coordinate system. However: (1) the x/z surface with
 * y = origin.y is assumed to be navigable, and (2) the grid's x/y/z axes
 * are parallel and point to the same direction as the space's own x/y/z axes.
 *
 * The actual size of a unit cube is specified by the static variable CUBE_SIZE.
 * Each unit cube is identified by a triple (x,y,z) where x/y/z are integers. If
 * the grid origin is located in the actual location o in the space, the actual
 * location of the cube (x,y,z)'s center is:
 *
 *     (CUBE_SIZE*(x + 0.5) + o.x, CUBE_SIZE*(y + 0.5) + o.y, CUBE_SIZE*(z + 0.5) + o.z)
 *
 * For example, the center of cube (0,0,0) is:
 *
 *     (o.x + 0.5*CUBE_SIZE, o.y + 0.5*CUBE_SIZE, o.z + 0.5*CUBE_SIZE)
 *
 * actual location of tenter of this cube is then
 *
 * TODO: for now are limited to this kind of blocks:
 *   (1) obstructing objects are assumed to be in the shape of cubes as well (though they are usually bigger).
 *   (2) these object-cubes (called blocks) are xyz-aligned. That is, they have their ribs parallel with
 *       the world/space x/y/z axes.
 *   (3) the blocks are not moving nor rotating.
 *
 * TODO: for now we don't check if a square on the grid is actually a SOLID floor (e.g. it
 *  could be a gaping hole in space). We can do this by maintaining a list of squares
 *  with solid floor.
 */
public class NavGrid implements Explorable<DPos3> {

    /**
     * The assumed height of the player characters. It is 1.8, we conservatively
     * assume it is 2f.
     */
    public static float AGENT_HEIGHT = 2f ;
    public static float AGENT_WIDTH  = 1f ;
    public static float CUBE_SIZE = 0.5f ;

    public float squareDiagonalLength = (new Vec3(0,CUBE_SIZE,CUBE_SIZE)).length() ;
    public float cubeDiagonalLength = (new Vec3(CUBE_SIZE,CUBE_SIZE,CUBE_SIZE)).length() ;

    /**
     * Define the world-coordinate of the grid origin. Note that this class
     * will then assume that the x/z surface with y = origin.y is assumed to be
     * navigable.
     */
    public Vec3 origin ;

    /**
     * When set to true, then neighboring cubes of different altitude (y-axis) will
     * be included neighbors, for navigation purpose. Else only neighbors with of y = 0
     * (grid coordinate) are considered as navigation-neighbors.
     *
     * The default is false.
     */
    public boolean enableFlying = false ;


    /**
     * Map unit-cubes to the obstacles that block them. Note that there might be multiple
     * obstacles that block the same cube.
     */
    public Map<DPos3,List<Obstacle<String>>> knownObstacles = new HashMap<>() ;

    /**
     * Keep track of all block-id that intersect the this grid (and hence blocking some cubes
     * in the grid).
     */
    public Set<String> allObstacleIDs = new HashSet<>() ;

    public NavGrid() { }

    /**
     * Reset the grid to a new origin. This will clear the list of known obstacles (because
     * those are obstacles for the grid anchored to the old origin. We should re-inspect
     * all known block and add them again to the grid.
     */
    public void resetGrid(Vec3 origin) {
        this.origin = origin ;
        knownObstacles.clear();
        allObstacleIDs.clear();
    }


    /**
     * Calculate the unit-cube that contains a point p.
     */
    public DPos3 gridProjectedLocation(Vec3 p) {
        p = Vec3.sub(p,origin) ;
        /*
        int x = (int) (Math.floor(p.x / CUBE_SIZE)) ;
        int y = (int) (Math.floor(p.y / CUBE_SIZE)) ;
        int z = (int) (Math.floor(p.z / CUBE_SIZE)) ;
        */
        int x = myFloor(p.x / CUBE_SIZE) ;
        int y = myFloor(p.y / CUBE_SIZE) ;
        int z = myFloor(p.z / CUBE_SIZE) ;

        return new DPos3(x,y,z) ;
    }

    public static int myFloor(float x) {
        return (int) Math.floor(x) ;
    }



    /**
     * Return the actual location of the center of a unit cube; the location is expressed
     * as a 3D position in the space.
     */
    public Vec3 getSquareCenterLocation(DPos3 cube) {
        float x = (((float) cube.x) + 0.5f) * CUBE_SIZE + origin.x ;
        float y = (((float) cube.y) + 0.5f) * CUBE_SIZE + origin.y ;
        float z = (((float) cube.z) + 0.5f) * CUBE_SIZE + origin.z ;
        return new Vec3(x,y,z) ;
    }

    /**
     * Square-distance from a location p to the center of a square; p is assumed to be on the grid
     * (so we ignore its y-value).
     */
    public float squareDistanceToSquare(Vec3 p, DPos3 cube) {
        Vec3 q = getSquareCenterLocation(cube) ;
        return Vec3.sub(q,p).lengthSq() ;
    }

    /**
     * Calculate the unit-cubes on this grid that could be obstructed by the given block. The method
     * does NOT take the block dynamic state into account (e.g. an open door).
     * Some padding is added to account for the agent's physical width. Currently, this assumes that:
     *      *    (1) the block's orientation is upright and aligned with the grid (it is not
     *      *    vertically tilted, nor horizontally angled).
     *      *    (2) the block does not move nor rotate, so we will not update its position in the future;
     *      *        though it can be removed if it does not exist anymore.
     *      *    (3) the block's shape is a cube.
     */
    List<DPos3> getObstructedCubes(WorldEntity block) {

        List<DPos3> obstructed = new LinkedList<>() ;

        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(block) ; // should add rotation if it is not a cube. TODO.
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(block) ; // should add rotation if it is not a cube. TODO.

        // Check if the block is below the ground surface. It if is, it can obstruct movement on or above
        // the surface.

        // Adding 0.1 offset for some bit of seemingly inaccuracy in the sampling of the agent's
        // ground position, which is used as the base of the origin position of this grid.
        // Without this offset, blocks that are just below the grid surface, and touching it to form
        // the grid's solid floor will appear as obstructing.
        float correction_offset = 0.1f ;
        if(maxCorner.y <= origin.y + correction_offset) {
            // the block is UNDER the ground-surface. So, it won't obstruct either.
            return obstructed ;
        }

        // TODO: a more general approach.
        // add some padding due to agent's body width:
        Vec3 hpadding = Vec3.mul(new Vec3(AGENT_WIDTH,0,AGENT_WIDTH), 0.6f) ;
        Vec3 vpadding = new Vec3(0, AGENT_HEIGHT, 0) ;
        minCorner = Vec3.sub(minCorner,hpadding) ;
        minCorner = Vec3.sub(minCorner, vpadding) ;
        maxCorner = Vec3.add(maxCorner,hpadding) ;
        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;
        // all squares between these two corners are blocked:
        for(int x = corner1.x; x<=corner2.x; x++) {
            for (int y = Math.max(0, corner1.y); y <= corner2.y; y++) {
                // PS: cubes below y=0 are below the ground surface and hence won't obstruct
                // navigation on and above the surface.
                for (int z = corner1.z; z<=corner2.z; z++) {
                    var cube = new DPos3(x,y,z) ;
                    obstructed.add(cube) ;
                }
            }
        }
        return obstructed ;
    }

    /**
     * Add the block as an obstacle on the grid. This is only the case the block intersects
     * with the grid's 2D plane. This assumes that:
     *    (1) the block's orientation is upright and aligned with the grid (it is not
     *    vertically tilted, nor horizontally angled).
     *    (2) the block does not move nor rotate, so we will not update its position in the future;
     *        though it can be removed if it does not exist anymore.
     *    (3) the block is a cube.
     */
    public void addObstacle(WorldEntity block) {

        // check if the block is already added. If so we don't do anything.
        if(allObstacleIDs.contains(block.id)) {
            return ;
        }

        var obstructedCubes = getObstructedCubes(block) ;
        var obstacle = new Obstacle<>(block.id) ;
        obstacle.isBlocking = true ;
        boolean added = false ;
        for(var cube : obstructedCubes) {
            var olist = knownObstacles.get(cube) ;
            if (olist == null) {
                olist = new LinkedList<>() ;
                knownObstacles.put(cube,olist) ;
            }
            olist.add(obstacle) ;
            added = true ;
            //console(">>>>> Gird2DNav: adding " + block.getId()) ;
        }
        // so the block is added as an obstacle. Add it to here too:
        if(added) allObstacleIDs.add(block.id) ;
    }

    //public void addObstacle(Collection<Block> blocks) {
    //    for(var b : blocks) addObstacle(b);
    //}

    /**
     * Use this to update the cubes  blocked by the block, in case its has changes position or has
     * rotated.
     * TODO.
     */
    public void updateBlockPosition(WorldEntity block) {
        throw new UnsupportedOperationException() ;
    }

    /**
     * Remove the obstacles with the given blockId. This means that all cubes that are marked
     * as blocked by this block are now cleared. Use this to unblock cubes when the block
     * does not exist anymore.
     */
    public void removeObstacle(String blockId) {
        List<DPos3> cubesThatBecomeFree = new LinkedList<>() ;
        for(var e : knownObstacles.entrySet()) {
            var olist = e.getValue() ;
            Obstacle<String> tobeRemoved = null ;
            for(var o : olist) {
                if(o.obstacle.equals(blockId)) {
                    tobeRemoved = o ;
                    break ;
                }
            }
            if (tobeRemoved != null) olist.remove(tobeRemoved) ;
            if (olist.isEmpty()) cubesThatBecomeFree.add(e.getKey()) ;

        }
        for(var sq : cubesThatBecomeFree) {
            knownObstacles.remove(sq) ;
        }
        allObstacleIDs.remove(blockId) ;
    }

    /**
     * Return the cubes representing the walkable corridor of a door. This assumes the
     * door is of size 1x1x1 large block, and it is not tilted, nor angled towards x or z axis.
     * The door is assumed to be obstructing on this grid.
     */
    List<DPos3> getDoorCorridorCubes(WorldEntity door) {

        List<DPos3> corridor = new LinkedList<>() ;

        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(door) ; // should add rotation if it is not a cube. TODO.
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(door) ; // should add rotation if it is not a cube. TODO.
        // the door horizontal orientation
        Vec3 hdir = (Vec3) door.getProperty("orientationForward") ; hdir.y = 0 ;
        float angle_with_x_axis = Vec3.dot(new Vec3(1,0,0), hdir) ;
        // TODO: handle vertical orientation
        // assumes that the door is UNROTATED.
        // TODO: a more general approach.
        // add some padding due to agent's body width:
        Vec3 padding = Vec3.mul(new Vec3(AGENT_WIDTH,0,AGENT_WIDTH), 0.6f) ;
        float padding_to_door_innerwall = 0.5f ;
        if(angle_with_x_axis <= 0.01) {
            // (almost) parallel with the x-axis (so door's corridor is along x-axis(
            minCorner.z += padding_to_door_innerwall ;
            maxCorner.z -= padding_to_door_innerwall ;
            minCorner.x -= padding.x ;
            maxCorner.x += padding.x ;
        }
        else {
            // then we assume the door's corridor is the along z-axis
            minCorner.x +=  padding_to_door_innerwall ;
            maxCorner.x -=  padding_to_door_innerwall ;
            minCorner.z -= padding.z ;
            maxCorner.z += padding.z ;
        }
        maxCorner.y -= padding_to_door_innerwall ;
        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;
        // all squares between these two corners are blocked:
        // TODO: doors must be handled differently, to calculate their passable corridors!
        for(int x = corner1.x; x<=corner2.x; x++) {
            for (int y = Math.max(0,corner1.y); y <= corner2.y ; y++) {
                for (int z = corner1.z; z<=corner2.z; z++) {
                    var cube = new DPos3(x,y,z) ;
                    corridor.add(cube) ;
                }
            }

        }
        return corridor ;
    }

    /**
     * Set the obstructing-state of a door to be as indicated by the boolean
     * flag: true would make it blocking (closed), and false would make it non-blocking (open).
     */
    public void  setObstacleBlockingState(WorldEntity door, boolean blocking) {

        var corridorCubes = getDoorCorridorCubes(door) ;
        for(var cube : corridorCubes) {
            var obstacles = knownObstacles.get(cube) ;
            if(obstacles == null || obstacles.isEmpty()) {
                return ;
            }
            for(var o : obstacles) {
                if(o.obstacle.equals(door.id)) {
                    o.isBlocking = blocking ;
                    break ;
                }
            }
        }
    }

    @Override
    public Iterable<DPos3> neighbours(DPos3 p) {
        List<DPos3> candidates = new LinkedList<>() ;
        for (int x = p.x-1 ; x <= p.x+1 ; x++) {
            int ymin = 0 ;
            int ymax = 0 ;
            if (enableFlying) {
                ymin = Math.max(0,p.y-1) ;
                ymax = p.y+1 ;
            }
            for (int y = ymin ; y <= ymax ; y++) {
                for (int z = p.z-1; z <= p.z+1 ; z++) {
                    if(x==p.x && y==p.y && z==p.z) continue;
                    var neighbourCube = new DPos3(x,y,z) ; // a neighbouring cube
                    var obstacle = knownObstacles.get(neighbourCube) ;
                    if(obstacle!=null && obstacle.stream().anyMatch(o -> o.isBlocking)) continue;
                    candidates.add(neighbourCube) ;
                }
            }
        }
        return candidates ;
    }

    @Override
    public Iterable<DPos3> neighbours_explore(DPos3 p) {
        return null;
    }

    @Override
    public float heuristic(DPos3 from, DPos3 to) {
        // using Manhattan distance...
        return CUBE_SIZE * (float) (Math.abs(to.x - from.x) + Math.abs(to.y - from.y) + Math.abs(to.z - from.z)) ;

        // using Euclidean distance...
        // return CUBE_SIZE * (float) Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y) + (to.z - from.z) * (to.z - from.z)) ;
    }

    @Override
    public float heuristic(DPos3 from, Vec3 to) {
        return CUBE_SIZE * (float) (Math.abs(to.x - from.x) + Math.abs(to.y - from.y) + Math.abs(to.z - from.z)) ;
    }

    @Override
    public float distance(DPos3 from, DPos3 to) {
        if (from.x != to.x && from.y != to.y && from.z != to.z) {
            return cubeDiagonalLength ;
        }
        if(from.x == to.x) {
            if (from.y == to.y || from.z == to.z) {
                return CUBE_SIZE;
            }
            return squareDiagonalLength ;
        }
        if(from.y == to.y) {
            if (from.x == to.x || from.z == to.z) {
                return CUBE_SIZE;
            }
            return squareDiagonalLength ;
        }
        // last case is if from.z == to.z
        if (from.x == to.x || from.y == to.y) {
            return CUBE_SIZE;
        }
        return squareDiagonalLength ;
    }

    @Override
    public DPos3 phantom_neighbour(DPos3 node, DPos3 real_neighbour) {
        return real_neighbour;
    }

    @Override
    public boolean isUnknown(DPos3 node) {
        return false;
    }

    @Override
    public void updateUnknown(Vec3 pos, float observation_radius) { }
}
