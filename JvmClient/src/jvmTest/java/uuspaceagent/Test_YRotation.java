package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.Rotation.getYRotation;

public class Test_YRotation {

    void printInfo(Vec3 v, Vec3 target, String info) {
        v = v.normalized() ;
        target = target.normalized() ;
        var cosalpha = Vec3.dot(v,target) ;
        var cross = Vec3.cross(v,target) ;
        var sinalpha = cross.length() ;
        System.out.println("================= " + info) ;
        System.out.println("** v=" + v + ", target=" + target);
        System.out.println("   from dot   cosalpha =" + cosalpha) ;
        System.out.println("   from cross sinalpha =" + sinalpha) ;
        System.out.println("   v x t =" + cross) ;
        System.out.println("   t x v =" + Vec3.cross(target,v)) ;

        var rotation = getYRotation(v,target) ;
        var rotatedv = rotation.apply(v) ;
        var angleBetweenRotatedvAndTarget = Math.toDegrees(Math.acos(Vec3.dot(rotatedv.normalized(),target))) ;

        System.out.println("   rotated v =" + rotatedv) ;
        System.out.println("   alpha rotated-v to target =" + angleBetweenRotatedvAndTarget) ;
        assertTrue(Math.abs(angleBetweenRotatedvAndTarget) <= 0.01f) ;
    }

    @Test
    public void test_acuteAngle_left_right() {
        System.out.println(">>> acute cases");
        float tan60 = 1.732f ;
        float tan30 = 0.57735f ;
        Vec3 v = new Vec3(tan60,0,1) ;
        Vec3 target = new Vec3(1,0,0) ;
        printInfo(v,target, "+60");

        printInfo(target,v, "-60");

        v = new Vec3(tan30,0,1) ;
        target = new Vec3(1,0,0) ;
        printInfo(v,target, "+30");

        printInfo(target,v, "-30");

    }

    @Test
    public void test_obstuseAngle_left_right() {
        System.out.println(">>> obtuse cases");
        float tan60 = 1.732f ;
        float tan30 = 0.57735f ;
        Vec3 v = new Vec3(tan60,0,1) ;
        Vec3 target = new Vec3(0,0,-1) ;
        printInfo(v,target, "+(90+60) = +150");
        printInfo(target,v, "-(90+60) = -150");

        v = new Vec3(tan30,0,1) ;
        target = new Vec3(0,0,-1) ;
        printInfo(v,target, "+(90+30) = +120");
        printInfo(target,v, "-(90+30) = -120");
    }

    @Test
    public void test_angle_between180_and270() {
        System.out.println(">>> angle between 180-270 cases");
        float tan60 = 1.732f ;
        float tan30 = 0.57735f ;
        Vec3 v = new Vec3(tan60,0,1) ;
        Vec3 target = new Vec3(-1,0,0) ;
        printInfo(v,target, "+(180+60) = +240");
        printInfo(target,v, "-(180+60) = -240");
    }

    @Test
    public void test_angle_0_and_90() {
        System.out.println(">>> angle 0");
        Vec3 v = new Vec3(1,0,1) ;
        Vec3 target = new Vec3(1,0,1) ;
        printInfo(v,target, "+0");

        System.out.println(">>> angle 180");
        v = new Vec3(1,0,1) ;
        target = new Vec3(-1,0,-1) ;
        printInfo(v,target, "+180");
        printInfo(target,v, "-180");

        //printInfo(target,v, "-90");

        System.out.println(">>> angle 90 cases");
        v = new Vec3(0,0,1) ;
        target = new Vec3(1,0,0) ;
        printInfo(v,target, "+90");
        printInfo(target,v, "-90");
    }
}
