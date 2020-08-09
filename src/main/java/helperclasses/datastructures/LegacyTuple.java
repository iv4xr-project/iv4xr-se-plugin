package helperclasses.datastructures;

/**
 * Representing a pair. We keep it to maintain compatibility with serialization/deserialization
 * towards the Lar Recruits (when we send data back and forth). 
 * 
 */
public class LegacyTuple<T1,T2> {
	public T1 object1;
    public T2 object2;

    /**
     * @param object1 Any type that implements Equal
     * @param object2 Any type that implements Equal
     */
    public LegacyTuple(T1 object1, T2 object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    /**
     * Override the equals method.
     * Equals should only be dependent on the variables that are stored in the Tuple.
     *
     * @param o The other object.
     * @return Boolean whether the Tuples are equal.
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof LegacyTuple<?, ?>) // o is a Tuple
                && ((LegacyTuple<?, ?>) o).object1.equals(this.object1) // first object is equal
                && ((LegacyTuple<?, ?>) o).object2.equals(this.object2); // second object is equal
    }

    /**
     * Override the hash function
     * hash(tuple(1,2)) should not be equal to hash(tuple(2,1)), so the second object is multiplied by 2.
     *
     * @return hashCode of the Tuple
     */
    @Override
    public int hashCode() {
        // use doubles so we do not cause an overflow
        return (int) ((double) object1.hashCode() + 2.0 * (double) object2.hashCode());
    }

    /**
     * @return string representation
     */
    @Override
    public String toString() {
        // use doubles so we do not cause an overflow
        return String.format("<%s,%s>", object1, object2);
    }
}
