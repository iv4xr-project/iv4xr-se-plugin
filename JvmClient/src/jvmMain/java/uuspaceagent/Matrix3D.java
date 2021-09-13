package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;

/**
 * 3x3 matrix.
 */
public class Matrix3D {

    public Vec3 a ;
    public Vec3 b ;
    public Vec3 c ;

    public Matrix3D(float ... elements) {
        a = new Vec3(elements[0], elements[1], elements[2]) ;
        b = new Vec3(elements[3], elements[4], elements[5]) ;
        c = new Vec3(elements[6], elements[7], elements[8]) ;
    }
    
    /**
     * Apply the transformation defined by this mattrix on a position v, to obtain a new
     * position as the result of the transformation. E.g. the transformation can define
     * moving v by rotation of some degree to a new position.
     */
    public Vec3 apply(Vec3 v) {
        return new Vec3(Vec3.dot(a,v), Vec3.dot(b,v), Vec3.dot(c,v)) ;
    }

}
