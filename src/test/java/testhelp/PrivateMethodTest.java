/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package testhelp;

import org.junit.jupiter.api.Assertions ;
import org.junit.jupiter.api.Test;

// a class with a private method 'sum'
class A {
    private Integer sum(int a, int b) {
        return a + b;
    }
}

public class PrivateMethodTest {

    @Test
    public void testUsage() {
        PrivateMethod<Integer> privateSum = new PrivateMethod<>(new A(), "sum");
        Assertions.assertEquals(5, (int) privateSum.invoke(2, 3));
    }
}
