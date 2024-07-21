package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;

/**
 * Cubical boundary class. Can be used to check if it contains or intersects with other boundaries, points or spheres.
 */
public class Boundary {
    /**
     * The lower boundary of the voxel boundary
     */
    float x, y, z;
    float size;

    public Boundary(Vec3 pos, float size) {
        x = pos.x;
        y = pos.y;
        z = pos.z;
        this.size = size;
        //upperBounds = new Vec3(pos.x + size, pos.y + size, pos.z + size);
    }

    public float size() { return size; } // Because a boundary is always cubic, we can just take the width
    //public float size() { return upperBounds.x - position.x; } // Because a boundary is always cubic, we can just take the width

    public Vec3 upperBounds() { return new Vec3(x + size, y + size, z + size); }

    public Vec3 center() { return new Vec3(x + size * 0.5f, y + size * 0.5f, z + size * 0.5f); }
    //public Vec3 center() { return Vec3.mul(Vec3.add(position, upperBounds), 0.5f); }

    public Vec3 pos() { return new Vec3(x, y, z); }

    public boolean intersects(Boundary other) {
        Vec3 this_upperBounds = this.upperBounds();
        Vec3 other_upperBounds = other.upperBounds();
        if (other.x > this_upperBounds.x || other_upperBounds.x < this.x)
            return false;
        if (other.y > this_upperBounds.y || other_upperBounds.y < this.y)
            return false;
        if (other.z > this_upperBounds.z || other_upperBounds.z < this.z)
            return false;
        return true;
    }

    public boolean contains(Boundary other) {
        Vec3 this_upperBounds = this.upperBounds();
        Vec3 other_upperBounds = other.upperBounds();
        if (other.x < this.x || other_upperBounds.x > this_upperBounds.x)
            return false;
        if (other.y < this.y || other_upperBounds.y > this_upperBounds.y)
            return false;
        if (other.z < this.z || other_upperBounds.z > this_upperBounds.z)
            return false;
        return true;
    }
    public boolean contains(Boundary2 other) {
        Vec3 this_upperBounds = this.upperBounds();
        Vec3 other_upperBounds = other.upperBounds;
        if (other.lowerBounds.x < this.x || other_upperBounds.x > this_upperBounds.x)
            return false;
        if (other.lowerBounds.y < this.y || other_upperBounds.y > this_upperBounds.y)
            return false;
        if (other.lowerBounds.z < this.z || other_upperBounds.z > this_upperBounds.z)
            return false;
        return true;
    }

    public boolean contains(Vec3 point) {
        Vec3 upperBounds = upperBounds();
        return (point.x >= x && point.x <= upperBounds.x
                &&
                point.y >= y && point.y <= upperBounds.y
                &&
                point.z >= z && point.z <= upperBounds.z);
    }
    public boolean contains(DPos3 point) {
        Vec3 upperBounds = upperBounds();
        return (point.x >= x && point.x <= upperBounds.x
                &&
                point.y >= y && point.y <= upperBounds.y
                &&
                point.z >= z && point.z <= upperBounds.z);
    }

    public boolean sphereIntersects(Vec3 center, float radius) {
        Vec3 closest = new Vec3(center.x, center.y, center.z);

        if (center.x < x) closest.x = x;
        else if(center.x > x + size) closest.x = x + size;

        if (center.y < y) closest.y = y;
        else if(center.y > y + size) closest.y = y + size;

        if (center.z < z) closest.z = z;
        else if(center.z > z + size) closest.z = z + size;

        return Vec3.dist(center, closest) <= radius;
    }

    public boolean sphereContains(Vec3 center, float radius) {
        Vec3 furthest = new Vec3(center.x, center.y, center.z);

        if (center.x < x) furthest.x = x + size;
        else if(center.x > x + size) furthest.x = x;

        if (center.y < y) furthest.y = y + size;
        else if(center.y > y + size) furthest.y = y;

        if (center.z < z) furthest.z = z + size;
        else if(center.z > z + size) furthest.z = z;

        return Vec3.dist(center, furthest) <= radius;
    }
}
