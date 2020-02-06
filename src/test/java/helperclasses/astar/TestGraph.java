/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.astar;

import helperclasses.datastructures.Vec3;

import java.util.HashSet;

/**
 * This class implements a graph which we can use for our unit tests of A*. The graph looks like:
 * 1-2-3-4-5
 * | | |   |
 * 6-7 8-9-10
 * This test graph will use full visibility of all nodes
 */

public class TestGraph extends Graph {
    /**
     * Use weights of 1 for each connection except 7-2 for testing cases
     */
    @Override
    public Double weightOfConnection(int idFrom, int idTo) {
        if (idFrom == 7 && idTo == 2) {
            return Double.valueOf(100);
        }
        return Double.valueOf(1);
    }

    /**
     * Implement the graph in the set of neighbours
     */
    @Override
    public int[] getNeighbours(int id) {
        switch (id) {
            case 1:
            case 7:
                return new int[]{2, 6};
            case 2:
                return new int[]{1, 3, 7};
            case 3:
                return new int[]{2, 4, 8};
            case 4:
                return new int[]{3, 5};
            case 5:
                return new int[]{4, 10};
            case 6:
                return new int[]{1, 7};
            case 8:
                return new int[]{3, 9};
            case 9:
                return new int[]{8, 10};
            case 10:
                return new int[]{5, 9};
            default:
                return null;
        }
    }

    /**
     * Implement the graph in the set of neighbours
     */
    @Override
    public int[] getKnownNeighbours(int id, boolean[] knownVertices, HashSet<Integer> blockedNodes) {
        switch (id) {
            case 1:
            case 7:
                return new int[]{2, 6};
            case 2:
                return new int[]{1, 3, 7};
            case 3:
                return new int[]{2, 4, 8};
            case 4:
                return new int[]{3, 5};
            case 5:
                return new int[]{4, 10};
            case 6:
                return new int[]{1, 7};
            case 8:
                return new int[]{3, 9};
            case 9:
                return new int[]{8, 10};
            case 10:
                return new int[]{5, 9};
            default:
                return null;
        }
    }

    /**
     * Always return a vec3 consisting of its own id
     */
    @Override
    public Vec3 toVec3(int id) {
        //check if id exists
        if (id >= 0 && id <= 10) {
            return new Vec3(id, id, id);
        } else {
            //if a unavailable id exists throw a exception
            throw new IllegalArgumentException();
        }
    }

    /**
     * Implement the node search by vec3
     */
    @Override
    public Integer vecToNode(Vec3 position) {
        //get the node id by flooring the x coordinate of the vec3
        int id = (int) position.x;
        //check if this results in a viable id
        if (id >= 0 && id <= 10) {
            return id;
        } else {
            return null;
        }
    }


}
