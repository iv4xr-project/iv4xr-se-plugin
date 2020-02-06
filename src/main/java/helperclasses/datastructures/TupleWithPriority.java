/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures;

/**
 * This class is used to store a generic object together with its priority into a tuple and implement the
 * Comparable on the priority
 */
public class TupleWithPriority<T> implements Comparable<TupleWithPriority> {

    public T object;
    public double priority;

    /**
     * Constructor to create the tuple
     *
     * @param object   value that is stored in the tuple
     * @param priority attached to the object
     */
    public TupleWithPriority(T object, double priority) {
        this.object = object;
        this.priority = priority;
    }

    /**
     * Override the compare to function to use the priority
     *
     * @param other the other tuple
     * @return comparison based on the priority
     */
    @Override
    public int compareTo(TupleWithPriority other) {
        return Double.compare(this.priority, other.priority);
    }
}
