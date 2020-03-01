/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package pathfinding;

import helperclasses.astar.Astar;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.mesh.TriangleMesh;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TriangleGraphTest {

    private TriangleGraph basicGraph() {
        return new TriangleGraph(
                new TriangleMesh(
                        new NavMeshContainer(
                                new int[]{0, 1, 2, 1, 2, 3},
                                new Vec3[]{
                                        new Vec3(0, 0, 0),
                                        new Vec3(3, 0, 0),
                                        new Vec3(0, 0, 3),
                                        new Vec3(3, 0, 3)
                                }
                        )
                )
        );
    }

    @Test
    public void testNeighbour() {

        TriangleGraph g = basicGraph();

        assertArrayEquals(new int[]{1}, g.getNeighbours(0)); // only one neighbour
        assertArrayEquals(new int[]{0}, g.getNeighbours(1)); // only one neighbour
    }

    /**
     * Test the visibility of neighbours when none of the nodes is explored
     */
    @Test
    public void testVisibleNeighbourVisibilityOff() {

        TriangleGraph g = basicGraph();
        boolean[] seenNodes = new boolean[4];

        assertArrayEquals(new int[]{}, g.getKnownNeighbours(0, seenNodes, new HashSet<>())); // no neighbour
        assertArrayEquals(new int[]{}, g.getKnownNeighbours(1, seenNodes, new HashSet<>())); // no neighbour
    }

    /**
     * Test the visibility of neighbours when all nodes are explored
     */
    @Test
    public void testVisibleNeighbourVisibilityOn() {

        TriangleGraph g = basicGraph();
        boolean[] seenNodes = new boolean[4];
        Arrays.fill(seenNodes, true);

        assertArrayEquals(new int[]{1}, g.getKnownNeighbours(0, seenNodes, new HashSet<>())); // one neighbour
        assertArrayEquals(new int[]{0}, g.getKnownNeighbours(1, seenNodes, new HashSet<>())); // one neighbour
    }

    /**
     * This test will test if triangles which partly overlap can correctly find neighbours
     */
    @Test
    public void testMultipleNeighboursOverlapping() {
        TriangleGraph g = new TriangleGraph(
                new TriangleMesh(
                        new NavMeshContainer(
                                new int[]{0, 1, 2, 1, 2, 3, 0, 3, 2, 0, 1, 3},
                                new Vec3[]{
                                        new Vec3(0, 0, 0),
                                        new Vec3(3, 0, 0),
                                        new Vec3(0, 0, 3),
                                        new Vec3(3, 0, 3)
                                }
                        )
                )
        );
        //check if each of the triangles has the correct amount of nodes
        assertEquals(3, g.getNeighbours(0).length);
        assertEquals(3, g.getNeighbours(1).length);
        assertEquals(3, g.getNeighbours(2).length);
        assertEquals(3, g.getNeighbours(3).length);
    }

    @Test
    public void testMultipleNeighbours() {
        TriangleGraph g = new TriangleGraph(
                new TriangleMesh(
                        new NavMeshContainer(
                                new int[]{0, 1, 2, 1, 2, 3, 4, 3, 2, 5, 2, 0},
                                new Vec3[]{
                                        new Vec3(0, 0, 0),
                                        new Vec3(3, 0, 0),
                                        new Vec3(0, 0, 3),
                                        new Vec3(3, 0, 3),
                                        new Vec3(1, 0, 3),
                                        new Vec3(3, 0, 1)
                                }
                        )
                )
        );

        //check if each of the triangles has the correct amount of nodes
        assertEquals(2, g.getNeighbours(0).length);
        assertEquals(2, g.getNeighbours(1).length);
        assertEquals(1, g.getNeighbours(2).length);
        assertEquals(1, g.getNeighbours(3).length);
    }

    @Test
    public void testCentre() {

        TriangleGraph g = basicGraph();

        assertEquals(new Vec3(1, 0, 1), g.toVec3(0)); // centre
        assertEquals(new Vec3(2, 0, 2), g.toVec3(1)); // centre
    }

    @Test
    public void testClosestNode() {

        TriangleGraph g = basicGraph();

        assertEquals(0, (int) g.vecToNode(new Vec3(1.12, 0, 1)));
        assertEquals(1, (int) g.vecToNode(new Vec3(2, 0.111, 1.8)));
        assertEquals(0, (int) g.vecToNode(new Vec3(1.49, 0, 1.49)));
        assertEquals(1, (int) g.vecToNode(new Vec3(1.51, 0, 1.51)));
    }

    @Test
    public void testPointInTriangle() {

        TriangleGraph g = basicGraph();

        assertTrue(g.pointInTriangleXZ(new Vec3(0, 0, 0), g.nodes.get(0)));

        assertTrue(g.pointInTriangleXZ(new Vec3(1.5, 0, 1.5), g.nodes.get(0)));
        assertTrue(g.pointInTriangleXZ(new Vec3(1.5, 0, 1.5), g.nodes.get(1)));

        assertTrue(g.pointInTriangleXZ(new Vec3(1.6, 0, 1.6), g.nodes.get(1)));
        assertFalse(g.pointInTriangleXZ(new Vec3(1.4, 0, 1.4), g.nodes.get(1)));
    }

    @Test
    public void testWeight() {

        TriangleGraph g = basicGraph();

        // it uses the squared distance for efficiency
        assertEquals(2, g.weightOfConnection(0, 1), 0.00000000001);
    }

    @Test
    public void testHeuristic() {

        TriangleGraph g = basicGraph();

        // it uses the squared distance for efficiency
        assertEquals(2, g.heuristic(0, new Vec3(2, 0, 2)), 0.00000000001);
        assertEquals(2, g.heuristic(1, new Vec3(1, 0, 1)), 0.00000000001);
        assertEquals(300, g.heuristic(0, new Vec3(11, 10, 11)), 0.00000000001);
    }

    @Test
    public void testPathFinding() {
        TriangleGraph g = new TriangleGraph(
                new TriangleMesh(
                        new NavMeshContainer(
                                new int[]{0, 1, 2, 1, 2, 3, 2, 3, 4},
                                new Vec3[]{
                                        new Vec3(0, 0, 0),
                                        new Vec3(3, 0, 0),
                                        new Vec3(0, 0, 3),
                                        new Vec3(3, 0, 3),
                                        new Vec3(3, 0, 6)
                                }
                        )
                )
        );

        Vec3 from = new Vec3(0, 0, 0);
        Vec3 to = new Vec3(1.9, 0, 5.9);

        assertEquals(0, (int) g.vecToNode(from));
        assertEquals(2, (int) g.vecToNode(to));

        //work with full visibility
        boolean[] bools = new boolean[5];
        Arrays.fill(bools, true);
        Integer[] path = new Astar(g, from, to, bools, new HashSet<>()).getPath();

        assertEquals(3, path.length);
        assertArrayEquals(new Integer[]{2, 1, 0}, path);
    }

    private TriangleGraph advancedGraph() {
        //this graph has 6 nodes 0 - 1
        //                       |   |
        //                       2 - 3 - 4 - 5
        return new TriangleGraph(
                new TriangleMesh(
                        new NavMeshContainer(
                                new int[]{0, 1, 3, 0, 3, 4, 1, 3, 2, 2, 3, 4, 2, 4, 5, 4, 5, 6},
                                new Vec3[]{
                                        new Vec3(0, 0, 0),
                                        new Vec3(1, 0, 1),
                                        new Vec3(2, 0, 2),
                                        new Vec3(3, 0, 3),
                                        new Vec3(4, 0, 4),
                                        new Vec3(5, 0, 5),
                                        new Vec3(6, 0, 6)
                                }
                        )
                )
        );
    }

    @Test
    public void testGetNeighbouringNodesMesh(){
        TriangleGraph g = advancedGraph();
        //test if we get the correct nodes if we only know the vertices 0,1,2,3,5
        boolean[] knownNodes = new boolean[]{true, true, true, true, false, false, true};

        Set<Integer> result = g.getNeighbouringNodesMesh(0, knownNodes, new HashSet<>()).keySet();
        Set<Integer> expected = new HashSet<Integer>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        expected.add(3);

        assertTrue(result.equals(expected));
    }

    @Test
    public void testGetUnexploredNeighbouringNodes(){
        TriangleGraph g = advancedGraph();
        //test if we get the correct nodes if we only know the vertices 0,1,2,3,5
        boolean[] knownNodes = new boolean[]{true, true, true, true, false, false, true};

        HashMap<Integer, Integer> result = g.getUnexploredNeighbouringNodes(0, knownNodes, new HashSet<>());

        //check that only node 4 is found as explorable
        assertTrue(result.containsKey(4));
        assertEquals(1, result.size());
    }

}
