package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;

/**
 * For trying out calculating cos,sin, and angle from dot and cross product.
 */
public class Coba_Calculating_Angle_from_DotCross {


    void test(Vec3 v1, Vec3 v2) {
        System.out.println("=========");
        System.out.println(("v1 = " + v1 + "   , v2 = " + v2));
        v1 = v1.normalized();
        v2 = v2.normalized() ;
        float cosalpha = Vec3.dot(v1,v2) ;
        Vec3 normal = Vec3.cross(v1,v2) ;
        System.out.println("cosalpha = " + cosalpha);
        System.out.println("alpha = " + Math.toDegrees(Math.acos(cosalpha)));
        System.out.println("cross = " + normal);
        System.out.println("sinalpha = " + normal.length());
        System.out.println("angle from sin = " + Math.toDegrees(Math.asin(normal.length()))) ;
    }

    @Test
    public void test1() {
        test(new Vec3(1,0,1), new Vec3(2,0,1)) ;
        test(new Vec3(1,0,1), new Vec3(1,0,2)) ;
        test(new Vec3(2,0,1), new Vec3(1,0,1)) ;
        System.out.println(">>>>>>>") ;
        System.out.println(">>>>>>>") ;
        test(new Vec3(-1,0,1), new Vec3(1,0,1)) ;
        test(new Vec3(-1,0,1), new Vec3(2,0,1)) ;
        test(new Vec3(2,0,1), new Vec3(-1,0,1)) ;
    }

}
