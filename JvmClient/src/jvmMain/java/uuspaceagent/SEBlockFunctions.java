package uuspaceagent;

import eu.iv4xr.framework.mainConcepts.WorldEntity;
import eu.iv4xr.framework.mainConcepts.WorldModel;
import eu.iv4xr.framework.spatial.Vec3;
import spaceEngineers.model.Block;
import spaceEngineers.model.CubeSize;
import spaceEngineers.model.extensions.BlockExtensionsKt;

public class SEBlockFunctions {

    public static Vec3 fromSEVec3(spaceEngineers.model.Vec3 p) {
        return new Vec3(p.getX(), p.getY(), p.getZ()) ;
    }

    public static Vec3 getActualSize(Block block) {
        Vec3 logicalSize = fromSEVec3(block.getSize()) ;
        boolean isLargeBlock = block.getBlockType().contains("Large") ;
        if(isLargeBlock) return Vec3.mul(logicalSize,CubeSize.Large.getValue()) ;
        else return Vec3.mul(logicalSize,CubeSize.Small.getValue()) ;
    }

    /**
     * Get the position of the corner, with minimum x,y,z, assuming the block is
     * un-rotated.
     */
    public static Vec3 getBaseMinCorner(Block block) {
        // center position:
        Vec3 pos =  fromSEVec3(BlockExtensionsKt.getCenterPosition(block)) ;
        Vec3 halfsize = Vec3.mul(getActualSize(block), 0.5f)  ;
        return Vec3.sub(pos,halfsize) ;
    }

    /**
     * Get the position of the corner with the largest x,y,z, assuming the block is
     * un-rotated.
     */
    public static Vec3 getBaseMaxCorner(Block block) {
        // center position:
        Vec3 pos =  fromSEVec3(BlockExtensionsKt.getCenterPosition(block)) ;
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

}
