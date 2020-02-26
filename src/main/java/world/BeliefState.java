/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package world;

import communication.agent.AgentCommandType;
import environments.GymEnvironment;
import helperclasses.Intersections.EntityNodeIntersection;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import nl.uu.cs.aplib.agents.StateWithMessenger;
import nl.uu.cs.aplib.mainConcepts.Environment;

import java.util.*;
import java.util.function.Predicate;

/**
 * Stores agent knowledge of the agent itself, the entities it has observed, what parts of the
 * world map have been explored and its current movement goals.
 */
public class BeliefState extends StateWithMessenger {

    public String id;
    public Vec3 position;
    public Vec3 velocity;
    public MentalMap mentalMap;

    public int lastUpdated = -1;
    public boolean didNothingPreviousTurn;

    // entities mapped by id
    private HashMap<String, Entity> allEntities = new HashMap<>();
    private HashMap<String, DynamicEntity> dynamicEntities = new HashMap<>();
    private HashMap<String, InteractiveEntity> interactiveEntities = new HashMap<>();
    public HashSet<Integer> blockedNodes = new HashSet<>();//keep track of nodes which are blocked and can not be used for pathfinding
    private HashMap<String, Integer[]> nodesBlockedByEntity = new HashMap<>();

    public BeliefState() {

    }

    public BeliefState(String id, GymEnvironment env) {
        this.id = id;
        setEnvironment(env);
    }

    public Collection<Entity> getAllEntities() { return allEntities.values(); }
    public Collection<DynamicEntity> getAllDynamicEntities() { return dynamicEntities.values(); }
    public Collection<InteractiveEntity> getAllInteractiveEntities() { return interactiveEntities.values(); }


    public Boolean receivedPing = false;//store whether the agent has an unhandled ping

    public Integer[] getNodesBlockedByEntity(String id){
        return nodesBlockedByEntity.getOrDefault(id, new Integer[]{});
    }

    // search functions
    public boolean entityExists(String id) {
        return allEntities.containsKey(id);
    }
    public boolean dynamicEntityExists(String id){
        return dynamicEntities.containsKey(id);
    }
    public boolean interactiveEntityExists(String id){
        return interactiveEntities.containsKey(id);
    }

    public Entity getEntity(String id) {
        return allEntities.getOrDefault(id, null);
    }
    public InteractiveEntity getInteractiveEntity(String id){
        return interactiveEntities.getOrDefault(id, null);
    }
    public DynamicEntity getDynamicEntity(String id){
        return dynamicEntities.getOrDefault(id, null);
    }

    // predicates
    public boolean evaluateEntity(String id, Predicate<Entity> predicate) {
        return entityExists(id) && predicate.test(getEntity(id));
    }
    public boolean evaluateInteractiveEntity(String id, Predicate<InteractiveEntity> predicate){
        return interactiveEntityExists(id) && predicate.test(getInteractiveEntity(id));
    }
    public boolean evaluateDynamicEntity(String id, Predicate<DynamicEntity> predicate){
        return dynamicEntityExists(id) && predicate.test(getDynamicEntity(id));
    }

    // add
    private void addEntity(Entity newEntity){

        // set the right tick
        newEntity.lastUpdated = this.lastUpdated;

        // only store newer entities
        if(this.evaluateEntity(newEntity.id, original -> original.lastUpdated >= newEntity.lastUpdated))
            return;

        // add to all entity list
        allEntities.put(newEntity.id, newEntity);
        // add the Dynamic/Interactive entities when needed
        if(newEntity instanceof DynamicEntity)
            dynamicEntities.put(newEntity.id, (DynamicEntity) newEntity);
        else if (newEntity instanceof InteractiveEntity)
            interactiveEntities.put(newEntity.id, (InteractiveEntity) newEntity);
    }

    /**
     * Invoke the mental map to find a path.
     *
     * @param goal: The position where the agent wants to move to.
     * @return The path found
     */
    public Vec3[] navigateForce(Vec3 goal) {
        return mentalMap.navigateForce(position, goal, blockedNodes);
    }

    /**
     * Invoke the mental map to find a path, unless it already has a path towards it.
     *
     * @param goal: The position where the agent wants to move to.
     * @return The path found or the already stored path if the goal location is equal to the previous goal location
     */
    public Vec3[] navigate(Vec3 goal) {
        return mentalMap.navigate(position, goal, blockedNodes);
    }

    /**
     * Get the goal location of the agent.
     *
     * @return The current goal location used for the path finding.
     */
    public Vec3 getGoalLocation() {
        return mentalMap.getGoalLocation();
    }

    /**
     * This method will return the next wayPoint for an agent to move to.
     *
     * @return The coordinates of the next way point from the calculated path.
     */
    public Vec3 getNextWayPoint() {
        return mentalMap.getNextWayPoint();
    }

    /**
     * Update the agent's belief state with new information from the environment.
     *
     * @param observation: The observation used to update the belief state.
     */
    public void markObservation(Observation observation) {

        //check if the observation is not null
        if (observation == null) throw new IllegalArgumentException("Null observation received");

        didNothingPreviousTurn = observation.didNothing;
        position = observation.agentPosition;
        velocity = observation.velocity;
        lastUpdated++;

        for(var e : observation.entities){
            this.addEntity(e); // handle updates / new entities
            // check blocked nodes
            if (e instanceof InteractiveEntity && !interactiveEntityExists(e.id)) 
            	// WP: uh.. is the negation there correct?? Hackish ...so, only Dynamic-interactive entity
            	// will pass this guard.
                nodesBlockedByEntity.put(e.id, EntityNodeIntersection.getNodesBlockedByInteractiveEntity((InteractiveEntity) e, mentalMap.pathFinder.navmesh));
        }

        //update the seen nodes and position if there exists have a mental map
        if(mentalMap != null) {
            mentalMap.updateKnownVertices(observation.navMeshIndices);
            mentalMap.updateCurrentWayPoint(position);
        }

        //check if we need to recalculate the nodes
        if(anyInteractiveEntityChanged(observation))
            //if the blocked nodes needs updating, do so
            recalculateBlockedNodes();
    }

    /**
     * Return the position of the closest unknown node
     * @param startPosition: The position from which to look for unknown neighbours
     * @param targetPosition: The position in which direction the agent wants to explore
     * @return The vec3 coordinate of the closest neighbour or null if no neighbour is available
     */
    public Vec3 getUnknownNeighbourClosestTo(Vec3 startPosition, Vec3 targetPosition) {
        return mentalMap.getUnknownNeighbourClosestTo(startPosition, targetPosition, blockedNodes);
    }

    /**
     * Test if the agent is within the interaction bounds of an object.
     * @param id: the object to look for.
     * @return whether the object is within reach of the agent.
     */
    public boolean canInteract(String id) {
        return interactiveEntityExists(id) && getInteractiveEntity(id).canInteract(position);
    }

    /**
     * update the blocked nodes set according to the nodes blocked by entity list
     */
    private void recalculateBlockedNodes(){
        blockedNodes = new HashSet<>();

        //iterate over all key value pairs
        for(var kv: nodesBlockedByEntity.entrySet())
            //if a door is not active then it blocks the path
            if(evaluateInteractiveEntity(kv.getKey(), (InteractiveEntity ie) -> !ie.isActive))
                Collections.addAll(blockedNodes, kv.getValue());
    }

    /**
     * Check if any interactive entity has changed status
     * @return A boolean whether a recalculate is needed
     */
    private boolean anyInteractiveEntityChanged(Observation o){
        //loop over the interactive entities
        for (var newEntity: getInteractables(o.entities)) {
            //check if there is an update of an entity
            if(evaluateInteractiveEntity(newEntity.id, (InteractiveEntity originalEntity) -> originalEntity.isActive != newEntity.isActive ))
                return true;
        }
        return false;
    }

    private Iterable<InteractiveEntity> getInteractables(List<Entity> list){
        return new QArrayList<>(list)
                .where((Entity e) -> e instanceof InteractiveEntity)
                .select((Entity e) -> (InteractiveEntity) e);
    }

    public boolean entityIsUpToDate(String id){
        return evaluateEntity(id, e -> e.lastUpdated == this.lastUpdated);
    }
    public boolean withinRange(Vec3 destination){
        return withinRange(destination, 0.4f);
    }
    public boolean withinRange(String id){
        return evaluateEntity(id, e -> withinRange(e.position, 0.4f));
    }
    private boolean withinRange(Vec3 destination, float range){
        return this.position != null && this.position.distanceSquared(destination) < range * range;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sep = ", ";
        sb.append("BeliefState\n [ ");
        for (var e : allEntities.values()) {
            sb.append("\n\t");
            sb.append(e.toString());
        }
        sb.append("\n]");
        return sb.toString();
    }

    @Override
    public GymEnvironment env() {
        return (GymEnvironment) super.env();
    }

    @Override
    public BeliefState setEnvironment(Environment e) {
        super.setEnvironment(e);
        mentalMap = new MentalMap(env().pathFinder);
        return this;
    }

    @Override
    public void updateState() {
        super.updateState();
    }
}
