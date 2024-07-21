package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represent a 3D location in space, but the coordinates are discrete (they are
 * integers).
 */
public class DPos3 implements Serializable {
    public int x = 0 ;
    public int y = 0 ;
    public int z = 0 ;
    public DPos3() { }
    public DPos3(int x, int y, int z) {
        this.x = x ; this.y = y ; this.z = z ;
    }

    /**
     * Turn a Vec3 position to an instance of DPos3. The values x,y,z of the Vec3
     * are "floored" using Math.floor. This means for example (3.9, -3.9, 0) will
     * be converted to (3,-3,0).
     */
    public DPos3(Vec3 v) {
        x = (int) Math.floor(v.x) ;
        y = (int) Math.floor(v.y) ;
        z = (int) Math.floor(v.z) ;
    }
    /**
     * @return A + B
     */
    public static DPos3 add(DPos3 a, DPos3 b) {
        return new DPos3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * @return A - B
     */
    public static DPos3 sub(DPos3 a, DPos3 b) {
        return new DPos3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DPos3 dPos3 = (DPos3) o;
        return x == dPos3.x && y == dPos3.y && z == dPos3.z;
    }

    @Override
    public int hashCode() {
       return 961*x +31*y + z ;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")" ;
    }

    public Vec3 toVec3() {
        return new Vec3(x,y,z) ;
    }
}
