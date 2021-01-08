package spaceEngineers.commands;

import eu.iv4xr.framework.spatial.Vec3;

public class MovementArgs {

    public MovementArgs(Vec3 movement, Vec3 rotation, float roll) {
        this.movement = movement;
        this.rotation3 = rotation;
        this.roll = roll;
    }

    public MovementArgs(Vec3 movement) {
        this.movement = movement;
        this.rotation3 = Vec3.zero();
        this.roll = 0;
    }

    public Vec3 movement;
    public Vec3 rotation3;  // 2-dim vector would be sufficient, z coordinate is ignored for now.
    public float roll;
}
