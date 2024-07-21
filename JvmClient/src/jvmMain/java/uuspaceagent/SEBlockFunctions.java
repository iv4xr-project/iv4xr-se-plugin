package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import spaceEngineers.model.CubeSize;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility functions related to SE blocks.
 */
public class SEBlockFunctions {

    public static Vec3 fromSEVec3(spaceEngineers.model.Vec3F p) {
        return new Vec3(p.getX(), p.getY(), p.getZ()) ;
    }

    public static spaceEngineers.model.Vec3F toSEVec3(Vec3 p) {
        return new spaceEngineers.model.Vec3F(p.x, p.y, p.z) ;
    }

    /*
    public static Vec3 getActualSize(Block block) {
        Vec3 logicalSize = fromSEVec3(block.getSize()) ;
        boolean isLargeBlock = block.getBlockType().contains("Large") ;
        if(isLargeBlock)  {
            var size = Vec3.mul(logicalSize,CubeSize.Large.getValue()) ;
            return size ;
        }
        else return Vec3.mul(logicalSize,CubeSize.Small.getValue()) ;
    }
    */

    public static Vec3 getActualSize(WorldEntity block) {
        Vec3 logicalSize = block.extent ;
        boolean isLargeBlock = block.getStringProperty("blockType").contains("Large")
                ||  block.getStringProperty("blockType").contains("Window1x1Flat")
                ||  block.getStringProperty("blockType").contains("TargetDummy")
                ;
        if(isLargeBlock)  {
            var size = Vec3.mul(logicalSize,CubeSize.Large.getValue()) ;
            return size ;
        }
        else return Vec3.mul(logicalSize,CubeSize.Small.getValue()) ;
    }

    public enum BlockSides {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
    }

    /**
     * Get the center of a given side of the block, plus some delta distance perpendicular to
     * that side.
     * TODO: This still assumes the block is not vertically tilted.
     */
    public static Vec3 getSideCenterPoint(WorldEntity block, BlockSides side, float delta) {
        Vec3 center = new Vec3(0,0,0) ;
        switch (side) {
            case FRONT : center.x  +=  getActualSize(block).x * 0.5f + delta ; break ;
            case BACK  : center.x  -=  getActualSize(block).x * 0.5f + delta ; break ;
            case RIGHT : center.z  +=  getActualSize(block).z * 0.5f + delta ; break ;
            case LEFT  : center.z  -=  getActualSize(block).z * 0.5f + delta ; break ;
            case TOP    : center.y +=  getActualSize(block).y * 0.5f + delta ; break ;
            case BOTTOM : center.y -=  getActualSize(block).y * 0.5f + delta ; break ;
        }

        // the location of block-center, projected to its "front face", and add the delta :
        Vec3 centerAtFrontFace = center ;

        // now, rotate that centerAtFrontFace location according to the block's forward orientation:
        // for now we don't do its up-orientation.
        Vec3 forwardOrientation = (Vec3) block.getProperty("orientationForward") ;
        Vec3 x_axis = new Vec3(1,0,0) ;
        Matrix3D rotation = Rotation.getYRotation(x_axis,forwardOrientation) ;
        Vec3 rotatedCenterAtFrontFace = rotation.apply(centerAtFrontFace) ;

        // the rotated position is relative to the block's center position.
        // We now add the block's actual center, and that should be it:
        return Vec3.add((Vec3) block.getProperty("centerPosition"), rotatedCenterAtFrontFace) ;
    }

    /**
     * Get the position of the corner, with minimum x,y,z, assuming the block is
     * xyz-aligned.
     */
    public static Vec3 getBaseMinCorner(WorldEntity block) {
        // center position:
        Vec3 pos = (Vec3) block.getProperty("centerPosition") ;
        Vec3 halfsize = Vec3.mul(getActualSize(block), 0.5f)  ;
        return Vec3.sub(pos,halfsize) ;
    }

    /**
     * Get the position of the corner with the largest x,y,z, assuming the block is
     * xyz-aligned.
     */
    public static Vec3 getBaseMaxCorner(WorldEntity block) {
        // center position:
        Vec3 pos = (Vec3) block.getProperty("centerPosition") ;
        Vec3 halfsize = Vec3.mul(getActualSize(block), 0.5f)  ;
        return Vec3.add(pos,halfsize) ;
    }

    static private WorldEntity findWorldEntity(WorldEntity we, String id) {
        if (id.equals(we.id)) return we ;
        for (WorldEntity f : we.elements.values()) {
            WorldEntity z = findWorldEntity(f,id) ;
            if (z != null) return z ;
        }
        return null ;
    }

    /**
     * Recursively search a wom for an entity with the given id. The search is recursive in
     * the sense that it will also search in among sub-entities.
     */
    static public WorldEntity findWorldEntity(WorldModel wom, String id) {
        for(var f : wom.elements.values()) {
            var e = findWorldEntity(f,id) ;
            if (e != null) return e ;
        }
        return null ;
    }

    public static List<String> getAllBlockIDs(WorldModel wom) {
        List<String> blockIds = new LinkedList<>() ;
        for(var e : wom.elements.values()) {
            if(e.type.equals("block")) {
                blockIds.add(e.id) ;
                continue ;
            }
            for(var b : e.elements.values()) {
                if (b.type.equals("block")) {
                    blockIds.add(b.id) ;
                }
            }
        }
        return blockIds ;
    }

    public static List<WorldEntity> getAllBlocks(WorldModel wom) {
        List<WorldEntity> blocks = new LinkedList<>() ;
        for(var e : wom.elements.values()) {
            if(e.type.equals("block")) {
                blocks.add(e) ;
                continue ;
            }
            for(var b : e.elements.values()) {
                if (b.type.equals("block")) {
                    blocks.add(b) ;
                }
            }
        }
        return blocks ;
    }

    /**
     * Get the state of a slide-door. If it is open, we return true, else false. If it is
     * not a door. the method returns null.
     *
     * NOTE: for now, because SE does not propagate open/close state of doors, this will
     * always return true.
     */
    public static Boolean getSlideDoorState(WorldEntity block) {
        if(!block.type.equals("block")) return null ;
        if (block.getStringProperty("blockType").contains("SlideDoor")) {
            return block.getBooleanProperty("isOpen") ;
        }
        return null ;
    }

    /**
     * Find the closest block (from the agent's current position) with the given block-type,
     * within the given radius.
     */
    public static WorldEntity findClosestBlock(WorldModel wom, String blockType, float radius) {
        float sqradius = radius*radius ;
        return findClosestBlock(wom,
                   e -> blockType.equals(e.getStringProperty("blockType"))
                        && Vec3.sub(e.position, wom.position).lengthSq() <= sqradius) ;
    }

    /**
     * Return the closest block with the specified property (the selector).
     */
    public static WorldEntity findClosestBlock(WorldModel wom, Predicate<WorldEntity> selector) {
        var candidates =  SEBlockFunctions.getAllBlocks(wom)
                .stream()
                .filter(e -> selector.test(e))
                .collect(Collectors.toList());
        if(candidates.isEmpty()) return null ;

        if(candidates.size() == 1) return candidates.get(0) ;

        // if there are more than one, sort the candidates to get the closest one:
        candidates.sort((e1,e2) -> Float.compare(
                 Vec3.sub(e1.position,wom.position).lengthSq()
                ,Vec3.sub(e2.position,wom.position).lengthSq())) ;

        return candidates.get(0) ;
    }

    /**
     * Return all blocks with the specified property (the selector), sorted on distance.
     */
    public static List<WorldEntity> findBlocksOfType(WorldModel wom, Predicate<WorldEntity> selector) {
        var candidates =  SEBlockFunctions.getAllBlocks(wom)
                .stream()
                .filter(e -> selector.test(e))
                .collect(Collectors.toList());
        if(candidates.isEmpty()) return null ;

        if(candidates.size() == 1) return candidates ;

        // if there are more than one, sort them
        candidates.sort((e1,e2) -> Float.compare(
                Vec3.sub(e1.position,wom.position).lengthSq(),
                Vec3.sub(e2.position,wom.position).lengthSq())) ;

        return candidates ;
    }

    /**
     * HACK: this will replace grids and blocks packaged in this world model with the same
     * entity but marked as dynamic. SE send them as non-dynamic, which causes them to
     * be ignored when merhing woms.
     */
    public static void hackForceDynamicFlag(WorldModel gridsAndBlocks) {
        for(var entry : gridsAndBlocks.elements.entrySet()) {
            entry.setValue(hackForceDynamicFlag(entry.getValue())) ;
        }
    }

    public static WorldEntity hackForceDynamicFlag(WorldEntity e) {
        WorldEntity f = new WorldEntity(e.id, e.type, true) ;
        f.timestamp = e.timestamp ;
        f.lastStutterTimestamp = e.lastStutterTimestamp ;
        f.position = e.position ;
        f.extent = e.extent ;
        f.velocity = e.velocity ;
        f.properties = e.properties ;
        f.elements = e.elements ;
        for(var entry : f.elements.entrySet()) {
            entry.setValue(hackForceDynamicFlag(entry.getValue())) ;
        }
        return f ;
    }


}
