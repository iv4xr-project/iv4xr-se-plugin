package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;

/**
 * Provide some methods for calculating 3D-rotations.
 */
public class Rotation {

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
        /* this is wrong:
        return new Matrix3D(
                cosalpha, 0, - sinalpha,
                0, 1, 0,
                sinalpha, 0, cosalpha
        ) ;
         */
        return new Matrix3D(
                cosalpha, 0, sinalpha,
                0, 1, 0,
                - sinalpha, 0, cosalpha
        ) ;
    }

    /**
     * Let t, v, and target be three vectors that same the same starting point, let's call it o.
     * The method will rotate t, anchored at o, with the same angle as the angle between v and
     * and target.
     *
     * More precisely, we turn the vector t around the axis w, with the angle alpha.
     * The axis w is the normal vector between v and target, and alpha is the angle between
     * v and target.
     *
     * The method is non-detructive (it does not change v), but rather returns a copy that would
     * be the resulting rotated t.
     *
     * Sources:
     *   (1) we use Rodrigues Formula for rotation: https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
     *   (2) for deterning the sign of sinalpha using cross product (but this turns out need to be needed, but could
     *   be useful for future): https://stackoverflow.com/questions/5188561/signed-angle-between-two-3d-vectors-with-same-origin-within-the-same-plane
     *
     */
    public static Vec3 rotate(Vec3 t, Vec3 v, Vec3 target) {
        v = v.normalized() ;
        target = target.normalized() ;
        float cosalpha = Vec3.dot(v,target) ;
        Vec3 cross = Vec3.cross(v,target) ;
        float sinalpha =cross.length() ;
        // float sinalpha = (float) Math.sin(Math.acos(cosalpha)) ;
        //System.out.println(">>> alpha via dot: " + Math.toDegrees(Math.acos(cosalpha))
        //                + ", alpha via cross: " + Math.toDegrees(Math.asin(sinalpha))) ;

        // this can throw an exception if v and target are on the same line (pointing
        // the same or opposite directgion), because the cross product would then be
        // (0,0,0), with length 0.
        // In that case we either do not rotate t, or reverse it.
        //System.out.println(">>> cross:" + cross + ", len=" + sinalpha) ;
        Vec3 k = null ;
        try {
            // System.out.println(">>> cross:" + cross + ", len=" + sinalpha) ;
            k = cross.normalized() ;
        }
        catch(Exception e) {
            // div by 0, so v and target are either the same vector or opposite one
            if(Math.signum(v.x) == Math.signum(target.x)
            && Math.signum(v.y) == Math.signum(target.y)
            && Math.signum(v.z) == Math.signum(target.z)) {
                return t ;
            }
            else {
                return Vec3.mul(t,-1f) ;
            }
        }

        // checking the direction of the angle...
        // Does not work; maybe not even needed..
        /*
        var dir = Vec3.dot(target,Vec3.cross(k,v))  ;
        System.out.println(">>> DIR: " + dir);
        if (dir < 0) {
            sinalpha = - sinalpha ;
        }
         */

        var w1 = Vec3.mul(t,cosalpha) ;
        var w2 = Vec3.mul(Vec3.cross(k,t), sinalpha) ;
        var w3 = Vec3.mul(k, Vec3.dot(k,t) * (1 - cosalpha)) ;
        return Vec3.add(w1, Vec3.add(w2,w3)) ;
    }

    public static Vec3 rotate3d(Vec3 gForward, Vec3 gUp, Vec3 target) {
        Vec3 lForward = new Vec3(0,0,1);
        Vec3 lUp      = new Vec3(0,1,0);
        Vec3 lRight   = new Vec3(1,0,0);
        gForward      = gForward.normalized();
        gUp           = gUp.normalized();
        Vec3 gRight   = Vec3.cross(gUp, gForward) ;

        /* source: https://en.wikipedia.org/wiki/Rotation_matrix
         * source dot-product version: https://phys.libretexts.org/Bookshelves/Classical_Mechanics/Variational_Principles_in_Classical_Mechanics_(Cline)/19%3A_Mathematical_Methods_for_Classical_Mechanics/19.05%3A_Appendix_-_Coordinate_transformations
         */
        Matrix3D R = new Matrix3D(
            Vec3.dot(gRight,   lRight), Vec3.dot(gRight,   lUp), Vec3.dot(gRight,   lForward),
            Vec3.dot(gUp,      lRight), Vec3.dot(gUp,      lUp), Vec3.dot(gUp,      lForward),
            Vec3.dot(gForward, lRight), Vec3.dot(gForward, lUp), Vec3.dot(gForward, lForward)
        );
        return R.apply(target);
    }

    public static Vec3 rotateOLD(Vec3 t, Vec3 v, Vec3 target) {
        v = v.normalized() ;
        target = target.normalized() ;
        float cosalpha = Vec3.dot(target,v) ;
        Vec3 cross = Vec3.cross(target,v) ;
        float sinalpha =cross.length() ;
        // float sinalpha = (float) Math.sin(Math.acos(cosalpha)) ;
        //System.out.println(">>> alpha via dot: " + Math.toDegrees(Math.acos(cosalpha))
        //                + ", alpha via cross: " + Math.toDegrees(Math.asin(sinalpha))) ;
        Vec3 k = cross.normalized() ;

        // Ok, so this is a silly way to determine the sign of the sinalpha. Won't work
        // as it will of course be always positive. But it appears not needed, as the rotation
        // seems to work without it ;)
        //if (Vec3.dot(k,cross) < 0 ) {
        //    sinalpha = - sinalpha ;
        //}

        var w1 = Vec3.mul(t,cosalpha) ;
        var w2 = Vec3.mul(Vec3.cross(k,t), sinalpha) ;
        var w3 = Vec3.mul(k, Vec3.dot(k,t) * (1 - cosalpha)) ;
        return Vec3.add(w1, Vec3.add(w2,w3)) ;
    }
}
