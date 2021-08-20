package uuspaceagent;

import eu.iv4xr.framework.extensions.pathfinding.Navigatable;
import eu.iv4xr.framework.spatial.Obstacle;
import eu.iv4xr.framework.spatial.Vec3;
import nl.uu.cs.aplib.utils.Pair;
import spaceEngineers.model.Block;

import java.util.*;

/**
 * Implements a 2D grid-navigation graph. This assumes navigation over a flat 2D surface, within
 * a 3D space. The surface is assumed to be horizontal (so, perpendicular to the y axis), and 0
 * slope.
 *
 * A 2D-grid can be thought to be divided into squares, with square (0,0) has its "lower-left"
 * corner positioned at the grid's origin.
 *
 * The size of the squares are specified by SQUARE_SIZE.
 *
 * In-game blocks can block one or more squares. In this implementation blocks are 3D objects.
 * We make the following assumptions:
 *    (1) the blocks are positioned upright, and not horizontally rotated either.
 *    (2) the blocks are not moving nor rotating.
 *    (3) for now the blocks should be cubes. For non-cubes we need to take their orientation
 *        into account. TODO.
 */
public class Grid2DNav implements Navigatable<Pair<Integer,Integer>>{

    /**
     * The assumed height of the player characters. It is 1.8, we conservatively
     * assume it is 2f.
     */
    public static float AGENT_HEIGHT = 2f ;
    public static float AGENT_WIDTH  = 1f ;
    public static float SQUARE_SIZE = 0.5f ;

    public Vec3 origin ;
    public float squareDiagonalLength = (float) Math.sqrt(2* SQUARE_SIZE * SQUARE_SIZE) ;

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
        int x = (int) (Math.signum(p.x) * Math.floor(Math.abs(p.x) / SQUARE_SIZE)) ;
        int z = (int) (Math.signum(p.z) * Math.floor(Math.abs(p.z) / SQUARE_SIZE)) ;
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
     * Add the block as an obstacle on the grid. This is only the case the block intersects
     * with the grid's 2D plane. This assumes that:
     *    (1) the block's orientation is upright and aligned with the grid (it is not
     *    vertically tilted, nor horizontally angled).
     *    (2) the block does not move nor rotate, so we will not update its position in the future;
     *        though it can be removed if it does not exist anymore.
     *    (3) the block is a cube.
     */
    public void addObstacle(Block block) {

        // check if the block is already added. If so we don't do anything.
        if(allObstacleIDs.contains(block.getId())) {
            return ;
        }

        // check if the block is below the 2D grid, and hence can be ignored:
        Vec3 maxCorner = SEBlockFunctions.getBaseMaxCorner(block) ; // should add rotation if it is not a cube. TODO.
        if(maxCorner.y <= origin.y) return ;

        // check if the block is above the agent's height, and hence can be ignored too:
        Vec3 minCorner = SEBlockFunctions.getBaseMinCorner(block) ; // should add rotation if it is not a cube. TODO.
        if(minCorner.y >= origin.y + AGENT_HEIGHT) return ;

        // else the block intersects with with the 2D grid. We now determine which
        // squares in the grid would be blocked by the block. The following logic
        // assumes that the block is UNROTATED.
        // TODO: a more general approach.
        // add some padding due to agent's body width:
        Vec3 padding = Vec3.mul(new Vec3(AGENT_WIDTH,AGENT_WIDTH,AGENT_WIDTH), 0.5f) ;
        minCorner = Vec3.sub(minCorner,padding) ;
        maxCorner = Vec3.add(maxCorner,padding) ;
        var corner1 = gridProjectedLocation(minCorner) ;
        var corner2 = gridProjectedLocation(maxCorner) ;
        var obstacle = new Obstacle<>(block.getId()) ;
        // all squares between these two corners are blocked:
        // TODO: doors must be handled differently, to calculate their passable corridors!
        for(int x = corner1.fst; x<=corner2.fst; x++) {
            for (int z = corner1.snd; z<=corner2.snd; z++) {
                var square = new Pair<>(x,z) ;
                var olist = knownObstacles.get(square) ;
                if (olist == null) {
                    olist = new LinkedList<>() ;
                    knownObstacles.put(square,olist) ;
                }
                olist.add(obstacle) ;
            }
        }
        // so the block is added as an obstacle. Add it to here too:
        allObstacleIDs.add(block.getId()) ;
    }

    public void addObstacle(Collection<Block> blocks) {
        for(var b : blocks) addObstacle(b);
    }

    /**
     * Use this to update the squares blocked by the block, in case its has changes position or has
     * rotated.
     * TODO.
     */
    public void updateBlockPosition(Block block) {
        throw new UnsupportedOperationException() ;
    }

    /**
     * Remove the obstacles with the given blockId. This means that all squares that are marked
     * as blocked by this block are now cleared.
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
     * Set the obstructing-state of a door to be as indicated by the boolean
     * flag: true would make it blocking (closed), and false would make it non-blocking (open).
     *
     * TODO: this is NOT correct. We need to calculate the passable corridor of a door,
     * and only unblock this corridor.
     */
    public void  setObstacleBlockingState(Block door, boolean blocking) {
        for(var olist : knownObstacles.values()) {
            // olist is all obstacles that obstruct some same square, so now check
            // if one in olist match the given id above:
            for (var o : olist) {
                if (o.obstacle.equals(door.getId())) {
                    o.isBlocking = blocking ;
                    // since in the implementation obstacles are shared, we can return:
                    return ;
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
                if(obstacle.stream().anyMatch(o -> o.isBlocking)) continue;
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
