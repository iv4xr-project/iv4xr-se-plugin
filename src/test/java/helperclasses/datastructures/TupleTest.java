/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures;

import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;

/*
This class holds all unit tests of Tuple
*/
public class TupleTest {
    /*
    check if the defined objects are stored
     */
    @Test
    public void checkStorage() {
        //create tuple
        Tuple<Integer, Boolean> t = new Tuple<>(1, true);

        //check if the values are stored
        Assertions.assertEquals(Integer.valueOf(1), t.object1);
        Assertions.assertEquals(true, t.object2);
    }
}
