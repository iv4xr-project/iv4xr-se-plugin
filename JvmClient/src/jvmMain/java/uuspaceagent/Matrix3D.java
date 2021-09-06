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

    /**
     * Get a rotation matrix that would rotate the vector v towards the vector target,
     * where the rotation is around the y-axis (so, the y-values would remain the same).
     * The two vectors v and target are assume tobe on the same XZ plane (they have
     * the same y-values).
     *
     * The rotation is anchored at v's origin (so, (0,0,0)).
     *
     * Applying the rotation matrix on v would make it point to the same direction as
     * the vector target, projected to the xz-plane.
     *
     * Once the rotation matrix is obtained, it can be applied to a Vec3 position
     * to calculate the new position as the result of the rotation.
     */
    public static Matrix3D getYRotation(Vec3 v, Vec3 target) {
        v = v.copy() ; v.y = 0 ;
        target = target.copy() ; target.y = 0 ;
        v = v.normalized() ;
        target = target.normalized() ;
        float cosalpha = Vec3.dot(v,target) ;
        Vec3 cross = Vec3.cross(v,target) ;
        float sinalpha = Math.signum(cross.y) * cross.length() ;
        return new Matrix3D(
                cosalpha, 0, - sinalpha,
                0, 1, 0,
                sinalpha, 0, cosalpha
        ) ;
    }

}
