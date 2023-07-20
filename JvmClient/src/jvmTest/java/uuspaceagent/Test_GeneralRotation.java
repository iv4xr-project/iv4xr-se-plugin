package uuspaceagent;

import eu.iv4xr.framework.spatial.Vec3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test_GeneralRotation {

    void printInfo(Vec3 tobeRotated, Vec3 v, Vec3 target, String info) {
        System.out.println("================= " + info) ;
        System.out.println("** v=" + v + ", target=" + target);
        System.out.println("   to-be-rotated w=" + tobeRotated) ;
        var rotationResult = Rotation.rotate(tobeRotated,v,target) ;
        System.out.println("   rotated w=" + rotationResult) ;

        assertTrue(
                Vec3.dot(v.normalized(),target.normalized())
                - Vec3.dot(tobeRotated.normalized(), rotationResult.normalized())
                <= 0.001f
        ) ;

        if (tobeRotated == v) {
            assertTrue(1f - Vec3.dot(target.normalized(),rotationResult.normalized()) <= 0.001f) ;
        }
    }

    @Test
    public void test_acuteAngle() {
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

        v = new Vec3(1,0,1) ;
        target = new Vec3(1,1,1) ;
        printInfo(v,v,target, "+45");
        printInfo(target,target,v, "-45");


        v = new Vec3(1,1,1) ;
        target = new Vec3(-1,2,1) ;
        printInfo(v,v,target, " some random angle :)");
        printInfo(target,target,v, "- some random angle :)");

        var agentPos  = new Vec3(9,-5,50) ;
        var dest = new Vec3(9,-3,50) ;
        target = Vec3.sub(dest,agentPos) ;
        v = new Vec3(0f,0,-1f) ;
        //printInfo(v,v,target, " some random angle :)");
        printInfo(new Vec3(0,0,1),v,target, " some random angle :)");
        printInfo(new Vec3(0,0,1),target,v, " some random angle :)");
        //printInfo(target,target,v, "- some random angle :)");
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
