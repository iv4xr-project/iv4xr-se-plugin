package spaceEngineers;

import eu.iv4xr.framework.spatial.Vec3;

public class SeBlock extends SeEntity {
    public float maxIntegrity;
    public float buildIntegrity;
    public float integrity;
    public String blockType;

    public Vec3 minPosition;
    public Vec3 maxPosition;
    public Vec3 size;
    public Vec3 orientationForward;
    public Vec3 orientationUp;
}
