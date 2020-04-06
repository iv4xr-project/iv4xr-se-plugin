/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import helperclasses.Intersections.EntityNodeIntersection;
import helperclasses.Intersections.PointTriangleIntersection;
import helperclasses.Intersections.TrianglePolygonIntersection;
import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import pathfinding.Pathfinder;

import java.nio.file.Path;
import java.util.*;

/**
 * This class is used  to store the current knowledge of the world and form it into a mental map
 */
public class MentalMap {
    private boolean[] knownVertices;
    private long[] knownVerticesTimestamps ; // containing information on when vert. i becomes known, else it is null
    public Pathfinder pathFinder;

    //store the current path and position on the path
    private Vec3[] path;
    private long pathTimestamp = -1 ;
    private Vec3 goalLocation;
    private int currentWayPoint = -1;

    /**
     * The constructor will take the required input to form an mental map
     *
     * @param p: The pathfinder which stores the pathfinder functions
     */
    public MentalMap(Pathfinder p) {
        pathFinder = p;
        knownVertices = new boolean[pathFinder.navmesh.vertices.length];
        knownVerticesTimestamps = new long[pathFinder.navmesh.vertices.length] ;
    }

    /**
     * Set the path for the agent to follow
     * @param goal: The goal location of the path
     * @param path: The path the agent will follow
     */
    public void applyPath(long timestamp, Vec3 goal, Vec3[] path){
        this.path = path;
        pathTimestamp = timestamp ;
        currentWayPoint = path.length - 1;
        goalLocation = goal;
    }
    
    public long getPathTimestamp() { return pathTimestamp ; }

    /**
     * This method will invoke the pathfinder to try to compute a path to the given
     * goal-position. Only then nodes so-far seen by the agent will be used for 
     * planning the path (in particular, the part of the world which the agent
     * has not seen will be excluded). A list of nodes which should additionally
     * be excluded for path planning, e.g. because they are blocked, can also be
     * added.
     * 
     * @param from: The location of the agent from where to start the path finding
     * @param goal: The position where the agent wants to move to
     * @return The path which is constructed or null if it was impossible to construct the path
     */
    public Vec3[] navigateForce(Vec3 from, Vec3 goal, HashSet<Integer> blockedNodes) {
        try {
            return pathFinder.navigate(from, goal, knownVertices, blockedNodes);
        } catch (IllegalStateException e){
            return null;
        }
    }

    /**
	 * This method will invoke the pathfinder, if the goal is different from the
	 * currently maintained goal. If the goal-position is the same, this method will
	 * return the previously remembered path to the position.
	 * 
	 * If the currently maintained goal-position is different, this method will
	 * invoke {@link navigateForce}. Only then nodes so-far seen by the agent will
	 * be used for planning the path (in particular, the part of the world which the
	 * agent has not seen will be excluded). A list of nodes which should
	 * additionally be excluded for path planning, e.g. because they are blocked,
	 * can also be added.
	 * 
	 * @param from: The location of the agent from where to start the path finding
	 * @param goal: The position where the agent wants to move to
	 * @return The path which is constructed or null if it was impossible to
	 *         construct the path
	 */
    public Vec3[] navigate(Vec3 from, Vec3 goal, HashSet<Integer> blockedNodes) {
        //Check if the goal is different
        if (goalLocation != null) {
            if (goal.distanceSquared(goalLocation) < 0.01) {
                if (path == null) {
                    return null;
                }
                return path;
            }
        }
        return navigateForce(from, goal, blockedNodes);
    }

    /**
     * Get the goal location of the agent
     *
     * @return The current goal location used for the path finding
     */
    public Vec3 getGoalLocation() {
        if(goalLocation == null) return null;
        return new Vec3(goalLocation);
    }
    
    /** 
     * Set the goal location and the path to it to null.
     */
    public void clearGoalLocation() {
    	goalLocation = null ;
    	path = null ;
    	currentWayPoint = -1 ;
    }

    /**
     * Update the current wayPoint if a new wayPoint has been reached
     *
     * @param position: The position of the player
     * @return Whether the waypoint was updated or not
     */
    public boolean updateCurrentWayPoint(Vec3 position) {
        //if the end of the path is reached return

        if(currentWayPoint < 0) {
            goalLocation = null;//disable the goal to signal that the goal is reached
            return false;
        }

        int oldWayPoint = currentWayPoint;

        //if the agent is close to the current wayPoint update to follow the next wayPoint
        double maxDist = 0.5;
        if (position.distance(path[currentWayPoint]) < maxDist) {
            currentWayPoint -= 1;
        }
        return oldWayPoint != currentWayPoint;
    }
    
    public void insertNewWayPoint(Vec3 p) {
    	Vec3[] newPath = new Vec3[path.length + 1] ;
    	for (int k=0; k<=currentWayPoint; k++) newPath[k] = path[k] ;
    	newPath[currentWayPoint+1] = p ;
    	for (int k=currentWayPoint+1; k<path.length; k++) newPath[k+1] = path[k] ;
    	path = newPath ;
    	currentWayPoint++ ;
     }

    /**
     * This method update the seen nodes based on a list of seen nodes
     *
     * @param visibleNodes: The list of nodes which are visible to the agent
     */
    public void updateKnownVertices(long timestamp, int[] visibleNodes) {
        for (int i : visibleNodes) {
        	// only update previously unknown vertex:
        	if (!knownVertices[i]) {
        		knownVertices[i] = true;
                knownVerticesTimestamps[i] = timestamp ;
        	}    
        }
    }

    /**
     * Get the unknown neighbours from the known nodes mesh starting from the position
     * @param from: position to start the mesh from
     * @param blockedNodes: A set of nodes which are blocked and can not be used for pathfinding
     * @return Null if the from location has no results else the unknown neighbours keys with their known neighbours as values
     */
    public HashMap<Integer, Integer> getUnknownNeighbours(Vec3 from, HashSet<Integer> blockedNodes){
        Integer id = pathFinder.graph.vecToNode(from);
        if(id == null) return null;

        return pathFinder.graph.getUnexploredNeighbouringNodes(id, knownVertices, blockedNodes);
    }

    /**
     * Return the position of the closest unknown node
     * @param startPosition: The position from which to look for unknown neighbours
     * @param targetPosition: The position in which direction the agents want to explore
     * @param blockedNodes: A set of nodes which are blocked and can not be used for pathfinding
     * @return The vec3 coordinate of the closest neighbour or null if no neighbour is available
     */
    public Vec3 getUnknownNeighbourClosestTo(Vec3 startPosition, Vec3 targetPosition, HashSet<Integer> blockedNodes){
        HashMap<Integer, Integer> h = getUnknownNeighbours(startPosition, blockedNodes);

        double distToTarget = Double.POSITIVE_INFINITY;
        Integer closestNeighbour = null;
        //loop trough the options to get the best option
        for(int i : h.keySet()){
            double dist = targetPosition.distance(pathFinder.graph.toVec3(i));
            if(dist < distToTarget){
                distToTarget = dist;
                closestNeighbour = i;
            }
        }

        //return null if no neighbours are available
        if(closestNeighbour == null) {
            return null;
        } else {
            return pathFinder.graph.toVec3(h.get(closestNeighbour));
        }
    }

    /**
     * This method will return the next wayPoint for an agent to move to
     *
     * @return Return the Vec3 coordinates of the next way point from the calculated path
     */
    public Vec3 getNextWayPoint() {
        if(currentWayPoint < 0) return path[0];
        return new Vec3(path[currentWayPoint]);
    }

    /**
     * Return a the vertices known to the mental map
     * @return An array of vertices by id known to the agent
     */
    public Integer[] getKnownVerticesById(){
        List<Integer> vertices = new LinkedList<>();
        for(int i =0; i < knownVertices.length; i++) {
            if(knownVertices[i]) vertices.add(i);
        }
        return vertices.toArray(new Integer[vertices.size()]);
    }
    
    public List<Tuple<Integer,Long>> getKnownVertices_withTimestamps() {
    	List<Tuple<Integer,Long>> vertices = new LinkedList<>();
        for(int i =0; i < knownVertices.length; i++) {
            if(knownVertices[i]) {
            	vertices.add( new Tuple(i,knownVerticesTimestamps[i]));
            }
        }
        return vertices ;
    }
    
    /*
    public Vec3 getFurthestCenterPoint_ofLastExploredPolygon(Vec3 agentPosition) {
    	long mostRecentTimestamp = -1 ;
    	for(int k=0; k<knownVertices.length; k++) {
    		if (knownVertices[k]) {
    			mostRecentTimestamp = Math.max(mostRecentTimestamp, knownVerticesTimestamps[k]) ;
    		}
    	}
    	if (mostRecentTimestamp < 0) return null ;
    	Vec3 furthest = null ;
    	for (int k=0;  k<knownVertices.length; k++) {
    		if (knownVertices[k] &&  knownVerticesTimestamps[k]>=mostRecentTimestamp) {
    			var q = pathFinder.graph.toVec3(k) ;
    			if (furthest==null || agentPosition.distance(q) > agentPosition.distance(furthest)) {
    			    furthest = q ;
    			}
    		}
    	}
    	return furthest ;
    }
    */

    public boolean[] getKnownVertices(){ return knownVertices;}
    
    /**
     * Get the navigation polygon (triangle) that contains the given position q. If one exists, the method
     * returns its corners. Else null is returned.
     */
    public Vec3[] getContainingPolygon(Vec3 q) {
    	for(int i = 0; i < pathFinder.navmesh.faces.length; i++){
    		var corners = EntityNodeIntersection.getTriangleVertices(pathFinder.navmesh,i) ;
    		boolean contained = PointTriangleIntersection.isPointIntersectingTriangle(corners[0], corners[1], corners[2], q) ;
            if(contained) return corners ;
    	}
    	return null ;
    }


}
