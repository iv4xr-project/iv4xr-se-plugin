package uuspaceagent.exploration;

import eu.iv4xr.framework.extensions.pathfinding.*;
import eu.iv4xr.framework.spatial.Vec3;
import uuspaceagent.Timer;

import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Except for the addition of exploration, the same as @AStar.java
  */
public class AStarExplore<NodeId> implements PathExplorer<NodeId> {

    public enum SearchMode { GREEDY, DIJKSTRA, HEURISTIC } ;

    public AStar.SearchMode searchMode = AStar.SearchMode.HEURISTIC ;

    /**
     * When this is set, this function will be use to calculate the heuristic distance between
     * two nodes, rather than using the default heuristic-method supplied by underlying navigation
     * graph.
     */
    public Function<Explorable<NodeId>, BiFunction<NodeId,NodeId,Float>> dynamicHeuristicDistance = null ;
    public Function<Explorable<NodeId>, BiFunction<NodeId,Vec3,Float>> dynamicHeuristicDistance2 = null ;

    float getHeuristicDistance(Explorable<NodeId> graph, NodeId a, NodeId b) {
        if(dynamicHeuristicDistance == null) return graph.heuristic(a, b) ;
        return dynamicHeuristicDistance.apply(graph).apply(a, b) ;
    }
    float getHeuristicDistance(Explorable<NodeId> graph, NodeId a, Vec3 b) {
        if(dynamicHeuristicDistance2 == null) return graph.heuristic(a, b) ;
        return dynamicHeuristicDistance2.apply(graph).apply(a, b) ;
    }

    @Override
    public ArrayList<NodeId> findPath(Explorable<NodeId> graph, NodeId start, NodeId goal) {
        Timer.pathfindingStart = Instant.now();
        PriorityQueue<Priotisable<NodeId>> open = new PriorityQueue<>(10, new PriotisableComperator<>());

        // Closed nodes with their associated measured distance
        HashMap<NodeId, Float> closed = new HashMap<NodeId, Float>();
        // Closed nodes with the shortest node towards it.
        HashMap<NodeId, NodeId> paths = new HashMap<NodeId, NodeId>();

        // The fDistance of the starting node does not matter actually, since it will be immediately
        // popped from the open-set anyway.
        float fDistance_ofStart = getHeuristicDistance(graph,start,goal) ;
        if(searchMode == AStar.SearchMode.DIJKSTRA) fDistance_ofStart = 0 ;
        open.add(new Priotisable<>(start, fDistance_ofStart));
        closed.put(start, 0f);

        while (!open.isEmpty()) {
            // remove the node with the lowest "priority" from the open-list.
            NodeId current = open.remove().item ;

            // Check if goal is reached, the search stops, and the path to it is returned.
            // In particular note that we don't search further e.g. to find a better path.
            if (current.equals(goal)) {
                // Reconstruct path backwards
                var path = new ArrayList<NodeId>();
                path.add(goal);
                while (! current.equals(start)) {
                    current = paths.get(current);
                    path.add(current);
                }
                // Reverse path to get correct direction
                Collections.reverse(path);
                Timer.endPathfinding();
                return path;
            }

            var distToCurrent = closed.get(current);

            for (NodeId next : graph.neighbours(current)) {

                float dn = graph.distance(current, next) ;
                if (dn == Float.POSITIVE_INFINITY)
                    continue;

                // System.out.println("## current:" + current + ", next: " + next) ;

                // The distance from start to next
                var distToNext = distToCurrent + dn ;

                // Guard for negative distances
                if (distToNext < 0)
                    distToNext = 0;

                // The distance from next to goal
                var heurFromNext = getHeuristicDistance(graph,next,goal);

                if (!closed.containsKey(next)) {
                    // Unexplored node
                    closed.put(next, distToNext);
                } else if (distToNext < closed.get(next)) {
                    // Already explored, but shorter route
                    closed.replace(next, distToNext);
                } else {
                    // Already explored, but longer route
                    continue;
                }
                ;

                if (Float.isInfinite(distToNext))
                    continue;

                paths.put(next, current);
                float fDistance ;
                switch(searchMode) {
                    case DIJKSTRA : fDistance = distToNext ; break ;
                    case GREEDY   : fDistance = heurFromNext ; break ;
                    default : /* heuristic-mode */ fDistance = distToNext + heurFromNext ;
                }

                if (open.stream().noneMatch(p -> p.item.equals(next))
                        || open.removeIf(p -> p.item.equals(next) && p.priority > fDistance)) {
                    // If not in open set, or already in open set with longer distance...
                    // put next neighbour in the open set
                    open.add(new Priotisable<>(next, fDistance));
                }
            }
        }

        Timer.endPathfinding();
        return null;
    }

    @Override
    public ArrayList<NodeId> explore(Explorable<NodeId> graph, NodeId start) {
        Timer.exploreStart = Instant.now();

        PriorityQueue<Priotisable<NodeId>> open = new PriorityQueue<>(10, new PriotisableComperator<>());
        // Closed nodes with their associated measured distance
        HashMap<NodeId, Float> closed = new HashMap<>();
        // Closed nodes with the shortest node towards it.
        HashMap<NodeId, NodeId> paths = new HashMap<>();

        // The fDistance of the starting node does not matter actually, since it will be immediately
        // popped from the open-set anyway.
        float fDistance_ofStart = 0 ;
        open.add(new Priotisable<>(start, fDistance_ofStart));
        closed.put(start, 0f);

        while (!open.isEmpty()) {
            // remove the node with the lowest "priority" from the open-list.
            NodeId current = open.remove().item ;

            var distToCurrent = closed.get(current);

            for (NodeId next : graph.neighbours_explore(current)) {

                // Check if an unknown node is reached, the search stops, and the path to it is returned.
                // In particular note that we don't search further e.g. to find a better path.
                if (graph.isUnknown(next) && current != start) {
                    // Reconstruct path backwards
                    var path = new ArrayList<NodeId>();
                    // current node is unknown, so we create a new one (if necessary) next to it, in case it is a wall.
                    NodeId newNode = graph.phantom_neighbour(next, current);
                    if (newNode != current) path.add(newNode);
                    while (! current.equals(start)) {
                        path.add(current);
                        current = paths.get(current);
                    }
                    // Reverse path to get correct direction
                    Collections.reverse(path);
                    Timer.endExplore();
                    return path;
                }

                float dn = graph.distance(current, next) ;
                if (dn == Float.POSITIVE_INFINITY)
                    continue;

                // System.out.println("## current:" + current + ", next: " + next) ;

                // The distance from start to next
                var distToNext = distToCurrent + dn ;

                // Guard for negative distances
                if (distToNext < 0)
                    distToNext = 0;

                if (!closed.containsKey(next)) {
                    // Unexplored node
                    closed.put(next, distToNext);
                } else if (distToNext < closed.get(next)) {
                    // Already explored, but shorter route
                    closed.replace(next, distToNext);
                } else {
                    // Already explored, but longer route
                    continue;
                }
                ;

                if (Float.isInfinite(distToNext))
                    continue;

                paths.put(next, current);
                float fDistance ;

                // Because there is no heuristic, Dijkstra is always used
                fDistance = distToNext ;

                if (open.stream().noneMatch(p -> p.item.equals(next))
                        || open.removeIf(p -> p.item.equals(next) && p.priority > fDistance)) {
                    // If not in open set, or already in open set with longer distance...
                    // put next neighbour in the open set
                    open.add(new Priotisable<>(next, fDistance));
                }
            }
        }

        Timer.endExplore();
        return null;
    }

    @Override
    public ArrayList<NodeId> exploreTo(Explorable<NodeId> graph, NodeId start, Vec3 goal) {

        PriorityQueue<Priotisable<NodeId>> open = new PriorityQueue<>(10, new PriotisableComperator<>());

        // Closed nodes with their associated measured distance
        HashMap<NodeId, Float> closed = new HashMap<NodeId, Float>();
        // Closed nodes with the shortest node towards it.
        HashMap<NodeId, NodeId> paths = new HashMap<NodeId, NodeId>();

        // The fDistance of the starting node does not matter actually, since it will be immediately
        // popped from the open-set anyway.
        float fDistance_ofStart = getHeuristicDistance(graph,start,goal) ;
        if(searchMode == AStar.SearchMode.DIJKSTRA) fDistance_ofStart = 0 ;
        open.add(new Priotisable<>(start, fDistance_ofStart));
        closed.put(start, 0f);

        if (open.peek() == null) return null;
        float nodeDist = graph.distance(open.peek().item, graph.neighbours_explore(open.peek().item).iterator().next());

        while (!open.isEmpty()) {
            // remove the node with the lowest "priority" from the open-list.
            NodeId current = open.remove().item;

            // Check if goal is reached, the search stops, and the path to it is returned.
            // In particular note that we don't search further e.g. to find a better path.
            if (graph.heuristic(current, goal) < nodeDist) {
                // Reconstruct path backwards
                var path = new ArrayList<NodeId>();
                path.add(current);
                while (! current.equals(start)) {
                    current = paths.get(current);
                    path.add(current);
                }
                // Reverse path to get correct direction
                Collections.reverse(path);
                return path;
            }

            var distToCurrent = closed.get(current);

            for (NodeId next : graph.neighbours_explore(current)) {
                // Check if an unknown node is reached, the search stops, and the path to it is returned.
                // In particular note that we don't search further e.g. to find a better path.
                if (graph.isUnknown(next) && current != start) {
                    // Reconstruct path backwards
                    var path = new ArrayList<NodeId>();
                    // current node is unknown, so we create a new one (if necessary) next to it, in case it is a wall.
                    NodeId newNode = graph.phantom_neighbour(next, current);
                    if (newNode != current) path.add(newNode);
                    while (! current.equals(start)) {
                        path.add(current);
                        current = paths.get(current);
                    }
                    // Reverse path to get correct direction
                    Collections.reverse(path);
                    return path;
                }

                float dn = graph.distance(current, next) ;
                if (dn == Float.POSITIVE_INFINITY)
                    continue;

                // System.out.println("## current:" + current + ", next: " + next) ;

                // The distance from start to next
                var distToNext = distToCurrent + dn ;

                // Guard for negative distances
                if (distToNext < 0)
                    distToNext = 0;

                // The distance from next to goal
                var heurFromNext = getHeuristicDistance(graph,next,goal);

                if (!closed.containsKey(next)) {
                    // Unexplored node
                    closed.put(next, distToNext);
                } else if (distToNext < closed.get(next)) {
                    // Already explored, but shorter route
                    closed.replace(next, distToNext);
                } else {
                    // Already explored, but longer route
                    continue;
                }
                ;

                if (Float.isInfinite(distToNext))
                    continue;

                paths.put(next, current);
                float fDistance ;
                switch(searchMode) {
                    case DIJKSTRA : fDistance = distToNext ; break ;
                    case GREEDY   : fDistance = heurFromNext ; break ;
                    default : /* heuristic-mode */ fDistance = distToNext + heurFromNext ;
                }

                if (open.stream().noneMatch(p -> p.item.equals(next))
                        || open.removeIf(p -> p.item.equals(next) && p.priority > fDistance)) {
                    // If not in open set, or already in open set with longer distance...
                    // put next neighbour in the open set
                    open.add(new Priotisable<>(next, fDistance));
                }
            }
        }

        return null;
    }
}

/**
 * Wraps around a type to add a float value on which can be sorted.
 *
 * @param <T> The type to wrap around.
 */
class Priotisable<T> {
    public float priority;
    public T item;

    /**
     * Wrap around an item to add a priority on which can be sorted.
     *
     * @param item: The item to wrap around.
     * @param priority: The priority on which can be sorted.
     */
    public Priotisable(T item, float priority) {
        this.item = item;
        this.priority = priority;
    }
}

class PriotisableComperator<T> implements Comparator<Priotisable<T>> {
    @Override
    public int compare(Priotisable<T> o1, Priotisable<T> o2) {
        return Float.compare(o1.priority, o2.priority);
    }
}