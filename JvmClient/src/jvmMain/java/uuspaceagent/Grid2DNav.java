package uuspaceagent;

import eu.iv4xr.framework.extensions.pathfinding.Navigatable;
import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.spatial.Obstacle;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Block;

import java.util.*;

/**
 * Implements a 2D grid-navigation graph. This assumes navigation over a flat 2D surface, within
 * a 3D space. The surface is assumed to be horizontal (so, perpendicular to the y axis), and 0
 * slope. The sides of the squares are parallel to the x and z axis.
 *
 * A 2D-grid can be thought to be divided into squares, with square (0,0) has its "lower-left"
 * corner positioned at the grid's origin. (So, the center point of (0,0) would be at
 * (origin.x + SQUARE_SIZE/2, origin.y, origin.z + SQUARE_SIZE/2)).
 *
 * The size of the squares are specified by SQUARE_SIZE.
 *
 * In-game blocks can block one or more squares. In this implementation blocks are 3D objects.
 * We make the following assumptions:
 *    (1) the blocks are positioned upright, and not horizontally rotated either.
 *    (2) the blocks are not moving nor rotating.
 *    (3) for now the blocks should be cubes. For non-cubes we need to take their orientation
 *        into account. TODO.
 *
 *  TODO: for now we don't check if a square on the grid is SOLID (e.g. it could be a gaping
 *  hole in space). We can do this by maintaining a list of squares with solid floor.
 */
public class Grid2DNav implements Navigatable<Pair<Integer,Integer>>{

    /**
     * The assumed height of the player characters. It is 1.8, we conservatively
     * assume it is 2f.
     */
    public static float AGENT_HEIGHT = 2f ;
    public static float AGENT_WIDTH  = 1f ;
    public static float SQUARE_SIZE = 0.5f ;

    public float squareDiagonalLength = (float) Math.sqrt(2* SQUARE_SIZE * SQUARE_SIZE) ;

    /**
     * Define the world-coordinate of the grid origin. The "bottom-left" corner of
     * square (0,0) is the location of this origin. (So, the center point of (0,0)
     * would be at (origin.x + SQUARE_SIZE/2, origin.y, origin.z + SQUARE_SIZE/2)).
     */
    public Vec3 origin ;


    /**
     * Map squares to the obstacles that block them. Note that there might be multiple
     * obstacles that block the same square,
     */
    public Map<Pair<Integer,Integer>,List<Obstacle<String>>> knownObstacles = new HashMap<>() ;

    /**
     * Keep track of all block-id that intersect the this 2D-grid (and hence blocking squares
     * in the grid).
     */
    public Set<String> allObstacleIDs = new HashSet<>() ;

    public Grid2DNav() { }

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


    public Pair<Integer,Integer> gridProjectedLocation(Vec3 p) {
        p = Vec3.sub(p,origin) ;
        int x = (int) ((Math.signum(p.x) * Math.floor(Math.abs(p.x) / SQUARE_SIZE))) ;
        int z = (int) ((Math.signum(p.z) * Math.floor(Math.abs(p.z) / SQUARE_SIZE))) ;
        return new Pair<>(x,z) ;
    }

    public Vec3 getSquareCenterLocation(Pair<Integer,Integer> sq) {
        float x = (((float) sq.fst) + 0.5f) *SQUARE_SIZE + origin.x ;
        float z = (((float) sq.snd) + 0.5f) *SQUARE_SIZE + origin.z ;
        return new Vec3(x,origin.y,z) ;
    }

    /**
     * Square-distance from a location p to the center of a square; p is assumed to be on the grid
     * (so we ignore its y-value).
     */
    public float squareDistanceToSquare(Vec3 p, Pair<Integer,Integer> sq) {
        Vec3 q = getSquareCenterLocation(sq) ;
        q.y = p.y ;
        return Vec3.sub(q,p).lengthSq() ;
    }

    /**
     * Return true if the block can potentially obstruct travel on the grid. This is the case
     * if the block intersects with the grid, or it hangs too low that the agent would
     * bump into its bottom.
     * NOTE: Currently the method assumes the block is NOT tilted and has its vertical
     * sides aligned with the x and z axis.
     */
    boolean canObstructTravelOnGrid(WorldEntity block) {

        // check if the block is below the 2D grid, and hence can would not obstruct:
        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(block) ; // should add rotation if it is not a cube. TODO.

        // Adding 0.1 offset for some bit of seemingly inaccuracy in the sampling of the agent's
        // ground position, which is used as the base of the origin position of this grid.
        // Without this offset, blocks that are just below the grid surface, and touching it to form
        // the grid's solid floor will appear as obstructing.
        float correction_offset = 0.1f ;
        if(maxCorner.y <= origin.y + correction_offset) return false ; // adding 0.1 for inaccuracy....

        // check if the block is not hanging too low:
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(block) ; // should add rotation if it is not a cube. TODO.
        if(minCorner.y >= origin.y + AGENT_HEIGHT) return false ;

        // the block can obstruct:
        return true ;
    }

    /**
     * Calculate the squares on this 2D-grid that could be obstructed by the given block. The method
     * does NOT take the block dynamic state into account (e.g. an open door).
     * Some padding is added to account for the agent's physical width. Currently, this assumes that:
     *      *    (1) the block's orientation is upright and aligned with the grid (it is not
     *      *    vertically tilted, nor horizontally angled).
     *      *    (2) the block does not move nor rotate, so we will not update its position in the future;
     *      *        though it can be removed if it does not exist anymore.
     *      *    (3) the block is a cube.
     */
    List<Pair<Integer,Integer>> getObstructedSquares(WorldEntity block) {

        List<Pair<Integer,Integer>> obstructed = new LinkedList<>() ;

        if (! canObstructTravelOnGrid(block)) { // the block can't physically obstruct travel:
            return obstructed ;
        }

        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(block) ; // should add rotation if it is not a cube. TODO.
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(block) ; // should add rotation if it is not a cube. TODO.
        // assumes that the block is UNROTATED.
        // TODO: a more general approach.
        // add some padding due to agent's body width:
        Vec3 padding = Vec3.mul(new Vec3(AGENT_WIDTH,AGENT_WIDTH,AGENT_WIDTH), 0.6f) ;
        minCorner = Vec3.sub(minCorner,padding) ;
        maxCorner = Vec3.add(maxCorner,padding) ;
        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;
        // all squares between these two corners are blocked:
        // TODO: doors must be handled differently, to calculate their passable corridors!
        for(int x = corner1.fst; x<=corner2.fst; x++) {
            for (int z = corner1.snd; z<=corner2.snd; z++) {
                var square = new Pair<>(x,z) ;
                obstructed.add(square) ;
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

        var obstructedSquares = getObstructedSquares(block) ;
        var obstacle = new Obstacle<>(block.id) ;
        obstacle.isBlocking = true ;
        // all squares between these two corners are blocked:
        // TODO: doors must be handled differently, to calculate their passable corridors!
        boolean added = false ;
        for(var square : obstructedSquares) {
            var olist = knownObstacles.get(square) ;
            if (olist == null) {
                olist = new LinkedList<>() ;
                knownObstacles.put(square,olist) ;
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
     * Use this to update the squares blocked by the block, in case its has changes position or has
     * rotated.
     * TODO.
     */
    public void updateBlockPosition(WorldEntity block) {
        throw new UnsupportedOperationException() ;
    }

    /**
     * Remove the obstacles with the given blockId. This means that all squares that are marked
     * as blocked by this block are now cleared. Use this to unblock squares when the block
     * does not exist anymore.
     */
    public void removeObstacle(String blockId) {
        List<Pair<Integer,Integer>> squaresThatBecomeFree = new LinkedList<>() ;
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
            if (olist.isEmpty()) squaresThatBecomeFree.add(e.getKey()) ;

        }
        for(var sq : squaresThatBecomeFree) {
            knownObstacles.remove(sq) ;
        }
        allObstacleIDs.remove(blockId) ;
    }

    /**
     * Return the squares representing the walkable corridor of a door. This assumes the
     * door is of size 1x1x1 large block, and it is not tilted, nor angled towards x or z axis.
     * The door is assumed to be obstructing on this grid.
     */
    List<Pair<Integer,Integer>> getDoorCorridorSquares(WorldEntity door) {

        List<Pair<Integer,Integer>> corridor = new LinkedList<>() ;

        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(door) ; // should add rotation if it is not a cube. TODO.
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(door) ; // should add rotation if it is not a cube. TODO.
        // the door horizontal orientation
        Vec3 hdir = (Vec3) door.getProperty("orientationForward") ; hdir.y = 0 ;
        float angle_with_x_axis = Vec3.dot(new Vec3(1,0,0), hdir) ;
        // assumes that the door is UNROTATED.
        // TODO: a more general approach.
        // add some padding due to agent's body width:
        Vec3 padding = Vec3.mul(new Vec3(AGENT_WIDTH,AGENT_WIDTH,AGENT_WIDTH), 0.6f) ;
        float padding_to_door_innerwall = 0.75f ;
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
        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;
        // all squares between these two corners are blocked:
        // TODO: doors must be handled differently, to calculate their passable corridors!
        for(int x = corner1.fst; x<=corner2.fst; x++) {
            for (int z = corner1.snd; z<=corner2.snd; z++) {
                var square = new Pair<>(x,z) ;
                corridor.add(square) ;
            }
        }
        return corridor ;
    }

    /**
     * Set the obstructing-state of a door to be as indicated by the boolean
     * flag: true would make it blocking (closed), and false would make it non-blocking (open).
     */
    public void  setObstacleBlockingState(WorldEntity door, boolean blocking) {

        var corridorSquares = getDoorCorridorSquares(door) ;
        for(var sq : corridorSquares) {
            var obstacles = knownObstacles.get(sq) ;
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
    public Iterable<Pair<Integer, Integer>> neighbours(Pair<Integer, Integer> p) {
        List<Pair<Integer,Integer>> candidates = new LinkedList<>() ;
        for (int x = p.fst-1 ; x <= p.fst+1 ; x++) {
            for (int z = p.snd-1; z <= p.snd+1 ; z++) {
                if(x==p.fst && z==p.snd) continue;
                var neighbourSquare = new Pair<>(x,z) ; // a neighbouring square
                var obstacle = knownObstacles.get(neighbourSquare) ;
                if(obstacle!=null && obstacle.stream().anyMatch(o -> o.isBlocking)) continue;
                candidates.add(neighbourSquare) ;
            }
        }
        return candidates ;
    }

    @Override
    public float heuristic(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        // using Manhattan distance...
        return (float) (SQUARE_SIZE * Math.abs(to.fst - from.fst) + Math.abs(to.snd - from.snd)) ;
    }

    @Override
    public float distance(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        if(from.fst.equals(to.fst) || from.snd.equals(to.snd)) return SQUARE_SIZE;
        else return squareDiagonalLength ;
    }
}
