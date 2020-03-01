/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.linq;

import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class QArrayListTest {

    /**
     * Helper function: Since there are only functions used from superclass ArrayList in this method, we can assume that this method is correct
     *
     * @param m The amount of numbers in the list
     * @return If m is 5 the list will look like: [0, 1, 2, 3, 4]
     */
    private QArrayList<Integer> TestList(int m) {
        QArrayList<Integer> list = new QArrayList<Integer>();
        for (int i = 0; i < m; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * Basic QArray constructor test
     */
    @Test
    public void testConstructorInstance() {
        QArrayList<Integer> list = new QArrayList<Integer>();
        assertEquals(0, list.size());
        list.add(0);
        assertEquals(1, list.size());
    }

    /**
     * Basic QArray constructor test from list input
     */
    @Test
    public void testConstructorFromInput() {

        int amount = 10;

        // create a normal array list
        ArrayList<Integer> original = new ArrayList<Integer>();
        for (int i = 0; i < amount; i++) {
            original.add(i);
        }

        // create a qArrayList by importing the original list
        QArrayList<Integer> qList = new QArrayList<Integer>(original);

        // all values should be equal
        for (int i = 0; i < amount; i++) {
            assertEquals(original.get(i), qList.get(i));
        }
    }

    /**
     * Test the select function using simple mathematical map functions
     * After mapping the list, the newly mapped list should be a copy
     */
    @Test
    public void testSelectCopy() {

        int amount = 20;

        QArrayList<Integer> list1 = TestList(amount); // [0, 1, 2, 3, .., 19]
        QArrayList<Integer> list2 = list1.select(i -> i); // actually does nothing

        // after removing an entry in list 2, the first list must be unchanged
        list2.remove(0); // [6, 7, 8, 9, .., 24]
        assertNotEquals(list1.size(), list2.size());

        // a list should return a copy, so the list should still be the same
        assertEquals(amount, list1.size());
        assertEquals(amount - 1, list2.size());
    }

    /**
     * Test the select function using simple mathematical map functions
     * After mapping the list, the new values must be correct
     */
    @Test
    public void testSelectMap() {

        int amount = 20;

        QArrayList<Integer> list1 = TestList(amount); // [0, 1, 2, 3, .., 19]
        QArrayList<Integer> list2 = list1.select(i -> i * 2);

        // a list should return a copy, so the list should still be the same size
        assertEquals(list1.size(), list2.size());

        // check whether the map function executed correctly
        for (int i = 0; i < amount; i++) {
            assertEquals(2 * list1.get(i), (long) list2.get(i));
        }
    }

    /**
     * Test the select function using simple mathematical map functions
     * After mapping an empty list, nothing happened
     */
    @Test
    public void testSelectEmpty() {

        int amount = 20;

        QArrayList<Integer> list1 = new QArrayList<Integer>(); // empty
        QArrayList<Integer> list2 = list1.select(i -> i * 100);

        // list2 should be empty en not throw an exception
        assertEquals(0, list2.size());
    }

    /**
     * Test the filter function using simple mathematical map functions
     * After filtering the list, the new list should be a copy
     */
    @Test
    public void testWhereCopy() {

        int amount = 20;

        QArrayList<Integer> list = TestList(amount);
        QArrayList<Integer> filteredList = list.where(i -> i >= (amount / 2));

        // a list should return a copy, so the list should still be the same
        assertEquals(amount, list.size());
        assertEquals(amount / 2, filteredList.size());
    }

    /**
     * Test the correctness of the filter function
     */
    @Test
    public void testWhereFiltered() {

        int amount = 20;

        QArrayList<Integer> list1 = TestList(amount);
        QArrayList<Integer> list2 = list1.where(i -> i % 2 == 0); // all even numbers!

        // list should be half the size
        assertEquals((int) (list1.size() / 2), list2.size());

        // check values
        for (int i = 0; i < amount; i++) {
            if (list1.get(i) % 2 == 0)
                assertTrue(list2.contains(list1.get(i)));
            else
                assertFalse(list2.contains(list1.get(i)));
        }
    }

    /**
     * Test the correctness of the allTrue function by running multiple queries on the list
     */
    @Test
    public void testAllTrue() {

        QArrayList<Integer> list = TestList(20);

        assertTrue(list.allTrue(i -> i >= 0));
        assertTrue(list.allTrue(i -> i < 20));
        // combined
        assertTrue(list.allTrue(i -> i >= 0 && i < 20));


        // insert random high number
        list.add(3, 12312);

        assertTrue(list.allTrue(i -> i >= 0)); // should still be true
        assertFalse(list.allTrue(i -> i < 20));
        // combined
        assertFalse(list.allTrue(i -> i >= 0 && i < 20));
    }

    /**
     * Test the correctness of the anyTrue function by running multiple queries on the list
     */
    @Test
    public void testAnyTrue() {

        QArrayList<Integer> list = TestList(20);

        assertTrue(list.anyTrue(i -> i >= 0));
        assertTrue(list.anyTrue(i -> i < 10));
        // combined
        assertTrue(list.anyTrue(i -> i >= 0 && i < 10));

        // insert random high number
        list.add(3, 12312);

        assertTrue(list.anyTrue(i -> i >= 1000)); // should still be true
        assertTrue(list.anyTrue(i -> i < 20000000));
        // specific values
        assertFalse(list.anyTrue(i -> i == 2321));
        assertTrue(list.anyTrue(i -> i == 12312));
    }

    /**
     * Test the max function using a mathematical map function
     */
    @Test
    public void testMaxNumber() {

        QArrayList<Integer> list = TestList(20);
        assertEquals(19, (int) list.max(i -> i));
        assertEquals(4, (int) list.max(i -> i % 5));
    }

    /**
     * Test the max function using a property like map function
     */
    @Test
    public void testMaxProperty() {

        QArrayList<String> list = new QArrayList<String>();
        list.add("AAA");
        list.add("BBBBBBB"); // length of 7
        list.add("CC");
        // "BBBBBBB" is the longest
        assertEquals(7, (int) list.max(s -> s.length()));
    }

    /**
     * Test the min function using a mathematical map function
     */
    @Test
    public void testMinNumber() {

        QArrayList<Integer> list = TestList(20);
        assertEquals(0, (long) list.min(i -> i));
        assertEquals(-19, (long) list.min(i -> -i)); // minimum number is at the end of the list
    }

    /**
     * Test the min function using a property like map function
     */
    @Test
    public void testMinProperty() {

        QArrayList<String> list = new QArrayList<String>();
        list.add("AAA");
        list.add("BBBBBBB"); // length of 7
        list.add("CC");
        // "CC" is the shortest
        assertEquals(2, (int) list.min(s -> s.length()));
    }

    /**
     * Test the last function by query and use of the orderBy function and insert/add functions
     */
    @Test
    public void testLast() {

        QArrayList<Integer> list = TestList(20);
        assertEquals(19, (int) list.last()); // last number is 19
        assertEquals(0, (int) list.orderBy(i -> -i).last()); // when sorted the other way 0 should be the last value
        list.add(777);
        assertEquals(777, (int) list.last()); // add a number at the end of the list
        list.set(0, 888);
        assertEquals(777, (int) list.last()); // should still be 777 after inserting at the start
    }

    /**
     * Test the last function by query and use of the orderBy function and insert/add functions
     */
    @Test
    public void testFirst() {

        QArrayList<Integer> list = TestList(20);
        assertEquals(0, (int) list.first());
        assertEquals(19, (int) list.orderBy(i -> -i).first());

        list.set(0, 777);
        assertEquals(777, (int) list.first()); // add a number at the beginning of the list
        list.add(888);
        assertEquals(777, (int) list.first()); // should still be 777 after adding at the end of the list
    }

    /**
     * Test the contains function by doing a lot of queries on boundary values
     */
    @Test
    public void testContains() {

        QArrayList<Integer> list = TestList(20);

        assertTrue(list.contains(i -> i > 5));
        assertFalse(list.contains(i -> i > 30));
        assertFalse(list.contains(i -> i == 20));
        assertTrue(list.contains(i -> i != 237123));
        assertFalse(list.contains(i -> i == -1));
        assertTrue(list.contains(i -> true));
        assertFalse(list.contains(i -> false));
    }

    /**
     * Test the orderBy function, the new list should be a copy of the original list
     */
    @Test
    public void testOrderByCopy() {
        QArrayList<Integer> list = new QArrayList<Integer>();
        list.add(3);
        list.add(29);
        list.add(-2);
        list.add(8);

        QArrayList<Integer> sorted = list.orderBy(i -> i); // sort on its own value (works as a sort)

        assertEquals(list.size(), sorted.size());

        sorted.add(1);
        assertNotEquals(list.size(), sorted.size());

    }

    /**
     * Test the orderBy function by sorting the list on numerical value
     */
    @Test
    public void testOrderByCorrect1() {

        QArrayList<Integer> list = new QArrayList<Integer>();
        list.add(3);
        list.add(29);
        list.add(-2);
        list.add(8);

        QArrayList<Integer> sorted = list.orderBy(i -> i); // sort on its own value (works as a sort)

        // sorted list is totally different as the original
        for (int i = 0; i < 4; i++) {
            assertNotEquals(list.get(i), sorted.get(i));
        }

        // list is actually sorted by number
        for (int i = 0; i < 3; i++) {
            assertTrue(sorted.get(i) < sorted.get(i + 1));
        }
    }

    /**
     * Test the orderBy function by using a Mod function, which tests whether the mapping function is working as expected
     */
    @Test
    public void testOrderByCorrect2() {

        QArrayList<Integer> list = new QArrayList<Integer>();
        list.add(3);
        list.add(29);
        list.add(-2);
        list.add(8);

        QArrayList<Integer> sorted = list.orderBy(i -> Math.floorMod(i, 7));

        // this should be the order
        int[] answer = new int[]{29, 8, 3, -2};
        for (int i = 0; i < 4; i++) {
            assertEquals(answer[i], (int) sorted.get(i));
        }
    }

    /**
     * Test the Reversed function by reversing a list and checking each value
     */
    @Test
    public void testReversed() {

        for (int test = 1; test < 20; test++) {
            QArrayList<Integer> list = TestList(test); // [0, 1, 2, 3, 4]
            list = list.reversed();
            for (int i = 0; i < test; i++) {
                assertEquals(test - 1 - i, (int) list.get(i));
            }
        }

    }

    /**
     * Test the Reversed function by reversing an empty list
     */
    @Test
    public void testReversedEmpty() {

        QArrayList<Integer> list = new QArrayList<Integer>(); // []
        list = list.reversed();
        assertEquals(0, list.size());
    }

    /**
     * Foldl as well as Foldr should return the sum of a list correctly
     */
    @Test
    public void testFoldlFoldrSum() {
        for (int test = 1; test < 20; test++) { // run the test 20 times

            QArrayList<Integer> list = TestList(test); // [0, 1, 2, 3, 4]
            int expected = (((test - 1) * test) / 2); // (n*(n-1) / 2) should be the sum
            assertEquals(expected, (int) list.foldl((a, b) -> a + b, 0)); // left
            assertEquals(expected, (int) list.foldr((a, b) -> a + b, 0)); // right
        }
    }

    /**
     * Test the Foldl and Foldr function by subtracting the list, foldl and foldr should each give different answers
     */
    @Test
    public void testFoldlFoldrSubtract() {
        QArrayList<Integer> list = TestList(5); // [0, 1, 2, 3, 4]

        // (((((0 - 0) - 1) - 2) - 3) - 4)
        // ((((0 - 1) - 2) - 3) - 4)
        // (((-1 - 2) - 3) - 4)
        // ((-3 - 3) - 4)
        // (-6 - 4)
        // -10
        assertEquals(-10, (int) list.foldl((a, b) -> a - b, 0)); // left

        // (0 - (1 - (2 - (3 - (4-0)))))
        // (0 - (1 - (2 - (3 - 4))))
        // (0 - (1 - (2 - -1)))
        // (0 - (1 - 3))
        // (0 - -2)
        // 2
        assertEquals(2, (int) list.foldr((a, b) -> a - b, 0)); // right
    }

}