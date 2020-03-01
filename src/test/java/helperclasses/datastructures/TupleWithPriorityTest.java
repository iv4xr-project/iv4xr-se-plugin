/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures;

import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;

/*
This class holds all unit tests of TupleWithPriorityTest
 */
public class TupleWithPriorityTest {
    /*
    check if the defined objects are stored
    */
    @Test
    public void checkStorage() {
        //create tuple
        TupleWithPriority<Boolean> t = new TupleWithPriority<Boolean>(true, 2);

        //check if the values are stored
        assertEquals(true, t.object);
        assertEquals(2, t.priority, 0.001);
    }

    /*
    check if the ordering between tuples is correct
    */
    @Test
    public void checkPriority() {
        //create tuple
        TupleWithPriority<Boolean> t1 = new TupleWithPriority<Boolean>(true, 2);
        TupleWithPriority<Boolean> t2 = new TupleWithPriority<Boolean>(true, 3);

        //check if the compare to function gives the correct order
        assertEquals(-1, t1.compareTo(t2));
        assertEquals(1, t2.compareTo(t1));
    }
}
