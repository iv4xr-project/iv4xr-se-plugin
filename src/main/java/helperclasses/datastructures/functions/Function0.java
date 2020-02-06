/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.functions;

/**
 * Example usage:
 * Function0<int> func = () -> 5;
 */
@FunctionalInterface
public interface Function0<A> {
    A apply();
}
