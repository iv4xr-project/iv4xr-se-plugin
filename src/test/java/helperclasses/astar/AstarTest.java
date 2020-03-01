/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.astar;

import helperclasses.datastructures.Vec3;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;
import java.util.HashSet;

/**
 * This class will hold all unit tests for our A* implementation
 */
public class AstarTest {
    /**
     * Test for a correct path in ascending order
     */
    @Test
    public void checkPathAscending() {
        //use our mock graph to test
        TestGraph graph = new TestGraph();

        //start the pathfinding from node 1 to node 9
        Astar astar = new Astar(graph, new Vec3(1, 1, 1), new Vec3(9.5, 9.5, 9.5), new boolean[]{true}, new HashSet<>());

        //get the path
        Integer[] path = astar.getPath();

        //check if we have a path
        assertNotNull(path);

        //check if we have the correct path
        assertEquals(9, path[0].intValue());
        assertEquals(8, path[1].intValue());
        assertEquals(3, path[2].intValue());
        assertEquals(2, path[3].intValue());
        assertEquals(1, path[4].intValue());
    }

    /**
     * Test for a correct path in descending order
     */
    @Test
    public void checkPathDescending() {
        //use our mock graph to test
        TestGraph graph = new TestGraph();

        //start the pathfinding from node 9 to node 1
        Astar astar = new Astar(graph, new Vec3(9.5, 9.5, 9.5), new Vec3(1, 1, 1), new boolean[]{}, new HashSet<>());

        //get the path
        Integer[] path = astar.getPath();

        //check if we have a path
        assertNotNull(path);

        //check if we have the correct path
        assertEquals(1, path[0].intValue());
        assertEquals(2, path[1].intValue());
        assertEquals(3, path[2].intValue());
        assertEquals(8, path[3].intValue());
        assertEquals(9, path[4].intValue());
    }

    /**
     * Check for a path to an impossible node
     */
    @Test
    public void checkImpossibleTo() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
        		//use our mock graph to test
    	        TestGraph graph = new TestGraph();
    	        //start the pathfinding from node impossible to node 9
    	        Astar astar = new Astar(graph, new Vec3(-10, -10, -10), new Vec3(1, 1, 1), new boolean[]{}, new HashSet<>());
    	        //get the path
    	        Integer[] path = astar.getPath();			
    		}
    	) ;    
    }

    /**
     * Check for a path from an impossible node
     */
    @Test
    public void checkImpossibleFrom() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
	            //use our mock graph to test
	            TestGraph graph = new TestGraph();
	            //start the pathfinding from node 1 to node impossible
	            Astar astar = new Astar(graph, new Vec3(1, 1, 1), new Vec3(-10, -10, -10), new boolean[]{}, new HashSet<>());
	            //get the path
	            Integer[] path = astar.getPath();
    		} 
    	) ;
    }

    /**
     * Check if A* makes use of heuristics to find its path. In the test graph 7-2 is blocked by weight, which
     * would result in an inefficient path if heuristics aren't used
     */
    @Test
    public void checkHeuristicUsage() {
        //use our mock graph to test
        TestGraph graph = new TestGraph();

        //start the pathfinding from node 7 to node 2
        Astar astar = new Astar(graph, new Vec3(7, 7, 7), new Vec3(2.5, 2.5, 2.5), new boolean[]{}, new HashSet<>());

        //get the path
        Integer[] path = astar.getPath();

        //check if we have a path
        assertNotNull(path);

        //check if we have the correct path avoiding the 7-2 link
        assertEquals(2, path[0].intValue());
        assertEquals(1, path[1].intValue());
        assertEquals(6, path[2].intValue());
        assertEquals(7, path[3].intValue());
    }
}
