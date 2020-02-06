/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.functions;

/**
 * Example usage:
 * Function2<String, Integer, String> func = (text, value) -> text + value;
 */
@FunctionalInterface
public interface Function2<A, B, C> {
    C apply(A a, B b);
}

