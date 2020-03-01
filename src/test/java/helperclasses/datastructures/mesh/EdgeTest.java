/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

public class EdgeTest {

    /**
     * Test the constructor by checking whether the vertex indices are sorted
     */
    @Test
    public void testConstruct0() {

        Edge[] edges = new Edge[]{
                new Edge(0, 1),
                new Edge(66, 65645),
                new Edge(4, 3),
                new Edge(22, 3)
        };

        // first vertex should be lower than the second
        for (Edge e : edges)
            assertTrue(e.vertices[0] < e.vertices[1]);
    }

    /**
     * Test the constructor by checking whether the vertex indices are stored correctly
     */
    @Test
    public void testConstruct1() {
        Edge e = new Edge(66, 65645);
        assertEquals(66, e.vertices[0]);
        assertEquals(65645, e.vertices[1]);
    }

    /**
     * Test the constructor by passing illegal arguments in the constructor
     */
    @Test
    public void testConstructExceptions() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
    			// cannot create edge with the same vertex indices
    	        Edge e = new Edge(1, 1);
    		} 
    	) ;
        
        /**
         org.junit.Assert.assertThrows(IllegalArgumentException.class, () -> new Edge(1, 1));
         assertThrows(java.lang.IllegalArgumentException.class, () -> new Edge(1, 1));
         // cannot create edge with negative indices
         assertThrows(IllegalArgumentException.class, () -> new Edge(-1, 1));
         // cannot create edge with negative indices
         assertThrows(IllegalArgumentException.class, () -> new Edge(0, -4));
         **/
    }

    /**
     * Test the equals function
     */
    @Test
    public void testEquals() {

        // Create 3 edges, where e0 and e2 are considered the same
        Edge e0 = new Edge(0, 1);
        Edge e1 = new Edge(0, 2);
        Edge e2 = new Edge(1, 0);

        // reflection
        assertEquals(e0, e0);
        assertEquals(e1, e1);
        assertEquals(e2, e2);

        // equals
        assertNotEquals(e0, e1);
        assertEquals(e0, e2); // should be the same
        assertNotEquals(e1, e2);
    }

    /**
     * Test the hashCode function
     */
    @Test
    public void testHashCode() {

        // Create 3 edges, where e0 and e2 are considered the same
        Edge e0 = new Edge(0, 1);
        Edge e1 = new Edge(0, 2);
        Edge e2 = new Edge(1, 0);

        // equals
        assertNotEquals(e0.hashCode(), e1.hashCode());
        assertEquals(e0.hashCode(), e2.hashCode()); // should be the same
        assertNotEquals(e1.hashCode(), e2.hashCode());

        HashMap<Edge, Integer> hashmap = new HashMap<>();

        hashmap.put(e0, 0);
        assertTrue(hashmap.containsKey(e2)); // e2 should be already in the hashmap
        assertFalse(hashmap.containsKey(e1)); // e1 should not be present in the hashmap
    }

    /**
     * Test the toString function
     */
    @Test
    public void testString() {

        // Create 3 edges, where e0 and e2 are considered the same
        Edge e0 = new Edge(0, 1);
        Edge e1 = new Edge(3, 2);
        Edge e2 = new Edge(1, 0);
        Edge e3 = new Edge(123, 2342531);

        // equals
        assertEquals("Edge<0,1>", e0.toString());
        assertEquals("Edge<0,1>", e2.toString()); // should look the same as e1
        assertEquals("Edge<2,3>", e1.toString());
        assertEquals("Edge<123,2342531>", e3.toString());
    }

}