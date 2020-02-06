/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.functions;

/**
 * Example usage:
 * Function3<String, Integer, Vec3, String> func = (text, value, vector) -> text + value + ": " + vector.toString();
 */
@FunctionalInterface
public interface Function3<A, B, C, D> {
    D apply(A a, B b, C c);
}