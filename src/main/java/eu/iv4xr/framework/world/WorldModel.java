package eu.iv4xr.framework.world;

import java.io.Serializable;
import java.util.* ;

import helperclasses.datastructures.Vec3;

/**
 * This describes a fragment of a virtual world in terms of how it is structurally
 * populated by in-world entities. This fragment can represent what an agent currently
 * sees. We can also use the same representation to represent the agent's belief on
 * how the world is structured; this may incorporate its past knowledge which may no
 * longer be uptodate.
 */
public class WorldModel {
	
	public String agentId ;
	public Vec3 position ; // agent's position
	public Vec3 velocity ; // agent's velocity
	public Vec3 extent ; // agent's dimension (x,y,z size/2)
	public long timestamp = -1 ;
	public Map<String,WorldEntity> elements = new HashMap<>() ;
	
	public WorldModel() { }
	
	/**
	 * Increase the time stamp by one unit.
	 */
	public void increaseTimeStamp() { timestamp++ ; }
	
	/**
	 * Search a top-level entity with the given id. Note that this method does NOT search 
	 * recursively in the set of sub-entities.
	 */
	public WorldEntity getElement(String id) {
		return elements.get(id) ;
	}
		
	/**
	 * Add a non-null entity e into this WorldModel, if it is NOT there already. If it already exists, we check
	 * if e's state is different from its old state as recorded in the WorldModel. If so, we will
	 * replace the old state with e, and link old as its previous state.
	 * 
	 * Either way, the stored e will get the timestamp of this WorldModel.
	 * 
	 * If e is added, this method will return e again (with updated timestamp). Else it returns the 
	 * entity representing its old state.
	 */
	public WorldEntity addEntity(WorldEntity e) {
		if (e==null) throw new IllegalArgumentException("Trying to add a null entity to a World Model.") ;
		e.assignTimeStamp(timestamp);
		var old = elements.get(e.id) ;
		if (old==null) {
			elements.put(e.id,e) ;
			return e ;
		}
		else {
			// check first if there is a state change
			if (e.hasSameState(old)) {
				// keep old; just update its timestamp:
				old.assignTimeStamp(timestamp);
				return old ;
			}
			else {
				// the entity has changes its state
				elements.put(e.id,e) ;
				e.linkPreviousState(old);
				return e ;
			}
		}
	}


}
