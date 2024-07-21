package uuspaceagent.AplibCopy;

public class ObstacleCopy<T> {
    public T obstacle;
    public Boolean isBlocking = false;

    /**
     * Wrap the given o as an Obstacle, marked initially as non-blocking.
     */
    public ObstacleCopy(T obstacle) {
        this.obstacle = obstacle;
    }
}