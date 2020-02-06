/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.quadtree;

import helperclasses.datastructures.linq.QArrayList;
import helperclasses.datastructures.mesh.Edge;

/**
 * Small setup for a QuadTree (needs a story)
 */
public abstract class QuadTree<T> {

    private QArrayList<QuadTree<T>> branches;

    public QuadTree() {
    }

    public boolean contains(T obj) {
        return branches.anyTrue(branch -> branch.contains(obj));
    }
}

class Leaf<T> extends QuadTree<T> {

    private T obj;
    private Edge[] edges;

    public Leaf(T obj) {
        this.obj = obj;
    }

    @Override
    public boolean contains(T obj) {
        return this.obj.equals(obj);
    }
}

