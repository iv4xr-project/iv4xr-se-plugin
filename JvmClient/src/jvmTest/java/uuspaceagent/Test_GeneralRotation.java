package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uuspaceagent.Matrix3D.getYRotation;

public class Test_GeneralRotation {

    void printInfo(Vec3 tobeRotated, Vec3 v, Vec3 target, String info) {
        System.out.println("================= " + info) ;
        System.out.println("** v=" + v + ", target=" + target);
        System.out.println("   to-be-rotated w=" + tobeRotated) ;
        var rotationResult = GoalAndTacticLib.rotate(tobeRotated,v,target) ;
        System.out.println("   rotated w=" + rotationResult) ;

        assertTrue(1f - Vec3.dot(target.normalized(),rotationResult.normalized()) <= 0.001f) ;
    }

    @Test
    public void test_acuteAngle_left_right() {
        System.out.println(">>> acute-angle cases");
        float tan60 = 1.732f ;
        float tan30 = 0.57735f ;
        Vec3 v = new Vec3(tan60,0,1) ;
        Vec3 target = new Vec3(1,0,0) ;
        printInfo(v ,v,target, "+60");
        printInfo(target,target,v, "-60");

        v = new Vec3(tan30,0,1) ;
        target = new Vec3(1,0,0) ;
        printInfo(v,v,target, "+30");
        printInfo(target,target,v, "-30");
    }

    @Test
    public void test_obstuseAngle_left_right() {
        System.out.println(">>> obtuse-angle cases");
        float tan60 = 1.732f ;
        float tan30 = 0.57735f ;
        Vec3 v = new Vec3(tan60,0,1) ;
        Vec3 target = new Vec3(0,0,-1) ;
        printInfo(v,v,target, "+(90+60) = +150");
        printInfo(target,target,v, "-(90+60) = -150");

        v = new Vec3(tan30,0,1) ;
        target = new Vec3(0,0,-1) ;
        printInfo(v,v,target, "+(90+30) = +120");
        printInfo(target,target,v, "-(90+30) = -120");
    }

    @Test
    public void test_angle_between180_and270() {
        System.out.println(">>> angle between 180-270 cases");
        float tan60 = 1.732f ;
        float tan30 = 0.57735f ;
        Vec3 v = new Vec3(tan60,0,1) ;
        Vec3 target = new Vec3(-1,0,0) ;
        printInfo(v,v,target, "+(180+60) = +240");
        printInfo(target,target,v, "-(180+60) = -240");
    }

    @Test
    public void test_angle_0_and_90() {
        System.out.println(">>> angle 0");
        Vec3 v = new Vec3(1,0,1) ;
        Vec3 target = new Vec3(1,0,1) ;
        printInfo(v,v,target, "+0");

        System.out.println(">>> angle 180");
        v = new Vec3(1,0,1) ;
        target = new Vec3(-1,0,-1) ;
        printInfo(v,v,target, "+180");
        printInfo(target,target,v, "-180");

        //printInfo(target,v, "-90");

        System.out.println(">>> angle 90 cases");
        v = new Vec3(0,0,1) ;
        target = new Vec3(1,0,0) ;
        printInfo(v,v,target, "+90");
        printInfo(target,target,v, "-90");
    }
}
