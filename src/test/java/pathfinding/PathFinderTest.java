/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package pathfinding;

import helperclasses.datastructures.Vec3;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

public class PathFinderTest {

    // CreateNavmesh() generates this mesh:
    //     8___9
    //     | \ |
    // 4___7__\6___13
    // | \ | \ | \ |
    // 2__\3__\5__\12
    // | \ |   | \ |
    // 0__\1  10__\11

    private NavMeshContainer CreateNavmesh() {
        return new NavMeshContainer(
                // 12 triangles
                new int[]{
                        0, 1, 2,   // 0
                        1, 2, 3,   // 1
                        2, 3, 4,   // 2
                        3, 4, 7,   // 3
                        3, 5, 7,   // 4
                        5, 6, 7,   // 5
                        6, 7, 8,   // 6
                        6, 8, 9,   // 7
                        5, 6, 12,  // 8
                        6, 12, 13, // 9
                        5, 10, 11, // 10
                        5, 11, 12  // 11
                },
                // 14 vertices
                new Vec3[]{
                        new Vec3(0, 0, 0), // 0
                        new Vec3(1, 0, 0), // 1
                        new Vec3(0, 0, 1), // 2
                        new Vec3(1, 0, 1), // 3
                        new Vec3(0, 0, 2), // 4
                        new Vec3(2, 0, 1), // 5
                        new Vec3(2, 0, 2), // 6
                        new Vec3(1, 0, 2), // 7
                        new Vec3(1, 0, 3), // 8
                        new Vec3(2, 0, 3), // 9
                        new Vec3(2, 0, 0), // 10
                        new Vec3(3, 0, 0), // 11
                        new Vec3(3, 0, 1), // 12
                        new Vec3(3, 0, 2), // 13
                }
        );
    }

    private boolean[] SeenNodes(){
        NavMeshContainer n = CreateNavmesh();
        boolean[] seenNodes = new boolean[n.indices.length];
        for (int i = 0; i<seenNodes.length; i++) {
            seenNodes[i]= true;
        }
        return seenNodes;
    }

    @Test
    public void StraightLineTest() {
        // INIT
        Pathfinder pathfinder = new Pathfinder(CreateNavmesh());
        Vec3 start = new Vec3(0.25, 0, 0.5);
        Vec3 end = new Vec3(1.75, 0, 1.75);

        assertArrayEquals(new Integer[]{5, 4, 3, 2, 1, 0}, pathfinder.trianglePath(start, end, SeenNodes(), new HashSet<>()));

        // is reversed
        Vec3[] expectedPath = new Vec3[]{
                new Vec3(end)
        };

        var actualPath = new Pathfinder(CreateNavmesh()).navigate(start, end, SeenNodes(), new HashSet<>());

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    public void OneCornerTest() {

        // Init
        Pathfinder pathfinder = new Pathfinder(CreateNavmesh());
        Vec3 start = new Vec3(0.75, 0, 1.75);
        Vec3 end = new Vec3(1.25, 0, 2.75);

        assertArrayEquals(new Integer[]{6, 5, 4, 3}, pathfinder.trianglePath(start, end, SeenNodes(), new HashSet<>()));

        // is reversed
        Vec3[] expectedPath = new Vec3[]{
                end,
                new Vec3(1, 0, 2)
        };

        var actualPath = new Pathfinder(CreateNavmesh()).navigate(start, end, SeenNodes(), new HashSet<>());

        for (var c : actualPath)
            System.out.println(c);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    public void TwoCornerTest() {

        // INIT
        Pathfinder pathfinder = new Pathfinder(CreateNavmesh());
        Vec3 start = new Vec3(0.75, 0, 0.5);
        Vec3 end = new Vec3(2.25, 0, 0.5);

        assertArrayEquals(new Integer[]{10, 11, 8, 5, 4, 3, 2, 1}, pathfinder.trianglePath(start, end, SeenNodes(), new HashSet<>()));

        // is reversed
        Vec3[] expectedPath = new Vec3[]{
                end,
                new Vec3(2, 0, 1),
                new Vec3(1, 0, 1)
        };


        var actualPath = new Pathfinder(CreateNavmesh()).navigate(start, end, SeenNodes(), new HashSet<>());

        for (var c : actualPath)
            System.out.println(c);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    public void testPathfindingErrorWithBlockedNodes(){
    	assertThrows(IllegalStateException.class, 
    		() -> { 
    	        //init
    	        Pathfinder pathfinder = new Pathfinder(CreateNavmesh());
    	        //test if we know all nodes
    	        Vec3 start = new Vec3(0,0,0);
    	        Vec3 end = new Vec3(3, 0, 2);

    	        //block node 8 to disable the path to node 13
    	        HashSet<Integer> blockedNodes = new HashSet<>();
    	        blockedNodes.add(8);

    	        var actualPath = new Pathfinder(CreateNavmesh()).navigate(start, end, SeenNodes(), blockedNodes);
    		} 
    	) ;
    }
}
