/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.functions;

import helperclasses.datastructures.linq.QArrayList;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;

public class FunctionTest {

    /**
     * Test a lambda function that takes no arguments
     */
    @Test
    public void testFunction0Constant() {
        // function always returns 5
        Function0 function1 = () -> 5;

        // applying the function should result in 5
        assertEquals(5, function1.apply());
    }

    /**
     * Test a lambda function that takes no arguments
     */
    @Test
    public void testFunction0ListCreation() {

        // function creates a new QArrayList
        Function0<QArrayList<Integer>> function1 = QArrayList::new;

        // applying the function multiple times should result in different instances of an object
        var list1 = function1.apply();
        var list2 = function1.apply();
        list1.add(4);

        assertEquals(1, list1.size());
        assertEquals(0, list2.size());
    }

    /**
     * Test a lambda function that takes 2 arguments
     */
    @Test
    public void testFunction2Formula() {

        Function2<Integer, Integer, Integer> formula = (a, b) -> a + b + b;

        // applying the function should result the a+b+b
        for (int a = 0; a < 5; a++) {
            for (int b = 0; b < 5; b++) {
                assertEquals(a + 2 * b, (int) formula.apply(a, b));
            }
        }
    }

    /**
     * Test a lambda function that takes 2 arguments
     */
    @Test
    public void testFunction2Tuple() {

        // function that takes a string and a number and attaches the string to the number doubled
        Function2<String, Integer, String> sum = (text, number) -> text + (number * 2);

        // applying the function should result the correct answer
        for (int a = 0; a < 5; a++) {
            String text = String.valueOf(a);
            for (int b = 0; b < 5; b++) {
                assertEquals("" + a + (2 * b), sum.apply(text, b));
            }
        }
    }

    /**
     * Test a lambda function that takes 3 arguments
     */
    @Test
    public void testFunction3Tuple() {

        // mathematical function that takes 3 arguments
        Function3<Integer, Integer, Integer, Double> secretFormula = (x, y, z) -> (Math.sqrt(x) + (Math.PI * y)) / z;
        assertEquals((Math.sqrt(7) + (Math.PI * 6)) / 5, secretFormula.apply(7, 6, 5), 0.0000000001);
    }

}