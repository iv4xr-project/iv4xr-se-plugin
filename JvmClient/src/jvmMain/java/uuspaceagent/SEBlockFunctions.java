package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import spaceEngineers.model.Block;
import spaceEngineers.model.CubeSize;
import spaceEngineers.model.extensions.BlockExtensionsKt;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility functions related to SE blocks.
 */
public class SEBlockFunctions {

    public static Vec3 fromSEVec3(spaceEngineers.model.Vec3 p) {
        return new Vec3(p.getX(), p.getY(), p.getZ()) ;
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
        boolean isLargeBlock = block.getStringProperty("blockType").contains("Large") ;
        if(isLargeBlock)  {
            var size = Vec3.mul(logicalSize,CubeSize.Large.getValue()) ;
            return size ;
        }
        else return Vec3.mul(logicalSize,CubeSize.Small.getValue()) ;
    }


    /*
    public static Vec3 getBaseMinCorner(Block block) {
        // center position:
        Vec3 pos =  fromSEVec3(BlockExtensionsKt.getCenterPosition(block)) ;
        Vec3 halfsize = Vec3.mul(getActualSize(block), 0.5f)  ;
        return Vec3.sub(pos,halfsize) ;
    }
    */

    /**
     * Get the position of the corner, with minimum x,y,z, assuming the block is
     * un-rotated.
     */
    public static Vec3 getBaseMinCorner(WorldEntity block) {
        // center position:
        Vec3 pos = (Vec3) block.getProperty("centerPosition") ;
        Vec3 halfsize = Vec3.mul(getActualSize(block), 0.5f)  ;
        return Vec3.sub(pos,halfsize) ;
    }

    /*
    public static Vec3 getBaseMaxCorner(Block block) {
        // center position:
        Vec3 pos =  fromSEVec3(BlockExtensionsKt.getCenterPosition(block)) ;
        Vec3 halfsize = Vec3.mul(getActualSize(block), 0.5f)  ;
        return Vec3.add(pos,halfsize) ;
    }
    */

    /**
     * Get the position of the corner with the largest x,y,z, assuming the block is
     * un-rotated.
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
    public static Boolean geSlideDoorState(WorldEntity block) {
        if(!block.type.equals("block")) return null ;
        if (block.getStringProperty("blockType").contains("SlideDoor")) {
            return true ;
        }
        return null ;
    }


}
