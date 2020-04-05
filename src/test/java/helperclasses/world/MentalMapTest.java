/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.world;

import helperclasses.datastructures.Vec3;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import pathfinding.NavMeshContainer;
import pathfinding.Pathfinder;
import world.MentalMap;
import java.util.HashSet;

public class MentalMapTest {
    double delta = 0.001;

    /**
     * Setup a basic mental map to be used in testing
     * @return The mental map
     */
    private MentalMap setUp() {
        //make a basic navMesh with 10 triangles which are connected in a circle
        NavMeshContainer m = new NavMeshContainer(new int[]{
                0, 1, 2,
                1, 2, 3,
                2, 3, 5,
                3, 4, 5,
                4, 5, 6,
                5, 6, 7,
                5, 7, 8,
                7, 8, 2,
                7, 2, 0
        }, new Vec3[]{
                new Vec3(0, 0, 0),
                new Vec3(1, 0, 1),
                new Vec3(2, 0, 2),
                new Vec3(3, 0, 3),
                new Vec3(4, 0, 4),
                new Vec3(5, 0, 5),
                new Vec3(6, 0, 6),
                new Vec3(7, 0, 7),
                new Vec3(8, 0, 8)
        });

        //we need to construct a pathfinder
        Pathfinder p = new Pathfinder(m);

        //return the mental map
        return new MentalMap(p);
    }

    /**
     * Setup a mental map to be used in testing
     * @return The mental map
     */
    private MentalMap setUp2() {
        //make a basic navMesh with 5 triangles 0-1-4
        //connected in this way                 |   |
        //                                      2---3
        NavMeshContainer m = new NavMeshContainer(new int[]{
                0, 1, 2,
                1, 2, 3,
                0, 2, 4,
                2, 4, 5,
                2, 3, 5
        }, new Vec3[]{
                new Vec3(0, 0, 1),
                new Vec3(1, 0, 0),
                new Vec3(1, 0, 1),
                new Vec3(2, 0, 1),
                new Vec3(1, 0, 2),
                new Vec3(2, 0, 2),
        });

        //we need to construct a pathfinder
        Pathfinder p = new Pathfinder(m);

        //return the mental map
        return new MentalMap(p);
    }

    /**
     *  set the visibility of all nodes to true to make the pathfinding possible
     * @param m: The mental map to be updated
     */
    private void SetVisibilityTrue(MentalMap m){
        //set all vertices to visible
        int[] seenVertices = new int[m.getKnownVertices().length];
        for(int i = 0; i < m.getKnownVertices().length; i++){
            seenVertices[i] = i;
        }
        m.updateKnownVertices(1,seenVertices);
    }

    /**
     * Check if the constructor can build a correct mental map from given input
     */
    @Test
    public void checkConstructorCorrect() {
        //setup
        MentalMap m = setUp();

        assertNotNull(m.pathFinder);
        assertEquals(9, m.getKnownVertices().length);

        //check that no vertice has been seen on setup
        for (int i = 0; i < m.getKnownVertices().length; i++) {
            assertFalse(m.getKnownVertices()[i]);
        }

    }

    /**
     * Check if the forced path finding with a mental map is being executed, path finding for correct paths
     * tests are in the Astar tests
     */
    @Test
    public void checkNavigateForceCorrect() {
        //setup
        MentalMap m = setUp();
        SetVisibilityTrue(m);

        //Find a path between triangle 0 and 3
        Vec3 goal = new Vec3(3.5, 0, 3.5);
        Vec3[] result = m.navigateForce(new Vec3(0, 0, 0), new Vec3(goal), new HashSet<>());

        //check if the path finding was successful
        assertTrue(result != null);
        m.applyPath(1,new Vec3(goal), result);

        //check if the goal location remained the same
        assertEquals(goal.x, m.getGoalLocation().x, delta);
        assertEquals(goal.y, m.getGoalLocation().y, delta);
        assertEquals(goal.z, m.getGoalLocation().z, delta);
    }

    /**
     * Check if the forced path finding is still correct if multiple are being executed behind each other
     */
    @Test
    public void checkNavigateForceMultiple() {
        //setup
        MentalMap m = setUp();
        SetVisibilityTrue(m);

        //Find a path between triangle 0 and 2
        Vec3 goal1 = new Vec3(3.5, 0, 3.5);
        Vec3[] result1 = m.navigateForce(new Vec3(0, 0, 0), new Vec3(goal1), new HashSet<>());

        //check if the path finding was successful
        assertTrue(result1 != null);
        m.applyPath(1,new Vec3(goal1), result1);

        //check if the goal location remained the same
        assertEquals(goal1.x, m.getGoalLocation().x, delta);
        assertEquals(goal1.y, m.getGoalLocation().y, delta);
        assertEquals(goal1.z, m.getGoalLocation().z, delta);

        //find a path between triangle 0 and 6
        Vec3 goal2 = new Vec3(7, 0, 7);
        Vec3[] result2 = m.navigateForce(new Vec3(0, 0, 0), new Vec3(goal2), new HashSet<>());
        m.applyPath(2,new Vec3(goal2), result2);

        //check if the path finding was successful
        assertTrue(result2 != null);

        //check if the goal location changed the same
        assertEquals(goal2.x, m.getGoalLocation().x, delta);
        assertEquals(goal2.y, m.getGoalLocation().y, delta);
        assertEquals(goal2.z, m.getGoalLocation().z, delta);
    }

    /**
     * Check if the path finding is still correct if multiple are being executed behind each other
     */
    @Test
    public void checkNavigateMultiple() {
        //setup
        MentalMap m = setUp();
        SetVisibilityTrue(m);

        //Find a path between triangle 0 and 3
        Vec3 goal = new Vec3(3.5, 0, 3.5);
        Vec3[] result1 = m.navigate(new Vec3(0, 0, 0), new Vec3(goal), new HashSet<>());
        m.applyPath(1,new Vec3(goal), result1);

        //check if the path finding was successful
        assertTrue(result1 != null);

        //check if the goal location remained the same
        assertEquals(goal.x, m.getGoalLocation().x, delta);
        assertEquals(goal.y, m.getGoalLocation().y, delta);
        assertEquals(goal.z, m.getGoalLocation().z, delta);

        //Find the same path again
        Vec3[] result2 = m.navigate(new Vec3(0, 0, 0), new Vec3(goal), new HashSet<>());
        m.applyPath(2,new Vec3(goal), result2);

        //check if the path finding was successful
        assertTrue(result2 != null);

        //check if the goal location changed the same
        assertEquals(goal.x, m.getGoalLocation().x, delta);
        assertEquals(goal.y, m.getGoalLocation().y, delta);
        assertEquals(goal.z, m.getGoalLocation().z, delta);
    }

    /**
     * Check the updating of seen vertices
     */
    @Test
    public void checkUpdateSeenVertices() {
        //setup
        MentalMap m = setUp();

        //update the seen nodes
        int[] updateVertices = new int[]{1, 2, 5};
        m.updateKnownVertices(1,updateVertices);

        //check that that only the seen vertices have been updated
        for (int i = 0; i < m.getKnownVertices().length; i++) {
            if (i == 1 || i == 2 || i == 5) {
                assertTrue(m.getKnownVertices()[i]);
            } else {
                assertFalse(m.getKnownVertices()[i]);
            }
        }
    }

    /**
     * Check the updating of seen vertices with an empty array
     */
    @Test
    public void checkUpdateSeenVerticesEmptyArray() {
        //setup
        MentalMap m = setUp();

        //update the seen nodes
        int[] updateVertices = new int[0];
        m.updateKnownVertices(1,updateVertices);

        //check that that only the seen vertices have been updated
        for (int i = 0; i < m.getKnownVertices().length; i++) {
            assertFalse(m.getKnownVertices()[i]);
        }
    }

    /**
     * Check if the get unknownNeighbourClosestTo returns the correct node
     */
    @Test
    public void testGetUnknownNeighbourClosestTo(){
        MentalMap m = setUp2();
        //update the known nodes so we only know node 0
        m.updateKnownVertices(1,new int[]{0,1});

        //try to get find node 3 when starting from node 0
        Vec3 node = m.getUnknownNeighbourClosestTo(m.pathFinder.graph.toVec3(0), m.pathFinder.graph.toVec3(3), new HashSet<>());

        //check if node 2 is found
        assertEquals(0, node.distance(m.pathFinder.graph.toVec3(0)), 0.1);
    }

    /**
     * Check if the get unknownNeighbourClosestTo returns an exception if the path is blocked
     */
    @Test
    public void testGetUnknownNeighbourClosestToWithBlocked(){
        MentalMap m = setUp2();
        //update the known nodes so we only know node 0
        m.updateKnownVertices(1,new int[]{0,1});

        //try to get node 3 when starting from node 0 to find node 3
        Vec3 node = m.getUnknownNeighbourClosestTo(m.pathFinder.graph.toVec3(0), m.pathFinder.graph.toVec3(3), new HashSet<>());

        //check that node 2 is found
        assertEquals(0, node.distance(m.pathFinder.graph.toVec3(0)), 0.1);
    }



    /**
     * Check if the updating of the current way point works correctly if the agent is close to it
     */
    @Test
    public void checkUpdateCurrentWayPointShortDistance() {
        //setup
        MentalMap m = setUp();
        SetVisibilityTrue(m);

        //Find a path between triangle 0 and 3
        Vec3 goal = new Vec3(3.5, 0, 3.5);
        Vec3[] result = m.navigate(new Vec3(0, 0, 0), new Vec3(goal), new HashSet<>());
        m.applyPath(1,new Vec3(goal), result);

        //get the way point
        Vec3 wayPoint = m.getNextWayPoint();
        Vec3 pos = new Vec3(wayPoint);
        pos.x += 0.001;

        //check if the the new way point is the same
        m.updateCurrentWayPoint(pos);

        //check that the new way point is a certain distance moved
        assertNotEquals(0, wayPoint.distance(m.getNextWayPoint()), 0.1);
    }

    /**
     * Check if the updating of the current way point is not changed when the agent is far away from it
     */
    @Test
    public void checkUpdateCurrentWayPointLongDistance() {
        //setup
        MentalMap m = setUp();
        SetVisibilityTrue(m);

        //Find a path between triangle 0 and 3
        Vec3 goal = new Vec3(3.5, 0, 3.5);
        Vec3[] result = m.navigate(new Vec3(0, 0, 0), new Vec3(goal), new HashSet<>());
        m.applyPath(1,new Vec3(goal), result);

        //get the way point
        Vec3 wayPoint = m.getNextWayPoint();
        Vec3 pos = new Vec3(wayPoint);
        pos.x += 1;

        //check if the the new way point is different from the previous one
        m.updateCurrentWayPoint(pos);

        //check that the new way point is a certain distance moved
        assertEquals(0, wayPoint.distance(m.getNextWayPoint()), delta);
    }

}
