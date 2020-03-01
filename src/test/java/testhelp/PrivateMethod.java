/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package testhelp;

import helperclasses.datastructures.linq.QArrayList;
import org.junit.jupiter.api.Assertions ;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * It is possible to invoke private methods in unit tests. However, doing this for all our tests will decrease the readability of our code.
 * This is is fixed by the PrivateMethod class. How to use:
 * <p>
 * Write your class with a private method:
 * public class A {
 * private Integer calculation(int a, int b) { ... }
 * }
 * <p>
 * Write your test using PrivateMethod
 * public class ATest {
 *
 * @param <ReturnType> Specify the return type of the private method before executing so you will not have to cast it in the end.
 * @author Maurin 12/11/2019
 * @Test public calculationTest(){
 * PrivateMethod<Integer> calculation = new PrivateMethod<Integer>(new A(), "calculation");
 * assertEquals(5, calculation.invoke(2, 3));
 * }
 * }
 */
public class PrivateMethod<ReturnType> {

    private Object instance;
    private Method privateMethod;

    public PrivateMethod(Object instance, String methodName) {
        this.instance = instance;
        try {
            // Find the private method and set its accessibility to true
            this.privateMethod = new QArrayList<>(
                    this.instance.getClass().getDeclaredMethods())
                    .where(m -> m.getName().equals(methodName))
                    .first();
            privateMethod.setAccessible(true);

        } catch (IndexOutOfBoundsException e) {
            // if first(); throws an exception
        	Assertions.fail("Method " + methodName + " could not be found! Please use the correct name of the method.");
        }
    }

    public ReturnType invoke(Object... params) {
        ReturnType result;
        try {
            result = (ReturnType) privateMethod.invoke(this.instance, params);
            return result;
        } catch (ClassCastException e) {
        	Assertions.fail("Method " + this.privateMethod.getName() + " does not return an object of this ReturnType! Please make sure you are using the correct generic type or method.");
        } catch (InvocationTargetException e) {
        	Assertions.fail("Method " + this.privateMethod.getName() + " could not be invoked! Please use the correct amount and instances of parameters.");
        } catch (IllegalAccessException e) {
        	Assertions.fail("Method " + this.privateMethod.getName() + " could not be accessed! Please set the setAccessible to public in the test.");
        }
        return null;
    }
}
