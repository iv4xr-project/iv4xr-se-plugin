package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;

/**
 * Cuboid boundary class. Can be used to check if it contains or intersects with other boundaries, points or spheres.
 */
public class Boundary2 {
    Vec3 lowerBounds, upperBounds;

    public Boundary2(Vec3 lowerBounds, Vec3 upperBounds) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    public boolean intersects(Boundary other) {
        Vec3 other_upperBounds = other.upperBounds();
        if (other.x > this.upperBounds.x || other_upperBounds.x < this.lowerBounds.x)
            return false;
        if (other.y > this.upperBounds.y || other_upperBounds.y < this.lowerBounds.y)
            return false;
        if (other.z > this.upperBounds.z || other_upperBounds.z < this.lowerBounds.z)
            return false;
        return true;
    }

    public boolean contains(Boundary other) {
        Vec3 other_upperBounds = other.upperBounds();
        if (other.x < this.lowerBounds.x || other_upperBounds.x > this.upperBounds.x)
            return false;
        if (other.y < this.lowerBounds.y || other_upperBounds.y > this.upperBounds.y)
            return false;
        if (other.z < this.lowerBounds.z || other_upperBounds.z > this.upperBounds.z)
            return false;
        return true;
    }

    public boolean contains(Vec3 point) {
        return (point.x >= lowerBounds.x && point.x <= upperBounds.x
                &&
                point.y >= lowerBounds.y && point.y <= upperBounds.y
                &&
                point.z >= lowerBounds.z && point.z <= upperBounds.z);
    }
    public boolean contains(DPos3 point) {
        return (point.x >= lowerBounds.x && point.x <= upperBounds.x
                &&
                point.y >= lowerBounds.y && point.y <= upperBounds.y
                &&
                point.z >= lowerBounds.z && point.z <= upperBounds.z);
    }
}
